package life.plank.juna.zone.data.network.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-sharath on 4/10/2018.
 */

public class MatchScores {
    private String header;
    private String rounds;
    private String teamOne;
    private String teamTwo;
    private String time;
    private String tag;
    private String teamOneScore;
    private String teamTwoScore;

    public MatchScores(String tag, String header, String rounds, String teamOne, String teamTwo, String time, String teamOneScore, String teamTwoScore) {
        this.header = header;
        this.rounds = rounds;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.time = time;
        this.tag = tag;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
    }

    public static List<MatchScores> getScores(Context context) {
        List<MatchScores> scoresModelList = new ArrayList<>();
        scoresModelList.add(new MatchScores("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "0", "1"));
        scoresModelList.add(new MatchScores("body", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "2", "4"));
        scoresModelList.add(new MatchScores("header", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.full_match_time), "1", "0"));
        scoresModelList.add(new MatchScores("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.half_match_time), "3", "0"));
        scoresModelList.add(new MatchScores("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.full_match_time), "2", "0"));
        scoresModelList.add(new MatchScores("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.full_match_time), "1", "3"));
        scoresModelList.add(new MatchScores("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.full_match_time), "0", "1"));
        scoresModelList.add(new MatchScores("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.full_match_time), "2", "3"));
        return scoresModelList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRounds() {
        return rounds;
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(String teamOne) {
        this.teamOne = teamOne;
    }

    public String getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(String teamTwo) {
        this.teamTwo = teamTwo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeamOneScores() {
        return teamOneScore;
    }

    public void setTeamOneScores(String teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public String getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(String teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

}
