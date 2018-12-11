package life.plank.juna.zone.util.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.Formation;
import life.plank.juna.zone.data.model.FormationList;
import life.plank.juna.zone.data.model.League;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.ScrubberData;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.util.football.ScoreBuilder;

import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;
import static life.plank.juna.zone.util.common.AppConstants.DASH;
import static life.plank.juna.zone.util.common.AppConstants.FOUL;
import static life.plank.juna.zone.util.common.AppConstants.FT;
import static life.plank.juna.zone.util.common.AppConstants.FULL_TIME_LOWERCASE;
import static life.plank.juna.zone.util.common.AppConstants.GOAL;
import static life.plank.juna.zone.util.common.AppConstants.HALF_TIME_LOWERCASE;
import static life.plank.juna.zone.util.common.AppConstants.HT;
import static life.plank.juna.zone.util.common.AppConstants.LIVE;
import static life.plank.juna.zone.util.common.AppConstants.NOT_STARTED;
import static life.plank.juna.zone.util.common.AppConstants.NS;
import static life.plank.juna.zone.util.common.AppConstants.RED_CARD;
import static life.plank.juna.zone.util.common.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.common.AppConstants.WIDE_DASH;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_CARD;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_RED;
import static life.plank.juna.zone.util.time.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.time.DateUtil.getDateForScrubber;
import static life.plank.juna.zone.util.time.DateUtil.getFutureMatchTime;
import static life.plank.juna.zone.util.time.DateUtil.getTimeDiffFromNow;

public class DataUtil {

    private static final String[] dummyEvents = new String[]{
            GOAL,
            SUBSTITUTION,
            YELLOW_CARD,
            RED_CARD,
            YELLOW_RED,
            FOUL, FOUL, FOUL, FOUL,
            FOUL, FOUL, FOUL, FOUL,
            FOUL, FOUL, FOUL, FOUL,
            FOUL, FOUL, FOUL, FOUL,
            FOUL, FOUL, FOUL, FOUL,
            FOUL, FOUL, FOUL, FOUL
    };
    private static Random random = new Random();

    public static String findString(@StringRes int stringRes) {
        return ZoneApplication.getContext().getString(stringRes);
    }

    public static CharSequence findString(@StringRes int stringRes, Object... formatArgs) {
        return ZoneApplication.getContext().getString(stringRes, formatArgs);
    }

    public static Integer findInt(@IntegerRes int integerRes) {
        return ZoneApplication.getContext().getResources().getInteger(integerRes);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(Object s) {
        return s == null;
    }

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null;
    }

    public static String formatInt(int i) {
        return String.valueOf(i > 9 ? i : "0" + i);
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || c.isEmpty();
    }

    public static boolean equalsNullString(String s) {
        return Objects.equals(s, "null");
    }

    public static String getSeparator(MatchDetails matchDetails, ImageView winPointer, boolean isBoard) {
        winPointer.setVisibility(View.INVISIBLE);
        int dateDiff = getDateDiffFromToday(matchDetails.getMatchStartTime());
        switch (dateDiff) {
            case -1:
                return getPastMatchSeparator(matchDetails, winPointer, isBoard);
            case 0:
                if (getTimeDiffFromNow(matchDetails.getMatchStartTime()) <= 0) {
                    return matchDetails.getHomeGoals() + (isBoard ? DASH : WIDE_DASH) + matchDetails.getAwayGoals();
                } else {
                    return getFutureMatchTime(matchDetails.getMatchStartTime());
                }
            case 1:
                return getFutureMatchTime(matchDetails.getMatchStartTime());
            default:
                if (dateDiff < -1) {
                    return getPastMatchSeparator(matchDetails, winPointer, isBoard);
                } else {
                    return getFutureMatchTime(matchDetails.getMatchStartTime());
                }
        }
    }

