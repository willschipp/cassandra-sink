package org.springframework.xd.sink.cassandra;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * Sink for Cassandra.
 * 
 * takes a Map and iterates over the key/values to insert into the target table
 * 
 * @author willschipp
 *
 */
@Component
public class CassandraMapSink {
	
	private static final Log logger = LogFactory.getLog(CassandraMapSink.class);

	@Autowired
	private CassandraOperations cassandraOperations;
	
	@Value("${table}")
	private String targetTable;
	
	/**
	 * expects the payload to be a Map of String and Objects
	 * @param payload
	 */
	@ServiceActivator
	public void sink(Map<String,Object> payload) {
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
			logger.error(e);
		}
	}
	
}
