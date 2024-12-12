package com.genesys.golftournament.service;

import org.springframework.stereotype.Service;


@Service
public class GolfCourseService {
    private final int[] parScores = {4,5,3,4,5,4,4,3,4,4,4,4,4,5,4,3,5,3};

    public int getParForHole(int holeNumber) {
        return parScores[holeNumber - 1];
    }

    public boolean isValidHole(int holeNumber) {
        return holeNumber >= 1 && holeNumber <= 18;
    }
}

