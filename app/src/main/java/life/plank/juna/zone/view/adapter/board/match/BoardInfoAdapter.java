package life.plank.juna.zone.view.adapter.board.match;

import android.app.Activity;
import android.view.View;

import com.ahamed.multiviewadapter.DataItemManager;
import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.model.Commentary;
import life.plank.juna.zone.data.model.Highlights;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.MatchStats;
import life.plank.juna.zone.data.model.ScrubberData;
import life.plank.juna.zone.data.model.binder.CommentaryBindingModel;
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel;
import life.plank.juna.zone.data.model.binder.LineupsBindingModel;
import life.plank.juna.zone.data.model.binder.MatchStatsBindingModel;
import life.plank.juna.zone.data.model.binder.ScrubberBindingModel;
import life.plank.juna.zone.data.model.binder.StandingsBindingModel;
import life.plank.juna.zone.data.model.binder.SubstitutionBindingModel;
import life.plank.juna.zone.data.model.binder.TeamStatsBindingModel;
import life.plank.juna.zone.util.AppConstants.MatchTimeVal;
import life.plank.juna.zone.view.adapter.board.match.binder.BenchDataBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.CommentaryBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.LineupsBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.MatchHighlightsBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.MatchStatsBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.ScheduledMatchFooterBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.ScrubberBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.StandingsBinder;
import life.plank.juna.zone.view.adapter.board.match.binder.TeamStatsBinder;

import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START_BOARD_ACTIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_PAST;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_LATER;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_TODAY;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getMatchTimeValue;

public class BoardInfoAdapter extends RecyclerAdapter {

    private RequestManager glide;
    private MatchDetails matchDetails;
    private Activity activity;
    private BoardInfoAdapterListener listener;
    private Boolean isLineupFragment;

    private DataItemManager<ScrubberBindingModel> scrubberDataManager;
    private DataItemManager<HighlightsBindingModel> highlightsDataManager;
    private DataItemManager<CommentaryBindingModel> commentaryDataManager;
    private DataItemManager<MatchStatsBindingModel> matchStatsDataManager;
    private DataItemManager<LineupsBindingModel> lineupsDataManager;
    private DataItemManager<SubstitutionBindingModel> substitutionDataManager;
    private DataItemManager<StandingsBindingModel> standingsDataManager;
    private DataItemManager<TeamStatsBindingModel> teamStatsDataManager;

    public BoardInfoAdapter(MatchDetails matchDetails, RequestManager glide, Activity activity, BoardInfoAdapterListener listener, Boolean isLineupFragment) {
        this.matchDetails = matchDetails;
        this.glide = glide;
        this.activity = activity;
        this.listener = listener;
        this.isLineupFragment = isLineupFragment;

        @MatchTimeVal int matchTimeValue = getMatchTimeValue(matchDetails.getMatchStartTime(), true);
        switch (matchTimeValue) {
            case MATCH_PAST:
            case MATCH_COMPLETED_TODAY:
            case MATCH_LIVE:
                preparePastOrLiveMatchAdapter();
                break;
            case MATCH_ABOUT_TO_START:
                prepareRecentMatchAdapter();
                break;
            case MATCH_ABOUT_TO_START_BOARD_ACTIVE:
                prepareRecentMatchAdapterWhenBoardIsActive();
                break;
            case MATCH_SCHEDULED_TODAY:
            case MATCH_SCHEDULED_LATER:
                prepareScheduledMatchAdapter();
                break;
        }
    }

    /**
     * Method for populating components of a past or live match
     * <br/>Consists of:
     * <br/>Scrubber, from {@link ScrubberBinder}
     * <br/>Scrubber, from {@link MatchHighlightsBinder}
     * <br/>Commentary, from {@link CommentaryBinder}
     * <br/>Match stats, from {@link MatchStatsBinder}
     * <br/>Lineups, from {@link LineupsBinder}
     * <br/>Substitutions, from {@link BenchDataBinder}
     */
    private void preparePastOrLiveMatchAdapter() {
        //TODO: Remove this check once BoardInfo usage is removed from MatchBoardFragment
        if (isLineupFragment) {
            initAndAddCommentaryDataManager();
            initAndAddMatchStatsDataManager();
        } else {
            initAndAddScrubberDataManager();
            initAndAddHighlightsDataManager();
            initAndAddCommentaryDataManager();
            initAndAddMatchStatsDataManager();
            initAndAddLineupsDataManager();
            initAndAddSubstitutionDataManager();
        }
    }

    /**
     * Method for populating components of a match which is about to start in an hour
     * <br/>Consists of:
     * <br/>Scrubber, from {@link ScrubberBinder}
     * <br/>Standings, from {@link StandingsBinder}
     * <br/>Team stats, from {@link TeamStatsBinder}
     * <br/>Lineups, from {@link LineupsBinder}
     */
    private void prepareRecentMatchAdapter() {
        initAndAddScrubberDataManager();
        initAndAddStandingsDataManager();
        initAndAddTeamStatsDataManager();
        initAndAddLineupsDataManager();
        addScheduledMatchFooter();
    }

    /**
     * Method for populating components of a scheduled match when board is active
     * <br/>Consists of:
     * <br/>Standings, from {@link StandingsBinder}
     * <br/>Team stats, from {@link TeamStatsBinder}
     */
    private void prepareRecentMatchAdapterWhenBoardIsActive() {
        initAndAddScrubberDataManager();
        initAndAddStandingsDataManager();
        initAndAddTeamStatsDataManager();
        addScheduledMatchFooter();
    }

