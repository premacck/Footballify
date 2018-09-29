package life.plank.juna.zone.view.adapter.multiview;

import android.view.View;

import com.ahamed.multiviewadapter.DataItemManager;
import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.Highlights;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchStats;
import life.plank.juna.zone.data.network.model.ScrubberData;
import life.plank.juna.zone.util.AppConstants.MatchTimeVal;
import life.plank.juna.zone.view.activity.MatchBoardActivity;
import life.plank.juna.zone.view.adapter.multiview.binder.CommentaryBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.CommentaryBinder.CommentaryBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.LineupsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.LineupsBinder.LineupsBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.MatchHighlightsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.MatchHighlightsBinder.HighlightsBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.MatchStatsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.MatchStatsBinder.MatchStatsBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.ScheduledMatchFooterBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.ScrubberBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.ScrubberBinder.ScrubberBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.StandingsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.StandingsBinder.StandingsBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.SubstitutionBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.SubstitutionBinder.SubstitutionBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.TeamStatsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.TeamStatsBinder.TeamStatsBindingModel;

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

    private Picasso picasso;
    private MatchDetails matchDetails;
    private MatchBoardActivity activity;
    private BoardInfoAdapterListener listener;

    private DataItemManager<ScrubberBindingModel> scrubberDataManager;
    private DataItemManager<HighlightsBindingModel> highlightsDataManager;
    private DataItemManager<CommentaryBindingModel> commentaryDataManager;
    private DataItemManager<MatchStatsBindingModel> matchStatsDataManager;
    private DataItemManager<LineupsBindingModel> lineupsDataManager;
    private DataItemManager<SubstitutionBindingModel> substitutionDataManager;
    private DataItemManager<StandingsBindingModel> standingsDataManager;
    private DataItemManager<TeamStatsBindingModel> teamStatsDataManager;

    public BoardInfoAdapter(MatchDetails matchDetails, Picasso picasso, MatchBoardActivity activity, BoardInfoAdapterListener listener) {
        this.matchDetails = matchDetails;
        this.picasso = picasso;
        this.activity = activity;
        this.listener = listener;

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
     * <br/>Substitutions, from {@link SubstitutionBinder}
     */
    private void preparePastOrLiveMatchAdapter() {
        initAndAddScrubberDataManager();
        initAndAddHighlightsDataManager();
        initAndAddCommentaryDataManager();
        initAndAddMatchStatsDataManager();
        initAndAddLineupsDataManager();
        initAndAddSubstitutionDataManager();
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
        scrubberDataManager = new DataItemManager<>(this, ScrubberBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(scrubberDataManager, new ScrubberBinder(listener));
    }

    private void initAndAddHighlightsDataManager() {
        highlightsDataManager = new DataItemManager<>(this, HighlightsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(highlightsDataManager, new MatchHighlightsBinder(activity));
    }

    private void initAndAddCommentaryDataManager() {
        commentaryDataManager = new DataItemManager<>(this, CommentaryBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(commentaryDataManager, new CommentaryBinder(listener));
    }

    private void initAndAddMatchStatsDataManager() {
        matchStatsDataManager = new DataItemManager<>(this, MatchStatsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(matchStatsDataManager, new MatchStatsBinder(picasso));
    }

    private void initAndAddLineupsDataManager() {
        lineupsDataManager = new DataItemManager<>(this, LineupsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(lineupsDataManager, new LineupsBinder(activity, picasso));
    }

    private void initAndAddSubstitutionDataManager() {
        if (!isNullOrEmpty(matchDetails.getMatchEvents())) {
            substitutionDataManager = new DataItemManager<>(this, SubstitutionBindingModel.from(matchDetails));
            addDataManagerAndRegisterBinder(substitutionDataManager, new SubstitutionBinder(picasso));
        }
    }

    private void initAndAddStandingsDataManager() {
        standingsDataManager = new DataItemManager<>(this, StandingsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(standingsDataManager, new StandingsBinder(picasso, listener));
    }

    private void initAndAddTeamStatsDataManager() {
        teamStatsDataManager = new DataItemManager<>(this, TeamStatsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(teamStatsDataManager, new TeamStatsBinder(picasso));
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
            scrubberDataManager.setItem(ScrubberBindingModel.from(matchDetails));
        }
    }

    public void updateScrubber(List<ScrubberData> scrubberDataList, boolean isError) {
        validateAndUpdateList(matchDetails.getScrubberDataList(), scrubberDataList, isError);
        setScrubber();
    }

    public void updateCommentaries(List<Commentary> commentaryList, boolean isError) {
        validateAndUpdateList(matchDetails.getCommentary(), commentaryList, isError);
        commentaryDataManager.setItem(CommentaryBindingModel.from(matchDetails));
    }

    public void setMatchStats() {
        if (matchStatsDataManager != null) {
            matchStatsDataManager.setItem(MatchStatsBindingModel.from(matchDetails));
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
            lineupsDataManager.setItem(LineupsBindingModel.from(matchDetails));
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
            substitutionDataManager.setItem(SubstitutionBindingModel.from(matchDetails));
        }
    }

    public void updateHighlights(List<Highlights> highlightsList, boolean isError) {
        validateAndUpdateList(this.matchDetails.getHighlights(), highlightsList, isError);
        if (highlightsDataManager != null) {
            highlightsDataManager.setItem(HighlightsBindingModel.from(matchDetails));
        }
    }

    public void setPreMatchData() {
        if (standingsDataManager != null) {
            standingsDataManager.setItem(StandingsBindingModel.from(matchDetails));
        }
        if (teamStatsDataManager != null) {
            teamStatsDataManager.setItem(TeamStatsBindingModel.from(matchDetails));
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
        void onScrubberClick(View fromView);

        void onCommentarySeeAllClick(View fromView);

        void onSeeAllStandingsClick(View fromView);
    }
}