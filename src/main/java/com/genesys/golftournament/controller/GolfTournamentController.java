package com.genesys.golftournament.controller;


import com.genesys.golftournament.model.LeaderboardInfo;
import com.genesys.golftournament.model.ScoreUpdate;
import com.genesys.golftournament.service.GolfTournamentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Golf Tournament API", description = "API for managing golf tournament scores and leaderboard")
@RequestMapping("/api/tournament")
public class GolfTournamentController {

    private final GolfTournamentService golfTournamentService;

    @Autowired
    public GolfTournamentController(GolfTournamentService golfTournamentService) {
        this.golfTournamentService = golfTournamentService;
    }

    @Operation(summary = "Submit a score update for a player")
    @PostMapping("/scores")
    public ResponseEntity<Void> postScore(@RequestBody ScoreUpdate scoreUpdate) {
        golfTournamentService.processScore(scoreUpdate);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get the current leaderboard")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardInfo>> getLeaderboard() {
        return ResponseEntity.ok(golfTournamentService.getLeaderboard());
    }
}

