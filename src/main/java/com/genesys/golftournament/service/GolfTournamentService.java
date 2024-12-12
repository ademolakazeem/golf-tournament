package com.genesys.golftournament.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.golftournament.model.GolfPlayer;
import com.genesys.golftournament.model.LeaderboardInfo;
import com.genesys.golftournament.model.ScoreUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.genesys.golftournament.config.KafkaTopicConfig.ALERTS_TOPIC;
import static com.genesys.golftournament.config.KafkaTopicConfig.LEADERBOARD_UPDATES_TOPIC;

@Service
@Slf4j
public class GolfTournamentService {
    private final Map<String, GolfPlayer> players = new ConcurrentHashMap<>();
    private final GolfCourseService golfCourseService;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public GolfTournamentService(GolfCourseService golfCourseService, KafkaTemplate<String, String> kafkaTemplate) {
        this.golfCourseService = golfCourseService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void processScore(ScoreUpdate scoreUpdate) {
        GolfPlayer player = players.computeIfAbsent(scoreUpdate.getPlayerId(),
                id -> new GolfPlayer(id, scoreUpdate.getPlayerName(), new HashMap<>(), 0, 0, false));

        if (!golfCourseService.isValidHole(scoreUpdate.getHoleNumber())) {
            throw new IllegalArgumentException("Invalid hole number");
        }

        // Calculate score relative to par for this hole
        int par = golfCourseService.getParForHole(scoreUpdate.getHoleNumber());
        int scoreRelativeToPar = scoreUpdate.getScore() - par;

        // Update player's data
        player.getHoleScores().put(scoreUpdate.getHoleNumber(), scoreUpdate.getScore());
        player.setHolesCompleted(player.getHoleScores().size());
        player.setScoreRelativeToPar(player.getScoreRelativeToPar() + scoreRelativeToPar);
        player.setRoundComplete(player.getHolesCompleted() == 18);

        // Check for under par score alert
        if (scoreRelativeToPar <= -1) {
            sendUnderParAlert(player.getName(), scoreUpdate.getHoleNumber(), scoreRelativeToPar);
        }

        // Update leaderboard
        publishLeaderboardUpdate();
    }

    private void sendUnderParAlert(String playerName, int holeNumber, int scoreRelativeToPar) {
        String message = String.format("GOLF TOURNAMENT ALERT: %s scored %d under par on hole %d! 🥳🎉🎉",
                playerName, Math.abs(scoreRelativeToPar), holeNumber);
        log.info(message);
        kafkaTemplate.send(ALERTS_TOPIC, message);
    }

    public List<LeaderboardInfo> getLeaderboard() {
        return players.values().stream()
                .map(this::createLeaderboardEntry)
                .sorted(Comparator.comparingInt(LeaderboardInfo::getTotalScore))
                .collect(Collectors.toList());
    }

    private LeaderboardInfo createLeaderboardEntry(GolfPlayer player) {
        String scoreDisplay = player.getScoreRelativeToPar() == 0 ? "E" :
                String.format("%+d", player.getScoreRelativeToPar());
        return new LeaderboardInfo(
                player.getName(),
                player.getHolesCompleted(),
                scoreDisplay,
                player.getScoreRelativeToPar()
        );
    }

    private void publishLeaderboardUpdate() {
        try {
            String leaderboardJson = new ObjectMapper()
                    .writeValueAsString(getLeaderboard());
            kafkaTemplate.send(LEADERBOARD_UPDATES_TOPIC, leaderboardJson);
        } catch (JsonProcessingException e) {
            log.error("Error publishing leaderboard update", e);
        }
    }
}

