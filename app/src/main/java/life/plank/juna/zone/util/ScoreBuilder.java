package life.plank.juna.zone.util;

import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.AppConstants.SPACE;
import static life.plank.juna.zone.util.AppConstants.WIDE_DASH;
import static life.plank.juna.zone.util.AppConstants.WIDE_SPACE;

public class ScoreBuilder {

    private StringBuilder stringBuilder;

    private ScoreBuilder() {
        stringBuilder = new StringBuilder();
    }

    public static ScoreBuilder start() {
        return new ScoreBuilder();
    }

    public ScoreBuilder spacingByPage(boolean isBoard) {
        stringBuilder.append(isBoard ? SPACE : WIDE_SPACE);
        return this;
    }

    public ScoreBuilder dashingByPage(boolean isBoard) {
        stringBuilder.append(isBoard ? DASH : WIDE_DASH);
        return this;
    }

    public ScoreBuilder setGoals(int goals) {
        stringBuilder.append(goals);
        return this;
    }

    public ScoreBuilder setGoals(int homeGoals, int homePenaltyGoals) {
        stringBuilder.append(homeGoals)
                .append(" (").append(homePenaltyGoals).append(") ");
        return this;
    }

    public String getScore() {
        return stringBuilder.toString();
    }

    public static String getWinScore(int homeGoals, int visitingGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .setGoals(homeGoals)
                .spacingByPage(isBoard)
                .setGoals(visitingGoals)
                .getScore();
    }

    public static String getTiedScore(int homeGoals, int visitingGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .setGoals(homeGoals)
                .dashingByPage(isBoard)
                .setGoals(visitingGoals)
                .getScore();
    }

    public static String getWinPenaltyScore(int homeGoals, int homePenaltyGoals, int visitingGoals, int visitingPenaltyGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .setGoals(homeGoals, homePenaltyGoals)
                .spacingByPage(isBoard)
                .setGoals(visitingGoals, visitingPenaltyGoals)
                .getScore();
    }

    public static String getTiedPenaltyScore(int homeGoals, int homePenaltyGoals, int visitingGoals, int visitingPenaltyGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .setGoals(homeGoals, homePenaltyGoals)
                .dashingByPage(isBoard)
                .setGoals(visitingGoals, visitingPenaltyGoals)
                .getScore();
    }
}