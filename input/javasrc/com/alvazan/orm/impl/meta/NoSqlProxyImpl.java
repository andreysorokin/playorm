package com.alvazan.orm.impl.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.util.proxy.MethodHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alvazan.orm.api.Converter;
import com.alvazan.orm.api.RowNotFoundException;
import com.alvazan.orm.layer2.nosql.NoSqlSession;
import com.alvazan.orm.layer2.nosql.Row;

public class NoSqlProxyImpl implements MethodHandler {

	private static final Logger log = LoggerFactory.getLogger(NoSqlProxyImpl.class);
	private NoSqlSession session;
	private Object entityId;
	private Method idMethod;
	private MetaClass<?> classMeta;
	private boolean isInitialized = false;
	
	public NoSqlProxyImpl(NoSqlSession session, MetaClass<?> classMeta, Object entityId) {
		this.session = session;
		this.entityId = entityId;
		this.classMeta = classMeta;
		this.idMethod = classMeta.getIdField().getIdMethod();
	}
	
	/**
	 * @param self - The proxy object(if you call any method on self, it will result in calling invoke method
	 *                   AND that includes a simple toString like if you did log.info("proxy="+self);
	 * @param superClassMethod - The method that is on the superclass like Account.java
	 * @param subclassProxyMethod - The method that is on the proxy like Account_$$_javassist_0
	 */
	@Override
	public Object invoke(Object self, Method superClassMethod, Method subclassProxyMethod, Object[] args)
			throws Throwable {
		if(log.isTraceEnabled()) {
			log.trace("name="+superClassMethod.getName()+"  superClass type="+superClassMethod.getDeclaringClass());
			log.trace("name="+subclassProxyMethod.getName()+" proxy type="+subclassProxyMethod.getDeclaringClass());
		}
		
		if(idMethod.equals(superClassMethod))
			return entityId;

		if(!isInitialized) {
			MetaIdField idField = classMeta.getIdField();
			Converter converter = idField.getConverter();
			byte[] rowKey = converter.convertToNoSql(entityId);
			List<byte[]> rowKeys = new ArrayList<byte[]>();
			rowKeys.add(rowKey);
			List<Row> rows = session.find(classMeta.getColumnFamily(), rowKeys);
			if(rows.size() != 1)
				throw new RowNotFoundException("row for type="+classMeta.getMetaClass().getName()+" not found for key="+entityId);
			else if(rows.get(0) == null)
				throw new RowNotFoundException("row for type="+classMeta.getMetaClass().getName()+" not found for key="+entityId);
			
			Row row = rows.get(0);
			classMeta.fillInInstance(row, session, self);
			isInitialized = true;
		}
		
		//Not sure if this should be subclassProxyMethod or superClassMethod
        return subclassProxyMethod.invoke(self, args);  // execute the original method.
	}

}
