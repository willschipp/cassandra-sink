package org.springframework.xd.sink.cassandra;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CassandraConfiguration.class)
public class CassandraConfigurationIT {

	@Autowired
	private MessageChannel input;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void test() throws Exception {
		Map<String,Object> payload = new HashMap<String,Object>();
		payload.put("id", UUID.randomUUID().toString());
		payload.put("value", "some value");
		String json = objectMapper.writeValueAsString(payload);
		Message<?> message = MessageBuilder.withPayload(json).build();
		//send
		input.send(message);
		//wait
		Thread.sleep(5 * 1000);
	}

}
