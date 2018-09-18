package life.plank.juna.zone.view.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchDetails;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchStats;
import life.plank.juna.zone.data.network.model.ScrubberData;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.data.network.model.TeamStatsModel;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.customview.CommentarySmall;
import life.plank.juna.zone.util.customview.HighlightsAdapter;
import life.plank.juna.zone.util.customview.LineupLayout;
import life.plank.juna.zone.util.customview.MatchHighlights;
import life.plank.juna.zone.util.customview.MatchStatsLayout;
import life.plank.juna.zone.util.customview.ScrubberLayout;
import life.plank.juna.zone.util.customview.StandingsLayout;
import life.plank.juna.zone.util.customview.SubstitutionLayout;
import life.plank.juna.zone.util.customview.TeamStatsLayout;
import life.plank.juna.zone.view.fragment.board.fixture.BoardInfoFragment;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;

public class BoardInfoAdapter extends BaseRecyclerView.Adapter<BaseRecyclerView.ViewHolder> {

    private static final String TAG = BoardInfoAdapter.class.getSimpleName();
    private static final int TYPE_SCRUBBER_VIEW = 11;
    private static final int TYPE_MATCH_HIGHLIGHTS_VIEW = 12;
    private static final int TYPE_COMMENTARY_VIEW = 13;
    private static final int TYPE_MATCH_STATS_VIEW = 14;
    private static final int TYPE_LINEUPS_VIEW = 15;
    private static final int TYPE_SUBSTITUTION_VIEW = 16;
    private static final int TYPE_STANDINGS_VIEW = 21;
    private static final int TYPE_TEAM_STATS_VIEW = 22;
    private final Activity activity;
    private BoardInfoFragment fragment;
    private boolean isBoardStarted;
    private Picasso picasso;
    private List<ScrubberData> scrubberDataList;
    private MatchDetails matchDetails;
    private MatchStats matchStats;
    private Lineups lineups;
    private List<TeamStatsModel> teamStatModels;
    private List<StandingModel> standingsList;

    public BoardInfoAdapter(BoardInfoFragment fragment, Activity activity, Picasso picasso, boolean isBoardStarted, MatchDetails matchDetails) {
        this.fragment = fragment;
        this.isBoardStarted = isBoardStarted;
        this.picasso = picasso;
        this.activity = activity;
        this.matchDetails = matchDetails;

        scrubberDataList = new ArrayList<>();
        matchStats = new MatchStats();
        lineups = new Lineups();
        teamStatModels = new ArrayList<>();
        standingsList = new ArrayList<>();
    }