    private static String getPastMatchSeparator(MatchDetails matchDetails, ImageView winPointer, boolean isBoard) {
        String teamNameSeparator;
        int homeWinDrawable = isBoard ?
                R.drawable.ic_win_home_light :
                R.drawable.ic_win_home_dark;
        int visitingWinDrawable = isBoard ?
                R.drawable.ic_win_away_light :
                R.drawable.ic_win_away_dark;
        if (matchDetails.getAwayTeamPenaltyScore() == 0 && matchDetails.getHomeTeamPenaltyScore() == 0) {
            if (matchDetails.getHomeGoals() > matchDetails.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            } else if (matchDetails.getAwayGoals() > matchDetails.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = ScoreBuilder.getTiedScore(matchDetails.getHomeGoals(), matchDetails.getAwayGoals(), isBoard);
            }
        } else {
            if (matchDetails.getHomeGoals() > matchDetails.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            } else if (matchDetails.getAwayGoals() > matchDetails.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = ScoreBuilder.getWinPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = ScoreBuilder.getTiedPenaltyScore(matchDetails.getHomeGoals(), matchDetails.getHomeTeamPenaltyScore(),
                        matchDetails.getAwayGoals(), matchDetails.getAwayTeamPenaltyScore(), isBoard);
            }
        }
        return teamNameSeparator;
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

    public static List<League> getStaticLeagues() {
        List<League> footballFeeds = new ArrayList<>();
        footballFeeds.add(new League(
                8,
                "Premier League",
                false,
                "2018/2019",
                "England",
                "https://image.ibb.co/msPsep/img_epl_logo.png",
                R.color.black_currant,
                R.drawable.img_epl_logo
        ));
        footballFeeds.add(new League(
                564,
                "La Liga",
                false,
                "2018/2019",
                "Spain",
                "https://cdn.bleacherreport.net/images/team_logos/328x328/la_liga.png",
                R.color.eclipse,
                R.drawable.img_laliga_logo
        ));
        footballFeeds.add(new League(
                82,
                "Bundesliga",
                false,
                "2018/2019",
                "Germany",
                "http://logok.org/wp-content/uploads/2014/12/Bundesliga-logo-880x655.png",
                R.color.sangria,
                R.drawable.img_bundesliga_logo
        ));
        footballFeeds.add(new League(
                2,
                "Champions League",
                false,
                "2018/2019",
                "Europe",
                "https://www.seeklogo.net/wp-content/uploads/2013/06/uefa-champions-league-eps-vector-logo.png",
                R.color.maire,
                R.drawable.img_champions_league_logo
        ));
        footballFeeds.add(new League(
                384,
                "Serie A",
                false,
                "2018/2019",
                "Italy",
                "http://www.tvsette.net/wp-content/uploads/2017/06/SERIE-A-LOGO.png",
                R.color.crusoe,
                R.drawable.img_serie_a_logo
        ));
        footballFeeds.add(new League(
                301,
                "Ligue 1",
                false,
                "2018/2019",
                "France",
                "http://logok.org/wp-content/uploads/2014/11/Ligue-1-logo-france-880x660.png",
                R.color.shuttle_grey,
                R.drawable.img_ligue_1_logo
        ));
        footballFeeds.add(new League(
                24,
                "FA Cup",
                true,
                "2018/2019",
                "England",
                "https://vignette.wikia.nocookie.net/logopedia/images/3/33/The_Emirates_FA_Cup.png",
                R.color.sapphire,
                R.drawable.img_fa_cup_logo
        ));
        footballFeeds.add(new League(
                570,
                "Copa Del Rey",
                true,
                "2018/2019",
                "Spain",
                "https://www.primeradivision.pl/luba/dane/pliki/bank_zdj/duzy/copadelrey.jpg",
                R.color.carmine,
                R.drawable.img_delrey_logo
        ));
        footballFeeds.add(new League(
                390,
                "Coppa Italia",
                true,
                "2018/2019",
                "Italy",
                "https://cdn.ghanasoccernet.com/2018/07/5b3f92288827c.jpg",
                R.color.husk,
                R.drawable.img_coppa_italia_logo
        ));
        footballFeeds.add(new League(
                5,
                "Europa League",
                false,
                "2018/2019",
                "Europe",
                "https://cdn.foxsports.com.br/sites/foxsports-br/files/img/competition/shields-original/logo-uefa-europa-league.png",
                R.color.carrot_orange,
                R.drawable.img_europa_logo
        ));
        return footballFeeds;
    }

    public static League getSpecifiedLeague(String leagueName) {
        if (isNullOrEmpty(leagueName)) return null;

        List<League> leagues = getStaticLeagues();
        for (League league : leagues) {
            if (Objects.equals(league.getName(), leagueName) || league.getName().equalsIgnoreCase(leagueName)) {
                return league;
            }
        }
        return null;
    }

    public static GradientDrawable getLeagueBackground(@ColorRes int leagueColor) {
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
                ColorUtils.compositeColors(ZoneApplication.getContext().getColor(R.color.color_league_gradient), ZoneApplication.getContext().getColor(leagueColor)),
                ZoneApplication.getContext().getColor(leagueColor)
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (target != null && !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //region Dummy Data for Scrubber. TODO : remove this region after getting the data from backend.
    private static List<ScrubberData> getDefinedDummyScrubberData() {
        List<ScrubberData> scrubberDataList = new ArrayList<>();
        scrubberDataList.add(new ScrubberData(0, 18, LIVE, false));
        scrubberDataList.add(new ScrubberData(2, 22, FOUL, true));
        scrubberDataList.add(new ScrubberData(4, 26, GOAL, true));
        scrubberDataList.add(new ScrubberData(6, 28, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(8, 30, FOUL, true));
        scrubberDataList.add(new ScrubberData(12, 32, YELLOW_CARD, false));
        scrubberDataList.add(new ScrubberData(15, 34, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(18, 38, FOUL, true));
        scrubberDataList.add(new ScrubberData(20, 44, YELLOW_CARD, true));
        scrubberDataList.add(new ScrubberData(21, 48, GOAL, false));
        scrubberDataList.add(new ScrubberData(22, 46, FOUL, true));
        scrubberDataList.add(new ScrubberData(24, 42, FOUL, true));
        scrubberDataList.add(new ScrubberData(28, 36, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(32, 32, FOUL, true));
        scrubberDataList.add(new ScrubberData(38, 34, RED_CARD, true));
        scrubberDataList.add(new ScrubberData(42, 30, FOUL, true));
        scrubberDataList.add(new ScrubberData(45, 34, HT, true));
        scrubberDataList.add(new ScrubberData(46, 38, FOUL, true));
        scrubberDataList.add(new ScrubberData(48, 44, GOAL, true));
        scrubberDataList.add(new ScrubberData(52, 42, FOUL, true));
        scrubberDataList.add(new ScrubberData(53, 36, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(54, 34, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(58, 38, FOUL, true));
        scrubberDataList.add(new ScrubberData(62, 44, GOAL, true));
        scrubberDataList.add(new ScrubberData(64, 40, FOUL, true));
        scrubberDataList.add(new ScrubberData(66, 38, RED_CARD, false));
        scrubberDataList.add(new ScrubberData(71, 36, FOUL, true));
        scrubberDataList.add(new ScrubberData(77, 32, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(82, 26, FOUL, true));
        scrubberDataList.add(new ScrubberData(85, 34, GOAL, true));
        scrubberDataList.add(new ScrubberData(89, 36, FOUL, true));
        scrubberDataList.add(new ScrubberData(90, 40, YELLOW_RED, true));
        scrubberDataList.add(new ScrubberData(92, 46, FOUL, true));
        scrubberDataList.add(new ScrubberData(94, 48, FT, true));
        return scrubberDataList;
    }

    static List<ScrubberData> getRandomDummyScrubberData() {
        List<ScrubberData> scrubberDataList = new ArrayList<>();
        for (int i = 0; i < 100; i += 2) {
            switch (i) {
                case 0:
                    scrubberDataList.add(new ScrubberData(0, getRandomInteraction(), LIVE, false));
                    break;
                case 40:
                    scrubberDataList.add(new ScrubberData(40, getRandomInteraction(), HT, true));
                    break;
                case 98:
                    scrubberDataList.add(new ScrubberData(98, getRandomInteraction(), FT, true));
                    break;
                default:
                    scrubberDataList.add(new ScrubberData(i, getRandomInteraction(), getRandomEvent(), getRandomBoolean()));
                    break;
            }
        }
        return scrubberDataList;
    }

    private static String getRandomEvent() {
        return dummyEvents[random.nextInt(dummyEvents.length - 1)];
    }

    private static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    private static int getRandomInteraction() {
        return random.nextInt(50);
    }
    //endregion

    /**
     * Method for converting the {@link List<ScrubberData>} list to the {@link LineChart} compatible, customized {@link LineDataSet}.
     *
     * @param scrubberDataList the {@link List<ScrubberData>} list
     * @return the {@link LineDataSet} object for use by the lineChart.
     */
    static LineDataSet getLineDataSet(List<ScrubberData> scrubberDataList) {
        List<Entry> entries = new ArrayList<>();
        for (ScrubberData scrubberData : scrubberDataList) {
            entries.add(new Entry(
                    scrubberData.getMillisecondsX(),
                    scrubberData.getInteractionY(),
                    getSuitableScrubberIcon(scrubberData.getEvent().getEventType(), scrubberData.getEvent().isHomeTeam())
            ));
        }
        LineDataSet dataSet = new LineDataSet(entries, ZoneApplication.getContext().getString(R.string.scrubber));
        dataSet.setDrawIcons(true);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(ZoneApplication.getContext().getColor(R.color.mainGradientEnd));
        dataSet.setLineWidth(1f);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(new GradientDrawable(TOP_BOTTOM, new int[]{
                ZoneApplication.getContext().getColor(R.color.mainGradientTranslucentStart),
                ZoneApplication.getContext().getColor(R.color.mainGradientTranslucentEnd)
        }));
        return dataSet;
    }

    /**
     * Method for getting icons for scrubber events.<br/>
     * <b>Note</b> : Icons will only be shown for <b>YELLOW_CARD, RED_CARD, YELLOW_RED, GOAL, SUBSTITUTION, LIVE, HT and FT events.</b>
     */
    private static Drawable getSuitableScrubberIcon(String eventType, boolean isHomeTeam) {
        @DrawableRes int drawableIcon;
        switch (eventType) {
            case YELLOW_CARD:
                drawableIcon = isHomeTeam ?
                        R.drawable.yellow_left :
                        R.drawable.yellow_right;
                break;
            case RED_CARD:
                drawableIcon = isHomeTeam ?
                        R.drawable.red_left :
                        R.drawable.red_right;
                break;
            case YELLOW_RED:
                drawableIcon = R.drawable.yellow_red;
                break;
            case GOAL:
                drawableIcon = isHomeTeam ?
                        R.drawable.ic_goal_left :
                        R.drawable.ic_goal_right;
                break;
            case SUBSTITUTION:
                drawableIcon = isHomeTeam ?
                        R.drawable.ic_sub_left :
                        R.drawable.ic_sub_right;
                break;
            case LIVE:
            case HT:
            case FT:
                drawableIcon = R.drawable.ic_whistle;
                break;
            default:
                drawableIcon = 0;
                break;
        }
        return drawableIcon != 0 ? ZoneApplication.getContext().getResources().getDrawable(drawableIcon, null) : null;
    }

    /**
     * Customizing the {@link LineChart} instance according to our needs.
     *
     * @param lineChart the {@link LineChart} instance to customize.
     */
    private static void prepareScrubber(LineChart lineChart) {
        lineChart.setPinchZoom(true);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter((value, axis) -> getDateForScrubber((long) value));
    }

    public static String getDisplayTimeStatus(String apiTimeStatus) {
        switch (apiTimeStatus) {
            case HT:
                return HALF_TIME_LOWERCASE;
            case FT:
                return FULL_TIME_LOWERCASE;
            case NS:
                return NOT_STARTED;
            default:
                return apiTimeStatus;
        }
    }

    public static void pinFeedEntry(List<FeedEntry> feedEntryList, FeedEntry feedEntryToPin) {
        int index = feedEntryList.indexOf(feedEntryToPin);
        if (index >= 0) {
            feedEntryList.remove(feedEntryToPin);
            feedEntryList.add(0, feedEntryToPin);
        }
    }

    public static void unpinFeedEntry(List<FeedEntry> feedEntryList, FeedEntry feedEntryToUnpin) {
        int previousPosition = feedEntryToUnpin.getFeedInteractions().getPreviousPosition();
        if (previousPosition >= 0) {
            feedEntryList.remove(feedEntryToUnpin);
            feedEntryList.add(previousPosition, feedEntryToUnpin);
        }
    }

    public static void updateScoreLocally(MatchFixture fixture, LiveScoreData scoreData) {
        fixture.setHomeGoals(scoreData.getHomeGoals());
        fixture.setAwayGoals(scoreData.getAwayGoals());
        fixture.setHomeTeamPenaltyScore(scoreData.getHomeTeamPenaltyScore());
        fixture.setAwayTeamPenaltyScore(scoreData.getAwayTeamPenaltyScore());
    }

    public static void updateScoreLocally(MatchDetails matchDetails, LiveScoreData scoreData) {
        matchDetails.setHomeGoals(scoreData.getHomeGoals());
        matchDetails.setAwayGoals(scoreData.getAwayGoals());
        matchDetails.setHomeTeamPenaltyScore(scoreData.getHomeTeamPenaltyScore());
        matchDetails.setAwayTeamPenaltyScore(scoreData.getAwayTeamPenaltyScore());
    }

    public static void updateTimeStatusLocally(MatchFixture fixture, LiveTimeStatus timeStatus) {
        fixture.setTimeStatus(timeStatus.getTimeStatus());
        fixture.setMinute(timeStatus.getMinute());
        fixture.setExtraMinute(timeStatus.getExtraMinute());
    }

    public static void updateTimeStatusLocally(MatchDetails matchDetails, LiveTimeStatus timeStatus) {
        matchDetails.setTimeStatus(timeStatus.getTimeStatus());
        matchDetails.setMinute(timeStatus.getMinute());
        matchDetails.setExtraMinute(timeStatus.getExtraMinute());
    }

    public static Lineups getIntegratedLineups(Lineups lineups, List<MatchEvent> matchEvents) {
        for (MatchEvent matchEvent : matchEvents) {
            if (matchEvent.isHomeTeam()) {
                integrateLineupsWithMatchEvents(lineups.getHomeTeamFormation(), matchEvent);
            } else {
                integrateLineupsWithMatchEvents(lineups.getAwayTeamFormation(), matchEvent);
            }
        }
        return lineups;
    }

    private static void integrateLineupsWithMatchEvents(List<FormationList> allFormationList, MatchEvent matchEvent) {
        for (List<Formation> formationList : allFormationList) {
            for (Formation formation : formationList) {
                if (Objects.equals(formation.getNickname(), matchEvent.getPlayerName())) {
                    switch (matchEvent.getEventType()) {
                        case GOAL:
                            formation.setGoals(formation.getGoals() + 1);
                            break;
                        case YELLOW_CARD:
                            formation.setYellowCards(1);
                            break;
                        case RED_CARD:
                            formation.setRedCards(1);
                            break;
                        case YELLOW_RED:
                            formation.setYellowRed(1);
                            break;
                        case SUBSTITUTION:
                            formation.setSubstituteOut(1);
                            break;
                    }
                }
            }
        }
    }

    public static <T> void validateAndUpdateList(List<T> originalList, List<T> newList, boolean isError) {
        if (!isError) {
            if (originalList == null) {
                originalList = new ArrayList<>();
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList);
            }
        }
    }

    public static class ScrubberLoader extends AsyncTask<Void, Void, LineData> {

        private WeakReference<LineChart> lineChartRef;
        private List<ScrubberData> scrubberDataList;
        private boolean isRandom;

        private ScrubberLoader(LineChart lineChart, List<ScrubberData> scrubberDataList) {
            this.lineChartRef = new WeakReference<>(lineChart);
            this.scrubberDataList = scrubberDataList;
        }

        //        TODO: remove below two methods once Scrubber API integration completes
        private ScrubberLoader(LineChart lineChart, boolean isRandom) {
            this.lineChartRef = new WeakReference<>(lineChart);
            this.isRandom = isRandom;
        }

        public static void prepare(LineChart lineChart, List<ScrubberData> scrubberDataList) {
            new ScrubberLoader(lineChart, scrubberDataList).execute();
        }

        public static void prepare(LineChart lineChart, boolean isRandom) {
            new ScrubberLoader(lineChart, isRandom).execute();
        }

        @Override
        protected void onPreExecute() {
            prepareScrubber(lineChartRef.get());
        }

        @Override
        protected LineData doInBackground(Void... voids) {
            List<ILineDataSet> lineDataSets1 = new ArrayList<>();
            lineDataSets1.add(getLineDataSet(
                    isNullOrEmpty(scrubberDataList) ?
                            isRandom ?
                                    getRandomDummyScrubberData() :
                                    getDefinedDummyScrubberData() :
                            scrubberDataList));
            return new LineData(lineDataSets1);
        }

        @Override
        protected void onPostExecute(LineData lineData) {
            lineChartRef.get().setData(lineData);
            lineChartRef.get().invalidate();
        }
    }
}