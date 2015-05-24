package org.springframework.xd.sink.cassandra;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

@Component
public class CassandraMapSink {

	@Autowired
	private CassandraOperations cassandraOperations;
	
	@Autowired
	private String targetTable;
	
	@ServiceActivator
	public void sink(Map<String,Object> payload) {
		System.out.println(payload);
		//build and insert
		Insert insert = QueryBuilder.insertInto(targetTable);
		insert.setConsistencyLevel(ConsistencyLevel.ONE);
		//loop
		for (Entry<String,Object> entry : payload.entrySet()) {
			insert.value(entry.getKey(), entry.getValue());
		}//end for
		//execute
		try {
			cassandraOperations.execute(insert);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
