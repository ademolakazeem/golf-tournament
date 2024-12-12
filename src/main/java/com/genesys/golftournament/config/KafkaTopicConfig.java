package com.genesys.golftournament.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public static final String LEADERBOARD_GROUP_ID = "leaderboard-group";
    public static final String ALERTS_GROUP_ID = "alerts-group";
    public static final String LEADERBOARD_UPDATES_TOPIC = "leaderboard-updates";
    public static final String ALERTS_TOPIC = "golf-alerts";


    @Bean
    public NewTopic leaderboardTopic() {
        return TopicBuilder.name(LEADERBOARD_UPDATES_TOPIC).build();
    }

    @Bean
    public NewTopic alertTopic() {
        return TopicBuilder.name(ALERTS_TOPIC).build();
    }
}
