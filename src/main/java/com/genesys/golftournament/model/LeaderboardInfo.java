package com.genesys.golftournament.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardInfo {
    private String playerName;
    private int holesCompleted;
    private String scoreDisplay; // Will show either "E" or +/- number
    private int totalScore;
}
