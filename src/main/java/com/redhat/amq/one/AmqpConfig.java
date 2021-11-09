package com.redhat.amq.one;

import org.amqphub.spring.boot.jms.autoconfigure.AMQP10JMSConnectionFactoryCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class AmqpConfig
{
   private static final Logger LOG = LoggerFactory.getLogger(AmqpConfig.class);

   @Value("${amqp.broker.scheme}")
   private String amqpBrokerScheme;
   
   @Value("${amqp.broker.host}")
   private String amqpBrokerHost;
   
   @Value("${amqp.broker.port}")
   private String amqpBrokerPort;
   
   @Value("${jms.username}")
   private String jmsUsername;
   
   @Value("${jms.password}")
   private String jmsPassword;
   
   @Value("${transport.keyStoreLocation}")
   private String transportKeyStoreLocation;
   
   @Value("${transport.keyStorePassword}")
   private String transportKeyStorePassword;
   
   @Value("${transport.trustStoreLocation}")
   private String transportTrustStoreLocation;
   
   @Value("${transport.trustStorePassword}")
   private String transportTrustStorePassword;
   
   @Value("${transport.verifyHost}")
   private String transportVerifyHost;

   @Bean
   public AMQP10JMSConnectionFactoryCustomizer myAMQP10Configuration()
   {
      return (factory) ->
      {
//         factory.setUsername(jmsUsername);
//         factory.setPassword(jmsPassword);
//         factory.setPopulateJMSXUserID(true);
         factory.setRemoteURI(remoteUri());
      };
   }

   private String remoteUri()
   {

      UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme(amqpBrokerScheme)
            .host(amqpBrokerHost)
            .port(amqpBrokerPort)
            .path("/")
            .queryParam("jms.username", jmsUsername)
            .queryParam("jms.password", jmsPassword)
//            .queryParam("transport.trustAll", "true")
//            .queryParam("transport.useOpenSSL", "true")
//            .queryParam("transport.keyStoreLocation", transportKeyStoreLocation)
//            .queryParam("transport.keyStorePassword", transportKeyStorePassword)
            .queryParam("transport.trustStoreLocation", transportTrustStoreLocation)
            .queryParam("transport.trustStorePassword", transportTrustStorePassword)
            .queryParam("transport.verifyHost", transportVerifyHost)
            .build();
      
      LOG.debug(uriComponents.toUriString());
      
      return uriComponents.toUriString();
   }

}
