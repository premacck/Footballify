package life.plank.juna.zone.domain.service;

import android.content.Context;

import life.plank.juna.zone.data.network.model.FootballMatch;

/**
 * Created by plank-sobia on 10/9/2017.
 */

public class GameService {
    private Context context;

    public GameService(Context context) {
        this.context = context;
    }

    public Integer computeScore(FootballMatch footballMatch, String selectedTeamName, Integer homeTeamGuessScore, Integer visitingTeamGuessScore, Integer playerScore) {
        Integer score;

        if (footballMatch.getVisitingTeamScore() > footballMatch.getHomeTeamScore()) {
            // visiting team is the winner
            if (footballMatch.getVisitingTeam().getName().equals(selectedTeamName)) {
                // user has chosen visiting team
                if (footballMatch.getVisitingTeamScore() == visitingTeamGuessScore) {
                    playerScore += 2 * (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());
                } else {
                    // assign the least score
                    score = Math.min(visitingTeamGuessScore - homeTeamGuessScore, footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());
                    playerScore += score;
                }

            } else {
                // user has chosen home team
                playerScore -= (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());
            }

        } else if (footballMatch.getVisitingTeamScore() < footballMatch.getHomeTeamScore()) {
            // homeTeam is the winner
            if (footballMatch.getHomeTeam().getName().equals(selectedTeamName)) {
                // user has chosen home team
                if (footballMatch.getHomeTeamScore() == homeTeamGuessScore) {
                    playerScore += 2 * (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());

                } else {
                    // assign the least score
                    score = Math.min(homeTeamGuessScore - visitingTeamGuessScore, footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());
                    playerScore += score;
                }

            } else {
                // user has chosen visitng team
                playerScore -= (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());
            }
        }
        return playerScore;
    }
}
