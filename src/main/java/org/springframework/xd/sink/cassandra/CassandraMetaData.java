package org.springframework.xd.sink.cassandra;

import org.springframework.xd.module.options.spi.ModuleOption;


public class CassandraMetaData {

	private String contactPoints;
	
	private int port;
	
	private String keySpace;
	
	private String table;

	public String getContactPoints() {
		return contactPoints;
	}

	@ModuleOption("Cassandra ContactPoint")
	public void setContactPoints(String contactPoints) {
		this.contactPoints = contactPoints;
	}

	public int getPort() {
		return port;
	}

	@ModuleOption("Cassandra Port")
	public void setPort(int port) {
		this.port = port;
	}

	public String getKeySpace() {
		return keySpace;
	}

	@ModuleOption("Cassandra Keyspace")
	public void setKeySpace(String keySpace) {
		this.keySpace = keySpace;
	}

	public String getTable() {
		return table;
	}

	@ModuleOption("Target table")
	public void setTable(String table) {
		this.table = table;
	}

	
	
	
}