    /**
     * Method for populating components of a scheduled match
     * <br/>Consists of:
     * <br/>Standings, from {@link StandingsBinder}
     * <br/>Team stats, from {@link TeamStatsBinder}
     */
    private void prepareScheduledMatchAdapter() {
        initAndAddStandingsDataManager();
        initAndAddTeamStatsDataManager();
        addScheduledMatchFooter();
    }

    private void initAndAddScrubberDataManager() {
        scrubberDataManager = new DataItemManager<>(this, ScrubberBindingModel.Companion.from(matchDetails));
      //  addDataManagerAndRegisterBinder(scrubberDataManager, new ScrubberBinder(listener));
    }

    private void initAndAddHighlightsDataManager() {
        highlightsDataManager = new DataItemManager<>(this, HighlightsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(highlightsDataManager, new MatchHighlightsBinder(activity));
    }

    private void initAndAddCommentaryDataManager() {
        commentaryDataManager = new DataItemManager<>(this, CommentaryBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(commentaryDataManager, new CommentaryBinder(listener));
    }

    private void initAndAddMatchStatsDataManager() {
        matchStatsDataManager = new DataItemManager<>(this, MatchStatsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(matchStatsDataManager, new MatchStatsBinder(glide));
    }

    private void initAndAddLineupsDataManager() {
        lineupsDataManager = new DataItemManager<>(this, LineupsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(lineupsDataManager, new LineupsBinder(activity, glide));
    }

    private void initAndAddSubstitutionDataManager() {
        if (!isNullOrEmpty(matchDetails.getMatchEvents())) {
            substitutionDataManager = new DataItemManager<>(this, SubstitutionBindingModel.Companion.from(matchDetails));
            addDataManagerAndRegisterBinder(substitutionDataManager, new BenchDataBinder(glide));
        }
    }

    private void initAndAddStandingsDataManager() {
        standingsDataManager = new DataItemManager<>(this, StandingsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(standingsDataManager, new StandingsBinder(listener));
    }

    private void initAndAddTeamStatsDataManager() {
        teamStatsDataManager = new DataItemManager<>(this, TeamStatsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(teamStatsDataManager, new TeamStatsBinder(glide));
    }

    private void addScheduledMatchFooter() {
        addDataManagerAndRegisterBinder(new DataItemManager<>(this, ""), new ScheduledMatchFooterBinder());
    }

    private <BM> void addDataManagerAndRegisterBinder(DataItemManager<BM> dataManager, ItemBinder<BM, ? extends ItemViewHolder<BM>> binderToRegister) {
        addDataManager(dataManager);
        registerBinder(binderToRegister);
    }

    //region Methods to update live match data
    public void setScrubber() {
        if (scrubberDataManager != null) {
            scrubberDataManager.setItem(ScrubberBindingModel.Companion.from(matchDetails));
        }
    }

    public void updateScrubber(List<ScrubberData> scrubberDataList, boolean isError) {
        validateAndUpdateList(matchDetails.getScrubberDataList(), scrubberDataList, isError);
        setScrubber();
    }

    public void updateCommentaries(List<Commentary> commentaryList, boolean isError) {
        validateAndUpdateList(matchDetails.getCommentary(), commentaryList, isError);
        commentaryDataManager.setItem(CommentaryBindingModel.Companion.from(matchDetails));
    }

    public void setMatchStats() {
        if (matchStatsDataManager != null) {
            matchStatsDataManager.setItem(MatchStatsBindingModel.Companion.from(matchDetails));
        }
    }

    public void updateMatchStats(MatchStats matchStats, int message) {
        if (matchStats != null) {
            matchDetails.setMatchStats(matchStats);
        }
        matchDetails.getMatchStats().setErrorMessage(message);
        setMatchStats();
    }

    public void setLineups() {
        if (lineupsDataManager != null) {
            lineupsDataManager.setItem(LineupsBindingModel.Companion.from(matchDetails));
        }
    }

    public void updateLineups(Lineups lineups) {
        if (lineups != null) {
            matchDetails.setLineups(lineups);
        }
        matchDetails.getLineups().setErrorMessage(0);
        setLineups();
    }

    public void updateMatchEventsAndSubstitutions(List<MatchEvent> matchEventList, boolean isError) {
        validateAndUpdateList(matchDetails.getMatchEvents(), matchEventList, isError);
        if (substitutionDataManager != null) {
            substitutionDataManager.setItem(SubstitutionBindingModel.Companion.from(matchDetails));
        }
    }

    public void updateHighlights(List<Highlights> highlightsList, boolean isError) {
        validateAndUpdateList(this.matchDetails.getHighlights(), highlightsList, isError);
        if (highlightsDataManager != null) {
            highlightsDataManager.setItem(HighlightsBindingModel.Companion.from(matchDetails));
        }
    }

    public void setPreMatchData() {
        if (standingsDataManager != null) {
            standingsDataManager.setItem(StandingsBindingModel.Companion.from(matchDetails));
        }
        if (teamStatsDataManager != null) {
            teamStatsDataManager.setItem(TeamStatsBindingModel.Companion.from(matchDetails));
        }
        if (lineupsDataManager != null) {
            lineupsDataManager.setItem(LineupsBindingModel.Companion.from(matchDetails));
        }
    }
    //endregion

    private <T> void validateAndUpdateList(List<T> originalList, List<T> newList, boolean isError) {
        if (!isError) {
            if (originalList == null) {
                originalList = new ArrayList<>();
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList);
            }
        }
    }

    public interface BoardInfoAdapterListener {
        void onCommentarySeeAllClick(View fromView);

        void onSeeAllStandingsClick(View fromView);
    }
}