package com;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import io.netty.bootstrap.Bootstrap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class TransactionConfiguration {
    //1.properties
    //2.kafka factory
    //3.Tempate
    @Bean
    Properties kafkaProperties(){
        Properties properties=new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"friends1_group");
        return properties;
    }
    @Bean
    ConsumerFactory<String,String> getConsumerFactory(){
        return new DefaultKafkaConsumerFactory(kafkaProperties());
    }
    //this is only for consumer because they have to listen simultaneous ... this property is required
    @Bean
    ConcurrentKafkaListenerContainerFactory<String,String> concurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory=new ConcurrentKafkaListenerContainerFactory();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(getConsumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }
    ProducerFactory<String,String> getProducerFactory(){
        return new DefaultKafkaProducerFactory(kafkaProperties());
    }
    //this kafka template is only for producers not for consumers
    @Bean
    KafkaTemplate<String,String> getKafkaTemplate(){
        return new KafkaTemplate<>(getProducerFactory());
    }
    @Bean
    RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
