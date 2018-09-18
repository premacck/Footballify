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

    public ScoreBuilder space() {
        stringBuilder.append(SPACE);
        return this;
    }

    public ScoreBuilder wideSpace() {
        stringBuilder.append(WIDE_SPACE);
        return this;
    }

    public ScoreBuilder wideDash() {
        stringBuilder.append(WIDE_DASH);
        return this;
    }

    public ScoreBuilder dash() {
        stringBuilder.append(DASH);
        return this;
    }

    public ScoreBuilder homeGoals(int homeGoals) {
        stringBuilder.append(homeGoals);
        return this;
    }

    public ScoreBuilder visitingGoals(int visitingGoals) {
        stringBuilder.append(visitingGoals);
        return this;
    }

    public ScoreBuilder homeGoals(int homeGoals, int homePenaltyGoals) {
        stringBuilder.append(homeGoals)
                .append(" (").append(homePenaltyGoals).append(") ");
        return this;
    }

    public ScoreBuilder visitingGoals(int visitingGoals, int visitingPenaltyGoals) {
        stringBuilder.append(visitingGoals)
                .append(" (").append(visitingPenaltyGoals).append(") ");
        return this;
    }

    public String getScore() {
        return stringBuilder.toString();
    }

    public static String getWinScore(int homeGoals, int visitingGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .homeGoals(homeGoals)
                .spacingByPage(isBoard)
                .visitingGoals(visitingGoals)
                .getScore();
    }

    public static String getTiedScore(int homeGoals, int visitingGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .homeGoals(homeGoals)
                .dashingByPage(isBoard)
                .visitingGoals(visitingGoals)
                .getScore();
    }

    public static String getWinPenaltyScore(int homeGoals, int homePenaltyGoals, int visitingGoals, int visitingPenaltyGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .homeGoals(homeGoals, homePenaltyGoals)
                .spacingByPage(isBoard)
                .visitingGoals(visitingGoals, visitingPenaltyGoals)
                .getScore();
    }

    public static String getTiedPenaltyScore(int homeGoals, int homePenaltyGoals, int visitingGoals, int visitingPenaltyGoals, boolean isBoard) {
        return ScoreBuilder.start()
                .homeGoals(homeGoals, homePenaltyGoals)
                .dashingByPage(isBoard)
                .visitingGoals(visitingGoals, visitingPenaltyGoals)
                .getScore();
    }
}