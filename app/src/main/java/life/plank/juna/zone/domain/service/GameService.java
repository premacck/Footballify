package life.plank.juna.zone.domain.service;

import life.plank.juna.zone.data.network.model.FootballMatch;

/**
 * Created by plank-sobia on 10/9/2017.
 */

public class GameService {

    public Integer computeScore(FootballMatch footballMatch, String selectedTeamName, Integer homeTeamGuessScore, Integer visitingTeamGuessScore, Integer playerScore) {
        Integer score;

        if (footballMatch.getVisitingTeamScore() > footballMatch.getHomeTeamScore()) {
            // visiting team is the winner
            if (footballMatch.getVisitingTeam().getName() == selectedTeamName) {
                // user has chosen visiting team
                if (footballMatch.getVisitingTeamScore() == visitingTeamGuessScore) {
                    playerScore = playerScore + 2 * (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());
                } else {
                    // assign the least score
                    if ((visitingTeamGuessScore - homeTeamGuessScore) < (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore()))
                        score = (visitingTeamGuessScore - homeTeamGuessScore);
                    else
                        score = (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());

                    playerScore = playerScore + score;
                }

            } else {
                // user has chosen home team
                playerScore = playerScore - (footballMatch.getVisitingTeamScore() - footballMatch.getHomeTeamScore());
            }

        } else if (footballMatch.getVisitingTeamScore() < footballMatch.getHomeTeamScore()) {
            // homeTeam is the winner
            if (footballMatch.getHomeTeam().getName() == selectedTeamName) {
                // user has chosen home team
                if (footballMatch.getHomeTeamScore() == homeTeamGuessScore) {
                    playerScore = playerScore + 2 * (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());

                } else {
                    // assign the least score
                    if ((homeTeamGuessScore - visitingTeamGuessScore) < (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore()))
                        score = (homeTeamGuessScore - visitingTeamGuessScore);
                    else
                        score = (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());

                    playerScore = playerScore + score;
                }

            } else {
                // user has chosen visitng team
                playerScore = playerScore - (footballMatch.getHomeTeamScore() - footballMatch.getVisitingTeamScore());
            }
        }
        return playerScore;
    }
}
