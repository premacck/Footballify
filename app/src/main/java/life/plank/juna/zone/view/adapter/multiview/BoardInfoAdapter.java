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
import life.plank.juna.zone.view.adapter.multiview.binder.ScrubberBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.ScrubberBinder.ScrubberBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.StandingsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.StandingsBinder.StandingsBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.SubstitutionBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.SubstitutionBinder.SubstitutionBindingModel;
import life.plank.juna.zone.view.adapter.multiview.binder.TeamStatsBinder;
import life.plank.juna.zone.view.adapter.multiview.binder.TeamStatsBinder.TeamStatsBindingModel;

import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
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
        scrubberDataManager = new DataItemManager<>(this, ScrubberBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(0, scrubberDataManager, new ScrubberBinder(listener));

        highlightsDataManager = new DataItemManager<>(this, HighlightsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(1, highlightsDataManager, new MatchHighlightsBinder(activity));

        commentaryDataManager = new DataItemManager<>(this, CommentaryBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(2, commentaryDataManager, new CommentaryBinder(listener));

        matchStatsDataManager = new DataItemManager<>(this, MatchStatsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(3, matchStatsDataManager, new MatchStatsBinder(picasso));

        lineupsDataManager = new DataItemManager<>(this, LineupsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(4, lineupsDataManager, new LineupsBinder(activity, picasso));

        substitutionDataManager = new DataItemManager<>(this, SubstitutionBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(5, substitutionDataManager, new SubstitutionBinder(picasso));
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
        scrubberDataManager = new DataItemManager<>(this, ScrubberBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(0, scrubberDataManager, new ScrubberBinder(listener));

        standingsDataManager = new DataItemManager<>(this, StandingsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(1, standingsDataManager, new StandingsBinder(picasso, listener));

        teamStatsDataManager = new DataItemManager<>(this, TeamStatsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(2, teamStatsDataManager, new TeamStatsBinder(picasso));

        lineupsDataManager = new DataItemManager<>(this, LineupsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(3, lineupsDataManager, new LineupsBinder(activity, picasso));
    }

    /**
     * Method for populating components of a scheduled match
     * <br/>Consists of:
     * <br/>Standings, from {@link StandingsBinder}
     * <br/>Team stats, from {@link TeamStatsBinder}
     */
    private void prepareScheduledMatchAdapter() {
        standingsDataManager = new DataItemManager<>(this, StandingsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(1, standingsDataManager, new StandingsBinder(picasso, listener));

        teamStatsDataManager = new DataItemManager<>(this, TeamStatsBindingModel.from(matchDetails));
        addDataManagerAndRegisterBinder(2, teamStatsDataManager, new TeamStatsBinder(picasso));
    }

    private <BM> void addDataManagerAndRegisterBinder(int index, DataItemManager<BM> dataManager, ItemBinder<BM, ? extends ItemViewHolder<BM>> binderToRegister) {
        addDataManager(index, dataManager);
        registerBinder(binderToRegister);
    }

    //region Methods to update live match data
    public void setScrubber() {
        commentaryDataManager.setItem(CommentaryBindingModel.from(matchDetails));
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
        matchStatsDataManager.setItem(MatchStatsBindingModel.from(matchDetails));
    }

    public void updateMatchStats(MatchStats matchStats, int message) {
        if (matchStats != null) {
            matchDetails.setMatchStats(matchStats);
        }
        matchDetails.getMatchStats().setErrorMessage(message);
        setMatchStats();
    }

    public void setLineups() {
        lineupsDataManager.setItem(LineupsBindingModel.from(matchDetails));
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
        substitutionDataManager.setItem(SubstitutionBindingModel.from(matchDetails));
    }

    public void updateHighlights(List<Highlights> highlightsList, boolean isError) {
        validateAndUpdateList(this.matchDetails.getHighlights(), highlightsList, isError);
        highlightsDataManager.setItem(HighlightsBindingModel.from(matchDetails));
    }

    public void setPreMatchData() {
        standingsDataManager.setItem(StandingsBindingModel.from(matchDetails));
        teamStatsDataManager.setItem(TeamStatsBindingModel.from(matchDetails));
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