package com.genesys.golftournament.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;


@Configuration
public class KafkaConsumerConfig {

    private final KafkaProdConsConfig kafkaProdConsConfig;

    @Autowired
    public KafkaConsumerConfig(KafkaProdConsConfig kafkaProdConsConfig) {
        this.kafkaProdConsConfig = kafkaProdConsConfig;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(kafkaProdConsConfig.consumerConfig());
    }

    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, String>> klcFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // factory.setConcurrency(1);
        //factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }
}
