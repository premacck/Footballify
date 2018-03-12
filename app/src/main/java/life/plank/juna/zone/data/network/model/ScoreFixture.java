package life.plank.juna.zone.data.network.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-sharath on 2/21/2018.
 */
public class ScoreFixture {
    private String header;
    private String rounds;
    private String teamOne;
    private String teamTwo;
    private String time;
    private String tag;

    public ScoreFixture(String tag, String header, String rounds, String teamOne, String teamTwo, String time) {
        this.header = header;
        this.rounds = rounds;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.time = time;
        this.tag = tag;
    }

    public static List<ScoreFixture> getFixture(Context context) {
        List<ScoreFixture> fixtureModelList = new ArrayList<>();
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.time_south)));
        fixtureModelList.add(new ScoreFixture("header", context.getString(R.string.header_fa_cup), context.getString(R.string.round_fa_cup), context.getString(R.string.team_one_fa_cup), context.getString(R.string.team_two_fa_cup), "" + context.getString(R.string.time_fa_cup)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_league), " ", context.getString(R.string.team_one_league), context.getString(R.string.team_two_league), context.getString(R.string.time_league)));
        fixtureModelList.add(new ScoreFixture("body", context.getString(R.string.header_south), " ", context.getString(R.string.team_one_south), context.getString(R.string.team_two_south), context.getString(R.string.time_south)));
        return fixtureModelList;
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
}
