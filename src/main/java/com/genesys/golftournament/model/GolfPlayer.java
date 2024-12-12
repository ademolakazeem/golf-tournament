package com.genesys.golftournament.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GolfPlayer {
    private String id;
    private String name;
    private Map<Integer, Integer> holeScores = new HashMap<>();
    private int holesCompleted;
    private int scoreRelativeToPar;
    private boolean roundComplete;
}
