package life.plank.juna.zone.util;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.ScrubberData;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.data.network.model.ZoneLiveData;

import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;
import static life.plank.juna.zone.util.AppConstants.DASH;
import static life.plank.juna.zone.util.AppConstants.FOUL;
import static life.plank.juna.zone.util.AppConstants.FT;
import static life.plank.juna.zone.util.AppConstants.FULL_TIME_LOWERCASE;
import static life.plank.juna.zone.util.AppConstants.GOAL;
import static life.plank.juna.zone.util.AppConstants.HALF_TIME_LOWERCASE;
import static life.plank.juna.zone.util.AppConstants.HT;
import static life.plank.juna.zone.util.AppConstants.LIVE;
import static life.plank.juna.zone.util.AppConstants.RED_CARD;
import static life.plank.juna.zone.util.AppConstants.SPACE;
import static life.plank.juna.zone.util.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.AppConstants.WIDE_DASH;
import static life.plank.juna.zone.util.AppConstants.WIDE_SPACE;
import static life.plank.juna.zone.util.AppConstants.YELLOW_CARD;
import static life.plank.juna.zone.util.AppConstants.YELLOW_RED;
import static life.plank.juna.zone.util.DateUtil.getDateDiffFromToday;
import static life.plank.juna.zone.util.DateUtil.getDateForScrubber;
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

    public static String getSeparator(MatchFixture matchFixture, ImageView winPointer) {
        winPointer.setVisibility(View.INVISIBLE);
        int dateDiff = getDateDiffFromToday(matchFixture.getMatchStartTime());
        switch (dateDiff) {
            case -1:
                return ZoneApplication.getContext().getString(R.string.yesterday);
            case 0:
                if (getTimeDiffFromNow(matchFixture.getMatchStartTime()) < 0) {
                    return matchFixture.getHomeGoals() + WIDE_DASH + matchFixture.getAwayGoals();
                } else {
                    return getFutureMatchTime(matchFixture.getMatchStartTime());
                }
            case 1:
                return ZoneApplication.getContext().getString(R.string.tomorrow);
            default:
                if (dateDiff < -1) {
                    return getPastMatchSeparator(matchFixture, winPointer, false);
                } else {
                    return getFutureMatchTime(matchFixture.getMatchStartTime());
                }
        }
    }

    //    TODO : merge this method with the above one in next pull request
    public static String getBoardSeparator(MatchFixture matchFixture, ImageView winPointer) {
        winPointer.setVisibility(View.INVISIBLE);
        int dateDiff = getDateDiffFromToday(matchFixture.getMatchStartTime());
        switch (dateDiff) {
            case -1:
                return ZoneApplication.getContext().getString(R.string.yesterday);
            case 0:
                if (getTimeDiffFromNow(matchFixture.getMatchStartTime()) < 0) {
                    return matchFixture.getHomeGoals() + DASH + matchFixture.getAwayGoals();
                } else {
                    return getFutureMatchTime(matchFixture.getMatchStartTime());
                }
            case 1:
                return ZoneApplication.getContext().getString(R.string.tomorrow);
            default:
                if (dateDiff < -1) {
                    return getPastMatchSeparator(matchFixture, winPointer, true);
                } else {
                    return getFutureMatchTime(matchFixture.getMatchStartTime());
                }
        }
    }

    private static String getPastMatchSeparator(MatchFixture matchFixture, ImageView winPointer, boolean isBoard) {
        String teamNameSeparator;
        int homeWinDrawable = isBoard ?
                R.drawable.ic_win_home_light :
                R.drawable.ic_win_home_dark;
        int visitingWinDrawable = isBoard ?
                R.drawable.ic_win_away_light :
                R.drawable.ic_win_away_dark;
        if (matchFixture.getAwayTeamPenaltyScore() == 0 && matchFixture.getHomeTeamPenaltyScore() == 0) {
            if (matchFixture.getHomeGoals() > matchFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = matchFixture.getHomeGoals() + (isBoard ? SPACE : WIDE_SPACE) +
                        matchFixture.getAwayGoals();
            } else if (matchFixture.getAwayGoals() > matchFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = matchFixture.getHomeGoals() + (isBoard ? SPACE : WIDE_SPACE) +
                        matchFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = matchFixture.getHomeGoals() + (isBoard ? DASH : WIDE_DASH) +
                        matchFixture.getAwayGoals();
            }
        } else {
            if (matchFixture.getHomeGoals() > matchFixture.getAwayGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(homeWinDrawable);
                teamNameSeparator = matchFixture.getHomeGoals() +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" + (isBoard ? SPACE : WIDE_SPACE) +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" +
                        matchFixture.getAwayGoals();
            } else if (matchFixture.getAwayGoals() > matchFixture.getHomeGoals()) {
                winPointer.setVisibility(View.VISIBLE);
                winPointer.setImageResource(visitingWinDrawable);
                teamNameSeparator = matchFixture.getHomeGoals() +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" + (isBoard ? SPACE : WIDE_SPACE) +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" +
                        matchFixture.getAwayGoals();
            } else {
                winPointer.setVisibility(View.INVISIBLE);
                teamNameSeparator = matchFixture.getHomeGoals() +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" + (isBoard ? DASH : WIDE_DASH) +
                        "(" + matchFixture.getHomeTeamPenaltyScore() + ")" +
                        matchFixture.getAwayGoals();
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

    public static List<FootballFeed> getStaticFeedItems() {
        List<FootballFeed> footballFeeds = new ArrayList<>();
        footballFeeds.add(new FootballFeed(
                "Premier League",
                new Thumbnail("https://image.ibb.co/msPsep/img_epl_logo.png", 0, 0),
                "2018/2019",
                "England",
                false
        ));
        footballFeeds.add(new FootballFeed(
                "La Liga",
                new Thumbnail("https://cdn.bleacherreport.net/images/team_logos/328x328/la_liga.png", 0, 0),
                "2018/2019",
                "Spain",
                false
        ));
        footballFeeds.add(new FootballFeed(
                "Bundesliga",
                new Thumbnail("http://logok.org/wp-content/uploads/2014/12/Bundesliga-logo-880x655.png", 0, 0),
                "2018/2019",
                "Germany",
                false
        ));
        footballFeeds.add(new FootballFeed(
                "Champions League",
                new Thumbnail("https://www.seeklogo.net/wp-content/uploads/2013/06/uefa-champions-league-eps-vector-logo.png", 0, 0),
                "2018/2019",
                "Europe",
                false
        ));
/*        TODO : add this one after it is done in backend.
        footballFeeds.add(new FootballFeed(
                "EFL",
                new Thumbnail("https://cdn.pulselive.com/test/client/pl/dev/i/elements/premier-league-logo-header.png", 0, 0),
                "2018/2019",
                "England",
                false
        ));*/
        footballFeeds.add(new FootballFeed(
                "Serie A",
                new Thumbnail("http://www.tvsette.net/wp-content/uploads/2017/06/SERIE-A-LOGO.png", 0, 0),
                "2018/2019",
                "Italy",
                false
        ));
        footballFeeds.add(new FootballFeed(
                "Ligue 1",
                new Thumbnail("http://logok.org/wp-content/uploads/2014/11/Ligue-1-logo-france-880x660.png", 0, 0),
                "2018/2019",
                "France",
                false
        ));
        footballFeeds.add(new FootballFeed(
                "FA Cup",
                new Thumbnail("https://vignette.wikia.nocookie.net/logopedia/images/3/33/The_Emirates_FA_Cup.png", 0, 0),
                "2018/2019",
                "England",
                true
        ));
        footballFeeds.add(new FootballFeed(
                "Copa Del Rey",
                new Thumbnail("https://www.primeradivision.pl/luba/dane/pliki/bank_zdj/duzy/copadelrey.jpg", 0, 0),
                "2018/2019",
                "Spain",
                true
        ));
        footballFeeds.add(new FootballFeed(
                "Coppa Italia",
                new Thumbnail("https://cdn.ghanasoccernet.com/2018/07/5b3f92288827c.jpg", 0, 0),
                "2018/2019",
                "Italy",
                true
        ));
        footballFeeds.add(new FootballFeed(
                "Europa League",
                new Thumbnail("https://cdn.foxsports.com.br/sites/foxsports-br/files/img/competition/shields-original/logo-uefa-europa-league.png", 0, 0),
                "2018/2019",
                "Europe",
                false
        ));
        return footballFeeds;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (target != null && !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

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

    //region Dummy Data for Scrubber. TODO : remove this region after getting the data from backend.
    static List<ScrubberData> getDefinedDummyScrubberData() {
        List<ScrubberData> scrubberDataList = new ArrayList<>();
        scrubberDataList.add(new ScrubberData(0, 18, LIVE, false));
        scrubberDataList.add(new ScrubberData(2, 48, FOUL, true));
        scrubberDataList.add(new ScrubberData(4, 21, GOAL, true));
        scrubberDataList.add(new ScrubberData(6, 9, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(8, 28, FOUL, true));
        scrubberDataList.add(new ScrubberData(12, 4, YELLOW_CARD, false));
        scrubberDataList.add(new ScrubberData(15, 17, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(18, 13, FOUL, true));
        scrubberDataList.add(new ScrubberData(20, 34, YELLOW_CARD, true));
        scrubberDataList.add(new ScrubberData(21, 25, GOAL, false));
        scrubberDataList.add(new ScrubberData(22, 15, FOUL, true));
        scrubberDataList.add(new ScrubberData(24, 20, FOUL, true));
        scrubberDataList.add(new ScrubberData(28, 18, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(32, 8, FOUL, true));
        scrubberDataList.add(new ScrubberData(38, 34, RED_CARD, true));
        scrubberDataList.add(new ScrubberData(42, 3, FOUL, true));
        scrubberDataList.add(new ScrubberData(45, 21, HT, true));
        scrubberDataList.add(new ScrubberData(46, 9, FOUL, true));
        scrubberDataList.add(new ScrubberData(48, 40, GOAL, true));
        scrubberDataList.add(new ScrubberData(52, 42, FOUL, true));
        scrubberDataList.add(new ScrubberData(53, 45, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(54, 38, SUBSTITUTION, false));
        scrubberDataList.add(new ScrubberData(58, 20, FOUL, true));
        scrubberDataList.add(new ScrubberData(62, 47, GOAL, true));
        scrubberDataList.add(new ScrubberData(64, 11, FOUL, true));
        scrubberDataList.add(new ScrubberData(66, 31, RED_CARD, false));
        scrubberDataList.add(new ScrubberData(71, 14, FOUL, true));
        scrubberDataList.add(new ScrubberData(77, 42, SUBSTITUTION, true));
        scrubberDataList.add(new ScrubberData(82, 21, FOUL, true));
        scrubberDataList.add(new ScrubberData(85, 15, GOAL, true));
        scrubberDataList.add(new ScrubberData(89, 30, FOUL, true));
        scrubberDataList.add(new ScrubberData(90, 35, YELLOW_RED, true));
        scrubberDataList.add(new ScrubberData(92, 32, FOUL, true));
        scrubberDataList.add(new ScrubberData(94, 47, FT, true));
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
                    scrubberData.getXValue(),
                    scrubberData.getYValue(),
                    getSuitableScrubberIcon(scrubberData.getEventType(), scrubberData.isHomeTeam())
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
    static void prepareScrubber(LineChart lineChart) {
        lineChart.setPinchZoom(true);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter((value, axis) -> getDateForScrubber((long) value));
    }

    /**
     * TODO: replace dummy List calls with list obtained from API call.
     */
    public static class ScrubberLoader extends AsyncTask<Void, Void, LineData> {

        private WeakReference<LineChart> lineChartRef;
        private boolean isRandom;

        private ScrubberLoader(LineChart lineChart, boolean isRandom) {
            this.lineChartRef = new WeakReference<>(lineChart);
            this.isRandom = isRandom;
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
            lineDataSets1.add(getLineDataSet(isRandom ? getRandomDummyScrubberData() : getDefinedDummyScrubberData()));
            return new LineData(lineDataSets1);
        }

        @Override
        protected void onPostExecute(LineData lineData) {
            lineChartRef.get().setData(lineData);
            lineChartRef.get().invalidate();
        }
    }

    public static String getDisplayTimeStatus(String apiTimeStatus) {
        switch (apiTimeStatus) {
            case HT:
                return HALF_TIME_LOWERCASE;
            case FT:
                return FULL_TIME_LOWERCASE;
            default:
                return apiTimeStatus;
        }
    }
}