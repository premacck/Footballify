package life.plank.juna.zone.view.adapter.board.info;

import android.app.Activity;

import com.ahamed.multiviewadapter.DataItemManager;
import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.binder.HighlightsBindingModel;
import life.plank.juna.zone.data.model.binder.LineupsBindingModel;
import life.plank.juna.zone.data.model.binder.MatchStatsBindingModel;
import life.plank.juna.zone.data.model.binder.SubstitutionBindingModel;
import life.plank.juna.zone.util.AppConstants.MatchTimeVal;
import life.plank.juna.zone.view.adapter.board.info.binder.CommentaryBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.LineupsBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.MatchHighlightsBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.MatchStatsBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.ScheduledMatchFooterBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.StandingsBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.SubstitutionBinder;
import life.plank.juna.zone.view.adapter.board.info.binder.TeamStatsBinder;

import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START_BOARD_ACTIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_PAST;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DateUtil.getMatchTimeValue;

public class LineupAdapter extends RecyclerAdapter {

    private Picasso picasso;
    private MatchDetails matchDetails;
    private Activity activity;

    private DataItemManager<LineupsBindingModel> lineupsDataManager;
    private DataItemManager<SubstitutionBindingModel> substitutionDataManager;

    public LineupAdapter(MatchDetails matchDetails, Picasso picasso, Activity activity) {
        this.matchDetails = matchDetails;
        this.picasso = picasso;
        this.activity = activity;

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
        }
    }

    /**
     * Method for populating components of a past or live match
     * <br/>Consists of:
     * <br/>Scrubber, from {@link MatchHighlightsBinder}
     * <br/>Commentary, from {@link CommentaryBinder}
     * <br/>Match stats, from {@link MatchStatsBinder}
     * <br/>Lineups, from {@link LineupsBinder}
     * <br/>Substitutions, from {@link SubstitutionBinder}
     */
    private void preparePastOrLiveMatchAdapter() {

        initAndAddHighlightsDataManager();
        initAndAddMatchStatsDataManager();
        initAndAddLineupsDataManager();
        initAndAddSubstitutionDataManager();
    }

    /**
     * Method for populating components of a match which is about to start in an hour
     * <br/>Consists of:
     * <br/>Team stats, from {@link TeamStatsBinder}
     * <br/>Lineups, from {@link LineupsBinder}
     */
    private void prepareRecentMatchAdapter() {

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

        addScheduledMatchFooter();
    }

    private void initAndAddHighlightsDataManager() {
        DataItemManager<HighlightsBindingModel> highlightsDataManager = new DataItemManager<>(this, HighlightsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(highlightsDataManager, new MatchHighlightsBinder(activity));
    }

    private void initAndAddMatchStatsDataManager() {
        DataItemManager<MatchStatsBindingModel> matchStatsDataManager = new DataItemManager<>(this, MatchStatsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(matchStatsDataManager, new MatchStatsBinder(picasso));
    }

    private void initAndAddLineupsDataManager() {
        lineupsDataManager = new DataItemManager<>(this, LineupsBindingModel.Companion.from(matchDetails));
        addDataManagerAndRegisterBinder(lineupsDataManager, new LineupsBinder(activity, picasso));
    }

    private void initAndAddSubstitutionDataManager() {
        if (!isNullOrEmpty(matchDetails.getMatchEvents())) {
            substitutionDataManager = new DataItemManager<>(this, SubstitutionBindingModel.Companion.from(matchDetails));
            addDataManagerAndRegisterBinder(substitutionDataManager, new SubstitutionBinder(picasso));
        }
    }

    private void addScheduledMatchFooter() {
        addDataManagerAndRegisterBinder(new DataItemManager<>(this, ""), new ScheduledMatchFooterBinder());
    }

    private <BM> void addDataManagerAndRegisterBinder(DataItemManager<BM> dataManager, ItemBinder<BM, ? extends ItemViewHolder<BM>> binderToRegister) {
        addDataManager(dataManager);
        registerBinder(binderToRegister);
    }

    //region Methods to update live match data

    public void setLineups() {
        if (lineupsDataManager != null) {
            lineupsDataManager.setItem(LineupsBindingModel.Companion.from(matchDetails));
        }
    }

    public void updateMatchEventsAndSubstitutions(List<MatchEvent> matchEventList, boolean isError) {
        validateAndUpdateList(matchDetails.getMatchEvents(), matchEventList, isError);
        if (substitutionDataManager != null) {
            substitutionDataManager.setItem(SubstitutionBindingModel.Companion.from(matchDetails));
        }
    }


    public void setPreMatchData() {
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

}