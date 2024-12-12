package com.genesys.golftournament.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GolfCourseServiceTest {

    private GolfCourseService golfCourseService;

    @BeforeEach
    void setUp() {
        golfCourseService = new GolfCourseService();
    }

    @Test
    void getParForHole_ShouldReturnCorrectPar() {
        // Given holes with known par values
        assertThat(golfCourseService.getParForHole(1)).isEqualTo(4);
        assertThat(golfCourseService.getParForHole(2)).isEqualTo(5);
        assertThat(golfCourseService.getParForHole(3)).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 18})
    void isValidHole_ShouldReturnTrue_ForValidHoles(int holeNumber) {
        assertThat(golfCourseService.isValidHole(holeNumber)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {-20, 0, 20})
    void isValidHole_ShouldReturnFalse_ForInvalidHoles(int holeNumber) {
        assertThat(golfCourseService.isValidHole(holeNumber)).isFalse();
    }
}

