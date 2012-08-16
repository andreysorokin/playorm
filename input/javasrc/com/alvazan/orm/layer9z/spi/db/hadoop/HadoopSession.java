package com.alvazan.orm.layer9z.spi.db.hadoop;

import java.util.List;
import java.util.Map;

import com.alvazan.orm.api.spi9.db.Action;
import com.alvazan.orm.api.spi9.db.Column;
import com.alvazan.orm.api.spi9.db.NoSqlRawSession;
import com.alvazan.orm.api.spi9.db.Row;
import com.alvazan.orm.api.spi9.db.ScanInfo;

public class HadoopSession implements NoSqlRawSession {

	@Override
	public List<Row> find(String colFamily, List<byte[]> key) {
		return null;
	}

	@Override
	public void sendChanges(List<Action> actions, Object ormSession) {
	}

	@Override
	public void clearDatabase() {
		throw new UnsupportedOperationException("Not supported by actual databases.  Only can be used with in-memory db.");
	}

	@Override
	public void start(Map<String, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Column> columnRangeScan(ScanInfo info, byte[] from, boolean fromInclusive, byte[] to, boolean toInclusive) {
		throw new UnsupportedOperationException("not done here yet");
	}

	@Override
	public Iterable<Column> columnRangeScanAll(ScanInfo scanInfo) {
		throw new UnsupportedOperationException("not done here yet");
	}

}
