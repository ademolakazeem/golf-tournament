package com.genesys.golftournament.controller;

import com.genesys.golftournament.service.GolfTournamentService;
import com.genesys.golftournament.model.ScoreUpdate;
import com.genesys.golftournament.model.LeaderboardInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
class GolfTournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //MockBean is deprecated, but it's not removed yet, so, can still be used.
    private GolfTournamentService golfTournamentService;


    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void postScore_ValidInput_ShouldReturnOk() throws Exception {

        ScoreUpdate scoreUpdate = new ScoreUpdate("golf-player-1", "Nemanja Matic", 1, 4);
        doNothing().when(golfTournamentService).processScore(any(ScoreUpdate.class));

        mockMvc.perform(post("/api/tournament/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scoreUpdate)))
                .andExpect(status().isOk());

        verify(golfTournamentService, times(1)).processScore(scoreUpdate);
    }

    @Test
    void postScore_NullInput_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/api/tournament/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(golfTournamentService, never()).processScore(any());
    }

    @Test
    void postScore_InvalidJson_ShouldReturnBadRequest() throws Exception {

        String invalidJson = "{\"playerId\":\"golf-player-1\", invalid}";

        mockMvc.perform(post("/api/tournament/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(golfTournamentService, never()).processScore(any());
    }

    @Test
    void getLeaderboard_ShouldReturnLeaderboardList() throws Exception {

        List<LeaderboardInfo> leaderboard = Arrays.asList(
                new LeaderboardInfo("Jonathan Southgate", 8, "-1", -4),
                new LeaderboardInfo("Desmond Elliot", 18, "E", 0)
        );
        when(golfTournamentService.getLeaderboard()).thenReturn(leaderboard);

        mockMvc.perform(get("/api/tournament/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].playerName").value("Jonathan Southgate"))
                .andExpect(jsonPath("$[0].holesCompleted").value(8))
                .andExpect(jsonPath("$[0].scoreDisplay").value("-1"))
                .andExpect(jsonPath("$[1].playerName").value("Desmond Elliot"))
                .andExpect(jsonPath("$[1].holesCompleted").value(18));

        verify(golfTournamentService, times(1)).getLeaderboard();
    }

    @Test
    void getLeaderboard_EmptyLeaderboard_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(golfTournamentService.getLeaderboard()).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/api/tournament/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

}