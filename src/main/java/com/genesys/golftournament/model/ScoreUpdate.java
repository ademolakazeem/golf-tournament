package com.genesys.golftournament.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreUpdate {
    private String playerId;
    private String playerName;
    private int holeNumber;
    private int score;
}
