package com.ibm.ups.testmq;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.jms.JMSException;

@Configuration
@EnableTransactionManagement
public class JmsConfig {

    @Value("${project.mq.host}")
    private String host;

    @Value("${project.mq.port}")
    private Integer port;

    @Value("${project.mq.queue-manager}")
    private String queueManager;

    @Value("${project.mq.channel}")
    private String channel;

    @Value("${project.mq.username}")
    private String username;

    @Value("${project.mq.password}")
    private String password;

    @Value("${project.mq.queue}")
    private String queue;

    @Value("${project.mq.receive-timeout}")
    private long receiveTimeout;

    @Bean
    public MQQueue destinationQueue() throws JMSException {
        return new MQQueue(queue);
    }

    @Bean
    public MQQueueConnectionFactory jmsConnectionFactory() throws JMSException {
        MQQueueConnectionFactory mQQueueConnectionFactory = new MQQueueConnectionFactory();
        mQQueueConnectionFactory.setQueueManager(queueManager);
        mQQueueConnectionFactory.setHostName(host);
        //mQQueueConnectionFactory.setPort(port);
        return mQQueueConnectionFactory;
    }


    @Bean
    public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(
            MQQueueConnectionFactory jmsConnectionFactory) {
        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        // userCredentialsConnectionFactoryAdapter.setUsername(username);
        // userCredentialsConnectionFactoryAdapter.setPassword(password);
        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(jmsConnectionFactory);
        return userCredentialsConnectionFactoryAdapter;
    }

    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(
            UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
        cachingConnectionFactory.setSessionCacheSize(500);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsOperations jmsOperations(MQQueueConnectionFactory cachingConnectionFactory, MQQueue destinationQueue) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setDefaultDestination(destinationQueue);
        jmsTemplate.setReceiveTimeout(2000);
        return jmsTemplate;
    }

    @Bean(name="jmsTransactionManager")
    public PlatformTransactionManager jmsTransactionManager(CachingConnectionFactory cachingConnectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(cachingConnectionFactory);
        return jmsTransactionManager;
    }
}