    @Override
    public BaseRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoardInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_info, parent, false), this, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isBoardStarted) {
            switch (position) {
                case 0:
                    return TYPE_SCRUBBER_VIEW;
                case 1:
                    return TYPE_MATCH_HIGHLIGHTS_VIEW;
                case 2:
                    return TYPE_COMMENTARY_VIEW;
                case 3:
                    return TYPE_MATCH_STATS_VIEW;
                case 4:
                    return TYPE_LINEUPS_VIEW;
                case 5:
                    return TYPE_SUBSTITUTION_VIEW;
                default:
                    return 0;
            }
        } else {
            switch (position) {
                case 0:
                    return TYPE_SCRUBBER_VIEW;
                case 1:
                    return TYPE_STANDINGS_VIEW;
                case 2:
                    return TYPE_TEAM_STATS_VIEW;
                default:
                    return 0;
            }
        }
    }

    /**
     * For started board, we require:
     * <br/>\t * Scrubber
     * <br/>\t * Match highlights, if present
     * <br/>\t * Commentary
     * <br/>\t * Match stats
     * <br/>\t * Lineups
     * <br/>\t * Substitutions
     * <p>
     * For the board which is yet to start, we require<br/>
     * <br/>\t * Scrubber
     * <br/>\t * Standings
     * <br/>\t * Team stats
     *
     * @return if matchDetails are there, 6 for started board, 3 otherwise
     */
    @Override
    public int getItemCount() {
        return isBoardStarted ? 6 : 3;
    }

    public void setScrubberData(List<ScrubberData> scrubberDataList, boolean isError) {
        validateAndUpdateList(this.scrubberDataList, scrubberDataList, isError);
        notifyItemChanged(0);
    }

    public void setMatchDetails(MatchDetails matchDetails, boolean isMatchStarted) {
        this.matchDetails = matchDetails;
        if (isMatchStarted) {
            if (matchDetails != null) {
                setMatchStats(matchDetails.getMatchStats(), 0);
                setLineups(matchDetails.getLineups(), 0);
            } else {
                setMatchStats(null, R.string.match_yet_to_start);
                setLineups(null, R.string.line_ups_not_available);
            }
//            Update Match Highlights
            notifyItemChanged(1);

//            Update MatchStats (3) or TeamStats(2)
            notifyItemChanged(isBoardStarted ? 3 : 2);

            if (isBoardStarted) {
//                Update Commentaries
                notifyItemChanged(2);

//                Update Match Events (Substitutions)
                notifyItemChanged(5);
            }
        } else {
            if (matchDetails != null) {
                setStandings(matchDetails.getStandingsList(), false);
                setTeamStats(matchDetails.getTeamStatsList(), false);
            } else {
                setStandings(null, true);
                setTeamStats(null, true);
            }
        }
    }

    /**
     * Method to update live commentaries only.
     */
    public void setCommentaries(List<Commentary> commentaryList, boolean isError) {
        if (matchDetails != null) {
            validateAndUpdateList(matchDetails.getCommentary(), commentaryList, isError);
            if (isBoardStarted) notifyItemChanged(2);
        }
    }

    public List<Commentary> getCommentaryList() {
        return matchDetails.getCommentary();
    }

    public void setMatchStats(MatchStats matchStats, int message) {
        if (matchStats != null) {
            this.matchStats = matchStats;
        }
        this.matchStats.setErrorMessage(message);
        if (isBoardStarted) notifyItemChanged(3);
    }

    public void setLineups(Lineups lineups, int message) {
        if (lineups != null) {
            this.lineups = lineups;
        }
        this.lineups.setErrorMessage(message);
        if (isBoardStarted) notifyItemChanged(4);
    }

    /**
     * Method to update live match events only.
     */
    public void setMatchEvents(List<MatchEvent> matchEventList, boolean isError) {
        if (matchDetails != null) {
            validateAndUpdateList(matchDetails.getMatchEvents(), matchEventList, isError);
            if (isBoardStarted) notifyItemChanged(5);
        }
    }

    public void setStandings(List<StandingModel> standingsList, boolean isError) {
        validateAndUpdateList(this.standingsList, standingsList, isError);
        if (!isBoardStarted) notifyItemChanged(1);
    }

    public void setTeamStats(List<TeamStatsModel> teamStatModels, boolean isError) {
        validateAndUpdateList(this.teamStatModels, teamStatModels, isError);
        if (!isBoardStarted) notifyItemChanged(2);
    }

    private <T> void validateAndUpdateList(List<T> originalList, List<T> newList, boolean isError) {
        if (!isError) {
            if (originalList == null) {
                originalList = new ArrayList<>();
            }
            if (!isNullOrEmpty(newList)) {
                originalList.addAll(newList);
            }
        } else {
            originalList = null;
        }
    }

    static class BoardInfoViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<BoardInfoAdapter> ref;
        @BindView(R.id.root_layout)
        FrameLayout rootLayout;

        BoardInfoViewHolder(View itemView, BoardInfoAdapter adapter, int viewType) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(adapter);
            ViewGroup customView = getCustomView(viewType);
            if (customView != null) {
                rootLayout.addView(customView);
            }
        }

        @Override
        public void bind() {
            if (ref.get().isBoardStarted) {
                switch (getAdapterPosition()) {
                    case 0:
                        prepareScrubber();
                        break;
                    case 1:
                        prepareMatchHighlights();
                        break;
                    case 2:
                        prepareCommentary();
                        break;
                    case 3:
                        prepareMatchStats();
                        break;
                    case 4:
                        prepareLineups();
                        break;
                    case 5:
                        prepareSubstitutions();
                        break;
                    default:
                        break;
                }
            } else {
                switch (getAdapterPosition()) {
                    case 0:
                        prepareScrubber();
                        break;
                    case 1:
                        prepareStandings();
                        break;
                    case 2:
                        prepareTeamStats();
                        break;
                    default:
                        break;
                }
            }
        }

        private void prepareMatchHighlights() {
            try {
                MatchHighlights matchHighlightsLayout = (MatchHighlights) rootLayout.getChildAt(0);
                if (ref.get().matchDetails != null) {
                    if (!isNullOrEmpty(ref.get().matchDetails.getHighlights())) {
                        matchHighlightsLayout.setLoading(false);
                        matchHighlightsLayout.setVisibility(View.VISIBLE);
                        int highlightsWidth = getScreenSize(ref.get().activity.getWindowManager().getDefaultDisplay())[0];
//                        Setting highlights
                        int highlightsHeight = (highlightsWidth * 9) / 17;
                        matchHighlightsLayout.setAdapter(new HighlightsAdapter(highlightsHeight));
                        matchHighlightsLayout.setHighlights(ref.get().matchDetails.getHighlights());
                    } else
                        matchHighlightsLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e(TAG, "prepareMatchHighlights: ", e);
            }
        }

        private void prepareCommentary() {
            try {
                CommentarySmall commentaryLayout = (CommentarySmall) rootLayout.getChildAt(0);
                if (ref.get().matchDetails != null && ref.get().matchDetails.getCommentary() != null) {
                    if (!ref.get().matchDetails.getCommentary().isEmpty()) {
                        commentaryLayout.setLoading(false);
                        commentaryLayout.setAdapter(new CommentaryAdapter());
                        commentaryLayout.updateAdapter(ref.get().matchDetails.getCommentary());
                        commentaryLayout.initListeners(ref.get().fragment);
                    } else
                        commentaryLayout.setLoading(true);
                } else {
                    commentaryLayout.notAvailable(R.string.match_yet_to_start);
                }
            } catch (Exception e) {
                Log.e(TAG, "prepareCommentary: ", e);
            }
        }

        private void prepareMatchStats() {
            try {
                MatchStatsLayout matchStatsLayout = (MatchStatsLayout) rootLayout.getChildAt(0);
                if (ref.get().matchDetails != null && ref.get().matchStats != null) {
                    if (ref.get().matchStats.getErrorMessage() == 0) {
                        matchStatsLayout.setLoading(false);
                        matchStatsLayout.update(
                                ref.get().matchStats,
                                ref.get().matchDetails.getVenue().getName(),
                                ref.get().matchDetails.getHomeTeam().getLogoLink(),
                                ref.get().matchDetails.getAwayTeam().getLogoLink(),
                                ref.get().picasso
                        );
                    } else {
                        matchStatsLayout.notAvailable(ref.get().matchStats.getErrorMessage());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "prepareMatchStats: ", e);
            }
        }

        private void prepareLineups() {
            try {
                LineupLayout lineupLayout = (LineupLayout) rootLayout.getChildAt(0);
                if (ref.get().matchDetails != null && ref.get().lineups != null) {
                    if (ref.get().lineups.getErrorMessage() == 0) {
                        lineupLayout.setLoading(false);
                        lineupLayout.update(
                                ref.get().lineups,
                                ref.get().matchDetails.getHomeTeam(),
                                ref.get().matchDetails.getAwayTeam(),
                                ref.get().picasso
                        );
                    } else {
                        lineupLayout.notAvailable(ref.get().lineups.getErrorMessage());
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "prepareLineups: ", e);
            }
        }

        private void prepareSubstitutions() {
            try {
                SubstitutionLayout substitutionLayout = (SubstitutionLayout) rootLayout.getChildAt(0);
                if (ref.get().matchDetails != null) {
                    substitutionLayout.setVisibility(View.VISIBLE);
                    substitutionLayout.setLoading(false);
                    substitutionLayout.setAdapter(new SubstitutionAdapter());
                    substitutionLayout.update(
                            ref.get().matchDetails.getMatchEvents(),
                            ref.get().matchDetails.getHomeTeam().getLogoLink(),
                            ref.get().matchDetails.getAwayTeam().getLogoLink(),
                            ref.get().picasso
                    );
                } else {
                    substitutionLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e(TAG, "prepareSubstitutions: ", e);
            }
        }

        private void prepareScrubber() {
            try {
                ScrubberLayout scrubberLayout = (ScrubberLayout) rootLayout.getChildAt(0);
                scrubberLayout.setLoading(false);
                scrubberLayout.prepare();
//                TODO : Un-comment below code, and remove the line above after backend is set up for scrubber.
/*                if (!isNullOrEmpty(ref.get().scrubberDataList)) {
                    scrubberLayout.setLoading(false);
                    scrubberLayout.prepare();
                } else
                    scrubberLayout.setLoading(true);*/
                scrubberLayout.initListeners(ref.get().fragment);
            } catch (Exception e) {
                Log.e(TAG, "prepareScrubber: ", e);
            }
        }

        private void prepareStandings() {
            try {
                StandingsLayout standingsLayout = (StandingsLayout) rootLayout.getChildAt(0);
                if (!isNullOrEmpty(ref.get().standingsList)) {
                    standingsLayout.setLoading(false);
                    standingsLayout.setAdapter(new StandingTableAdapter(ref.get().picasso));
                    standingsLayout.update(ref.get().standingsList);
                } else
                    standingsLayout.notAvailable(R.string.failed_to_get_standings);
            } catch (Exception e) {
                Log.e(TAG, "prepareStandings: ", e);
            }
        }

        private void prepareTeamStats() {
            try {
                TeamStatsLayout teamStatsLayout = (TeamStatsLayout) rootLayout.getChildAt(0);
                if (!isNullOrEmpty(ref.get().teamStatModels)) {
                    teamStatsLayout.setLoading(false);
                    teamStatsLayout.update(
                            ref.get().teamStatModels,
                            ref.get().matchDetails.getLeague(),
                            ref.get().matchDetails.getHomeTeam().getLogoLink(),
                            ref.get().matchDetails.getAwayTeam().getLogoLink(),
                            ref.get().picasso
                    );
                } else teamStatsLayout.notAvailable(R.string.team_stats_not_available_yet);
            } catch (Exception e) {
                Log.e(TAG, "prepareTeamStats: ", e);
            }
        }

        private ViewGroup getCustomView(int viewType) {
            if (ref.get().isBoardStarted) {
                switch (viewType) {
                    case TYPE_SCRUBBER_VIEW:
                        return new ScrubberLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_MATCH_HIGHLIGHTS_VIEW:
                        return new MatchHighlights(ref.get().activity, null);
                    case TYPE_COMMENTARY_VIEW:
                        return new CommentarySmall(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_MATCH_STATS_VIEW:
                        return new MatchStatsLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_LINEUPS_VIEW:
                        return new LineupLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_SUBSTITUTION_VIEW:
                        return new SubstitutionLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    default:
                        return null;
                }
            } else {
                switch (viewType) {
                    case TYPE_SCRUBBER_VIEW:
                        return new ScrubberLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_STANDINGS_VIEW:
                        return new StandingsLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    case TYPE_TEAM_STATS_VIEW:
                        return new TeamStatsLayout(ref.get().activity, null, R.style.BoardInfoLayout);
                    default:
                        return null;
                }
            }
        }
    }
}