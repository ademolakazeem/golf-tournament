package com.genesys.golftournament.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.genesys.golftournament.config.KafkaTopicConfig.ALERTS_GROUP_ID;
import static com.genesys.golftournament.config.KafkaTopicConfig.ALERTS_TOPIC;
import static com.genesys.golftournament.config.KafkaTopicConfig.LEADERBOARD_GROUP_ID;
import static com.genesys.golftournament.config.KafkaTopicConfig.LEADERBOARD_UPDATES_TOPIC;

@Service
@Slf4j
public class LeaderboardUpdateConsumer {

    @KafkaListener(topics = LEADERBOARD_UPDATES_TOPIC, groupId = LEADERBOARD_GROUP_ID)
    public void consumeLeaderboardUpdate(String leaderboardJson) {
        // Handle leaderboard updates (e.g., broadcast to WebSocket clients)
        log.info("Received leaderboard update: {}", leaderboardJson);
    }

    @KafkaListener(topics = ALERTS_TOPIC, groupId = ALERTS_GROUP_ID)
    public void consumeAlerts(String alert) {
        // Handle alerts (e.g., broadcast to WebSocket clients)
        log.info("Received alert: {}", alert);
    }
}

