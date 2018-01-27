package com.ibm.ups.testmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestmqApplicationTests {
	@Autowired
	private JmsOperations jmsOperations;

	@Test
	public void contextLoads() {
	}

	@Test
	public void sendMsg() {
		//jmsOperations.convertAndSend("Test");
		jmsOperations.convertAndSend("OMS_TEST_QUEUE", "hello" + new Date());
	}

	@Test
	@Transactional(value = "jmsTransactionManager")
	public void transaction() {
		jmsOperations.convertAndSend("transaction test");
//		jmsOperations.convertAndSend("OMS_TEST_QUEUE", "transaction test");
	}
}
