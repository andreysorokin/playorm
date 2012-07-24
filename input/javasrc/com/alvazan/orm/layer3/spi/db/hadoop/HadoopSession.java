package com.alvazan.orm.layer3.spi.db.hadoop;

import java.util.List;
import java.util.Map;

import com.alvazan.orm.api.spi.db.Action;
import com.alvazan.orm.api.spi.db.Column;
import com.alvazan.orm.api.spi.db.NoSqlRawSession;
import com.alvazan.orm.api.spi.db.Row;

public class HadoopSession implements NoSqlRawSession {

	@Override
	public List<Row> find(String colFamily, List<byte[]> key) {
		return null;
	}

	@Override
	public void sendChanges(List<Action> actions) {
	}

	@Override
	public void clearDatabaseIfInMemoryType() {
		throw new UnsupportedOperationException("Not supported by actual databases.  Only can be used with in-memory db.");
	}

	@Override
	public void start(Map<String, String> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Column> columnSlice(String colFamily, byte[] rowKey,
			byte[] from, byte[] to, int batchSize) {
		throw new UnsupportedOperationException("not done here yet");
	}
}
