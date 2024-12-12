package com.genesys.golftournament.service;

import com.genesys.golftournament.model.LeaderboardInfo;
import com.genesys.golftournament.model.ScoreUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GolfTournamentServiceTest {

    @Mock
    private GolfCourseService golfCourseService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private GolfTournamentService golfTournamentService;

    @BeforeEach
    void setUp() {
        golfTournamentService = new GolfTournamentService(golfCourseService, kafkaTemplate);
    }

    @Test
    void processScore_ShouldAddNewPlayer_WhenPlayerDoesNotExist() {
        // Given
        ScoreUpdate scoreUpdate = new ScoreUpdate("player1", "John Doe", 1, 4);
        when(golfCourseService.isValidHole(1)).thenReturn(true);
        when(golfCourseService.getParForHole(1)).thenReturn(4);

        // When
        golfTournamentService.processScore(scoreUpdate);

        // Then
        List<LeaderboardInfo> leaderboard = golfTournamentService.getLeaderboard();
        assertThat(leaderboard).hasSize(1);
        assertThat(leaderboard.get(0).getPlayerName()).isEqualTo("John Doe");
        assertThat(leaderboard.get(0).getHolesCompleted()).isEqualTo(1);
        assertThat(leaderboard.get(0).getScoreDisplay()).isEqualTo("E");
    }

    @Test
    void processScore_ShouldUpdateExistingPlayer_WhenPlayerExists() {
        // Given
        ScoreUpdate firstScore = new ScoreUpdate("player1", "John Doe", 1, 3);
        ScoreUpdate secondScore = new ScoreUpdate("player1", "John Doe", 2, 5);

        when(golfCourseService.isValidHole(anyInt())).thenReturn(true);
        when(golfCourseService.getParForHole(1)).thenReturn(4);
        when(golfCourseService.getParForHole(2)).thenReturn(5);

        // When
        golfTournamentService.processScore(firstScore);
        golfTournamentService.processScore(secondScore);

        // Then
        List<LeaderboardInfo> leaderboard = golfTournamentService.getLeaderboard();
        assertThat(leaderboard).hasSize(1);
        LeaderboardInfo entry = leaderboard.get(0);
        assertThat(entry.getHolesCompleted()).isEqualTo(2);
        assertThat(entry.getScoreDisplay()).isEqualTo("-1");
    }

    @Test
    void processScore_ShouldThrowException_WhenHoleNumberIsInvalid() {
        // Given
        ScoreUpdate scoreUpdate = new ScoreUpdate("player1", "John Doe", 19, 4);
        when(golfCourseService.isValidHole(19)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> golfTournamentService.processScore(scoreUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid hole number");
    }

    @Test
    void processScore_ShouldSendAlert_WhenScoreIsUnderPar() {
        // Given
        ScoreUpdate scoreUpdate = new ScoreUpdate("player1", "John Doe", 1, 2);
        when(golfCourseService.isValidHole(1)).thenReturn(true);
        when(golfCourseService.getParForHole(1)).thenReturn(4);

        // When
        golfTournamentService.processScore(scoreUpdate);

        // Then
        verify(kafkaTemplate).send(eq("golf-alerts"),
                contains("ALERT: John Doe scored 2 under par on hole 1!"));
    }

    @Test
    void getLeaderboard_ShouldReturnSortedLeaderboard() {
        // Given
        ScoreUpdate player1Score = new ScoreUpdate("player1", "John Doe", 1, 3);
        ScoreUpdate player2Score = new ScoreUpdate("player2", "Jane Doe", 1, 5);

        when(golfCourseService.isValidHole(1)).thenReturn(true);
        when(golfCourseService.getParForHole(1)).thenReturn(4);

        // When
        golfTournamentService.processScore(player1Score);
        golfTournamentService.processScore(player2Score);

        // Then
        List<LeaderboardInfo> leaderboard = golfTournamentService.getLeaderboard();
        assertThat(leaderboard).hasSize(2);
        assertThat(leaderboard.get(0).getPlayerName()).isEqualTo("John Doe");
        assertThat(leaderboard.get(1).getPlayerName()).isEqualTo("Jane Doe");
    }
}
