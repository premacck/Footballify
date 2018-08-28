package life.plank.juna.zone.util;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.data.network.model.SectionedFixtureDate;
import life.plank.juna.zone.data.network.model.SectionedFixtureMatchDay;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection;

import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.LIVE_MATCHES;
import static life.plank.juna.zone.domain.service.FootballFixtureClassifierService.FixtureSection.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.AppConstants.WIDE_DASH;
import static life.plank.juna.zone.util.AppConstants.WIDE_SPACE;
import static life.plank.juna.zone.util.DateUtil.getFutureMatchTime;
import static life.plank.juna.zone.util.DateUtil.getTimeDiffFromNow;

public class DataUtil {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null;
    }

    static String formatInt(int i) {
        return String.valueOf(i > 9 ? i : "0" + i);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static String getSeparator(ScoreFixture scoreFixture, FixtureSection fixtureSection, ImageView winPointer) {
        if (fixtureSection == PAST_MATCHES) {
            return getPastMatchSeparator(scoreFixture, winPointer);
        } else if (fixtureSection == LIVE_MATCHES) {
            if (getTimeDiffFromNow(scoreFixture.getMatchStartTime()) < 0) {
                winPointer.setVisibility(View.INVISIBLE);
                return scoreFixture.getHomeGoals() + WIDE_DASH + scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                return getFutureMatchTime(scoreFixture.getMatchStartTime());
            }
        } else {
            winPointer.setVisibility(View.INVISIBLE);
            return getFutureMatchTime(scoreFixture.getMatchStartTime());
        }
    }

    private static String getPastMatchSeparator(ScoreFixture scoreFixture, ImageView winPointer) {
        String teamNameSeparator;
        if (scoreFixture.getAwayTeamPenaltyScore() == 0 && scoreFixture.getHomeTeamPenaltyScore() == 0) {
            if (scoreFixture.getHomeGoals() > scoreFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_home);
                teamNameSeparator = scoreFixture.getHomeGoals() + WIDE_SPACE +
                        scoreFixture.getAwayGoals();
            } else if (scoreFixture.getAwayGoals() > scoreFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_away);
                teamNameSeparator = scoreFixture.getHomeGoals() + WIDE_SPACE +
                        scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = scoreFixture.getHomeGoals() + WIDE_DASH +
                        scoreFixture.getAwayGoals();
            }
        } else {
            if (scoreFixture.getHomeGoals() > scoreFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_home);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + WIDE_SPACE +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            } else if (scoreFixture.getAwayGoals() > scoreFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(R.drawable.ic_win_away);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + WIDE_SPACE +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = scoreFixture.getHomeGoals() +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" + WIDE_DASH +
                        "(" + scoreFixture.getHomeTeamPenaltyScore() + ")" +
                        scoreFixture.getAwayGoals();
            }
        }
        return teamNameSeparator;
    }

    /**
     * Removes empty sections in the sectionsList, if any.
     */
    public static List<SectionedFixtureMatchDay> removeEmptyMatchDays(List<SectionedFixtureMatchDay> list) {
        List<SectionedFixtureMatchDay> newList = new ArrayList<>(list);
        for (SectionedFixtureMatchDay fixture : newList) {
            if (fixture.getSectionedFixtureDateList().isEmpty()) {
                list.remove(fixture);
            }
        }
        newList.clear();
        return list;
    }

    public static List<SectionedFixtureDate> removeEmptyFixtureDates(List<SectionedFixtureDate> list) {
        List<SectionedFixtureDate> newList = new ArrayList<>(list);
        for (SectionedFixtureDate fixture : newList) {
            if (fixture.getScoreFixtureList().isEmpty()) {
                list.remove(fixture);
            }
        }
        newList.clear();
        return list;
    }

    public static ZoneLiveData getZoneLiveData(Intent intent, String key, Gson gson) {
        return gson.fromJson(intent.getStringExtra(key), new TypeToken<ZoneLiveData>() {
        }.getType());
    }

    public static String getFormattedExtraMinutes(int minute) {
        return minute > 9 ? minute + ".00" : "0" + minute + ".00";
    }

    public static List<MatchEvent> extractSubstitutionEvents(List<MatchEvent> matchEvents) {
        List<MatchEvent> newMatchEventList = new ArrayList<>(matchEvents);
        for (MatchEvent matchEvent : matchEvents) {
            if (!Objects.equals(matchEvent.getEventType(), SUBSTITUTION)) {
                newMatchEventList.remove(matchEvent);
            }
        }
        return newMatchEventList;
    }

    public static List<FootballFeed> getStaticFeedItems() {
        List<FootballFeed> footballFeeds = new ArrayList<>();
        footballFeeds.add(new FootballFeed(
                "Premier League",
                new Thumbnail("https://cdn.pulselive.com/test/client/pl/dev/i/elements/premier-league-logo-header.png", 0, 0),
                "2018/2019",
                "England"
        ));
        footballFeeds.add(new FootballFeed(
                "La Liga",
                new Thumbnail("https://cdn.bleacherreport.net/images/team_logos/328x328/la_liga.png", 0, 0),
                "2018/2019",
                "Spain"
        ));
        footballFeeds.add(new FootballFeed(
                "Bundesliga",
                new Thumbnail("http://logok.org/wp-content/uploads/2014/12/Bundesliga-logo-880x655.png", 0, 0),
                "2018/2019",
                "Germany"
        ));
        footballFeeds.add(new FootballFeed(
                "Champions League",
                new Thumbnail("https://www.seeklogo.net/wp-content/uploads/2013/06/uefa-champions-league-eps-vector-logo.png", 0, 0),
                "2018/2019",
                "Europe"
        ));
/*        TODO : add this one after it is done in backend.
        footballFeeds.add(new FootballFeed(
                "EFL",
                new Thumbnail("https://cdn.pulselive.com/test/client/pl/dev/i/elements/premier-league-logo-header.png", 0, 0),
                "2018/2019",
                "England"
        ));*/
        footballFeeds.add(new FootballFeed(
                "Serie A",
                new Thumbnail("http://www.tvsette.net/wp-content/uploads/2017/06/SERIE-A-LOGO.png", 0, 0),
                "2018/2019",
                "Italy"
        ));
        footballFeeds.add(new FootballFeed(
                "Ligue 1",
                new Thumbnail("http://logok.org/wp-content/uploads/2014/11/Ligue-1-logo-france-880x660.png", 0, 0),
                "2018/2019",
                "France"
        ));
        footballFeeds.add(new FootballFeed(
                "FA Cup",
                new Thumbnail("https://vignette.wikia.nocookie.net/logopedia/images/3/33/The_Emirates_FA_Cup.png", 0, 0),
                "2018/2019",
                "England"
        ));
        footballFeeds.add(new FootballFeed(
                "Copa Del Rey",
                new Thumbnail("https://www.primeradivision.pl/luba/dane/pliki/bank_zdj/duzy/copadelrey.jpg", 0, 0),
                "2018/2019",
                "Spain"
        ));
        footballFeeds.add(new FootballFeed(
                "Coppa Italia",
                new Thumbnail("https://cdn.ghanasoccernet.com/2018/07/5b3f92288827c.jpg", 0, 0),
                "2018/2019",
                "Italy"
        ));
        footballFeeds.add(new FootballFeed(
                "Europa League",
                new Thumbnail("https://cdn.foxsports.com.br/sites/foxsports-br/files/img/competition/shields-original/logo-uefa-europa-league.png", 0, 0),
                "2018/2019",
                "Europe"
        ));
        return footballFeeds;
    }
}