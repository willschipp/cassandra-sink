package org.springframework.xd.sink.cassandra;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableIntegration
public class CassandraConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Value("${contactPoints}")
	private String contactPoints;
	
	@Value("${port}")
	private int port;
	
	@Value("${keySpace}")
	private String keySpace;
	
	@Bean
	public MessageChannel input() {
		return new DirectChannel();
	}

	/**
	 * simple transformation flow which goes
	 * input -> transformer (json to map) -> service activator
	 * @return
	 */
	@Bean
	public IntegrationFlow sinkFlow() {
		return IntegrationFlows.from(input())
				.transform(new JsonToObjectTransformer(Map.class))
				.handle(new ServiceActivatingHandler(sink())).get();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	@Bean
	public CassandraMapSink sink() {
		return new CassandraMapSink();
	}

	@Bean
	public CassandraClusterFactoryBean cluster() {

		CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
		cluster.setContactPoints(contactPoints);
		cluster.setPort(port);

		return cluster;
	}

	@Bean
	public CassandraMappingContext mappingContext() {
		return new BasicCassandraMappingContext();
	}

	@Bean
	public CassandraConverter converter() {
		return new MappingCassandraConverter(mappingContext());
	}

	@Bean
	public CassandraSessionFactoryBean session() throws Exception {

		CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
		session.setCluster(cluster().getObject());
		session.setKeyspaceName(keySpace);
		session.setConverter(converter());
		session.setSchemaAction(SchemaAction.NONE);

		return session;
	}

	@Bean
	public CassandraOperations cassandraTemplate() throws Exception {
		return new CassandraTemplate(session().getObject());
	}

}
