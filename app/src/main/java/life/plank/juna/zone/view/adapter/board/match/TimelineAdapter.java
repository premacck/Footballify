package life.plank.juna.zone.view.adapter.board.match;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.util.view.BaseRecyclerView;

import static life.plank.juna.zone.util.common.AppConstants.FULL_TIME;
import static life.plank.juna.zone.util.common.AppConstants.GOAL;
import static life.plank.juna.zone.util.common.AppConstants.HALF_TIME;
import static life.plank.juna.zone.util.common.AppConstants.HT;
import static life.plank.juna.zone.util.common.AppConstants.KICK_OFF;
import static life.plank.juna.zone.util.common.AppConstants.LIVE;
import static life.plank.juna.zone.util.common.AppConstants.LIVE_TIME;
import static life.plank.juna.zone.util.common.AppConstants.PENALTY;
import static life.plank.juna.zone.util.common.AppConstants.RED_CARD;
import static life.plank.juna.zone.util.common.AppConstants.SUBSTITUTION;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_CARD;
import static life.plank.juna.zone.util.common.AppConstants.YELLOW_RED;
import static life.plank.juna.zone.util.common.DataUtil.getFormattedExtraMinutes;
import static life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.view.UIDisplayUtil.getDp;

public class TimelineAdapter extends BaseRecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    private List<MatchEvent> matchEventList;
    private Context context;

    public TimelineAdapter(Context context) {
        this.context = context;
        matchEventList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimelineViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false),
                this
        );
    }

    @Override
    public int getItemCount() {
        return matchEventList.size();
    }

    public void updateEvents(List<MatchEvent> matchEventList) {
        this.matchEventList.addAll(matchEventList);
        notifyDataSetChanged();
    }

    /**
     * Adding the element at its respective position.
     * Best case runtime : O(1)
     * Worst case runtime : O(n)
     * Live case runtime : O(1) - because live events are going to be coming "live", so it will always insert at 0th position.
     */
    public void updateLiveEvents(List<MatchEvent> matchEventList) {
        if (!isNullOrEmpty(matchEventList) && !isNullOrEmpty(matchEventList.get(0).getPlayerName())) {
            int positionToInsert = 0;
            for (MatchEvent matchEvent : this.matchEventList) {
                if (matchEvent.getMinute() > matchEventList.get(0).getMinute()) {
                    positionToInsert = this.matchEventList.indexOf(matchEvent);
                } else break;
            }
            this.matchEventList.addAll(positionToInsert, matchEventList);
            notifyItemRangeInserted(positionToInsert, matchEventList.size());
        }
    }

    /**
     * Adding the whistle event at its respective position.
     */
    public void updateWhistleEvent(LiveTimeStatus timeStatus) {
        int positionToInsert = 0;
        for (MatchEvent matchEvent : matchEventList) {
            if (matchEvent.getMinute() > timeStatus.getMinute()) {
                positionToInsert = this.matchEventList.indexOf(matchEvent);
            } else break;
        }
        matchEventList.add(positionToInsert, new MatchEvent(timeStatus));
        notifyItemInserted(positionToInsert);
    }

    static class TimelineViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.center_space)
        View verticalLine;
        @BindView(R.id.whistle_event_layout)
        RelativeLayout whistleLayout;
        @BindView(R.id.whistle)
        ImageView whistleImage;
        @BindView(R.id.whistle_text_1)
        TextView whistleEventUp;
        @BindView(R.id.whistle_text_2)
        TextView whistleEventDown;
        @BindView(R.id.minute)
        TextView minuteView;
        @BindView(R.id.timeline_layout)
        LinearLayout timelineLayout;
        @BindView(R.id.timeline_text_1)
        TextView timelineEventUp;
        @BindView(R.id.timeline_text_2)
        TextView timelineEventDown;

        private final WeakReference<TimelineAdapter> ref;
        private MatchEvent event;

        TimelineViewHolder(View itemView, TimelineAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(adapter);
        }

        @Override
        public void bind() {
            event = ref.get().matchEventList.get(getAdapterPosition());

            if (event.getLiveTimeStatus() != null) {
                setLayout(true);
                onWhistleEvent();
            } else {
                setLayout(false);
                placeTimelineLayout();

                switch (event.getEventType()) {
                    case GOAL:
                        onGoalEvent();
                        break;
                    case YELLOW_CARD:
                    case RED_CARD:
                    case YELLOW_RED:
                        onCardEvent();
                        break;
                    case SUBSTITUTION:
                        onSubstitutionEvent();
                        break;
                    case PENALTY:
                        onPenaltyEvent();
                        break;
                    default:
                        break;
                }
            }
        }

        private void setLayout(boolean isWhistleEvent) {
            whistleLayout.setVisibility(isWhistleEvent ? View.VISIBLE : View.GONE);
            timelineLayout.setVisibility(isWhistleEvent ? View.GONE : View.VISIBLE);
            minuteView.setVisibility(isWhistleEvent ? View.GONE : View.VISIBLE);
        }

        private void placeTimelineLayout() {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timelineLayout.getLayoutParams();
            params.addRule(
                    event.isHomeTeam() ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END,
                    RelativeLayout.TRUE
            );
            params.removeRule(event.isHomeTeam() ? RelativeLayout.ALIGN_PARENT_END : RelativeLayout.ALIGN_PARENT_START);
            params.addRule(event.isHomeTeam() ? RelativeLayout.START_OF : RelativeLayout.END_OF, R.id.center_space);
            params.removeRule(event.isHomeTeam() ? RelativeLayout.END_OF : RelativeLayout.START_OF);
            params.rightMargin = (int) getDp(event.isHomeTeam() ? 22 : 0);
            params.leftMargin = (int) getDp(event.isHomeTeam() ? 0 : 22);
            timelineEventUp.setGravity(Gravity.CENTER_VERTICAL | (event.isHomeTeam() ? Gravity.END : Gravity.START));
            timelineEventDown.setGravity(Gravity.CENTER_VERTICAL | (event.isHomeTeam() ? Gravity.END : Gravity.START));
            LinearLayout.LayoutParams eventParams = (LinearLayout.LayoutParams) timelineEventUp.getLayoutParams();
            eventParams.gravity = Gravity.CENTER_VERTICAL | (event.isHomeTeam() ? Gravity.END : Gravity.START);
            timelineEventUp.setLayoutParams(eventParams);
            timelineEventDown.setLayoutParams(eventParams);
            timelineLayout.setLayoutParams(params);
        }

        private void onWhistleEvent() {
            whistleImage.setVisibility(View.VISIBLE);
            verticalLine.setVisibility(View.INVISIBLE);
            minuteView.setVisibility(View.GONE);
            whistleEventUp.setText(getTimedEventString());
            whistleEventDown.setVisibility(View.VISIBLE);
            String whistleEventString = "+" + getTimedEventExtraString();
            whistleEventDown.setText(whistleEventString);
        }

        private void onCardEvent() {
            setCenterLayout();
            timelineEventDown.setCompoundDrawablePadding((int) getDp(10));
            int suitableCardDrawable = event.getEventType().contains(ref.get().context.getString(R.string.red)) ?
                    event.isHomeTeam() ?
                            R.drawable.red_left :
                            R.drawable.red_right :
                    event.isHomeTeam() ?
                            R.drawable.yellow_left :
                            R.drawable.yellow_right;
            if (event.isHomeTeam()) {
                timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, suitableCardDrawable, 0);
            } else {
                timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(suitableCardDrawable, 0, 0, 0);
            }
            timelineEventUp.setText(event.getPlayerName());
            timelineEventDown.setVisibility(View.GONE);
        }

        private void onSubstitutionEvent() {
            setCenterLayout();
            timelineEventDown.setCompoundDrawablePadding((int) getDp(10));
            if (event.isHomeTeam()) {
                timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_substitute_in, 0);
                timelineEventDown.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_substitute_out, 0);
            } else {
                timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_substitute_in, 0, 0, 0);
                timelineEventDown.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_substitute_out, 0, 0, 0);
            }
            timelineEventUp.setText(event.getPlayerName());
            timelineEventDown.setVisibility(View.VISIBLE);
            timelineEventDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            timelineEventDown.setText(event.getRelatedPlayerName());
        }

        private void onGoalEvent() {
            setCenterLayout();
            timelineEventDown.setCompoundDrawablePadding((int) getDp(4));
            timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(
                    event.isHomeTeam() ? 0 : R.drawable.ic_goal_right,
                    0,
                    event.isHomeTeam() ? R.drawable.ic_goal_left : 0,
                    0
            );
            timelineEventUp.setText(event.getPlayerName());
            timelineEventDown.setVisibility(View.VISIBLE);
            timelineEventDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            String timelineEventDownText;
            if (event.getRelatedPlayerName() != null) {
                timelineEventDown.setCompoundDrawablesWithIntrinsicBounds(
                        event.isHomeTeam() ? R.drawable.ic_assist : 0,
                        0,
                        event.isHomeTeam() ? 0 : R.drawable.ic_assist,
                        0
                );
                timelineEventDownText = event.isHomeTeam() ?
                        event.getRelatedPlayerName() + "    " + event.getResult() :
                        event.getResult() + "    " + event.getRelatedPlayerName();
            } else {
                timelineEventDown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                timelineEventDownText = event.getResult();
            }
            timelineEventDown.setText(timelineEventDownText);
        }

        private void onPenaltyEvent() {
            setCenterLayout();
            timelineEventDown.setVisibility(View.VISIBLE);
            timelineEventUp.setVisibility(View.VISIBLE);
            timelineEventDown.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            timelineEventUp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            timelineEventDown.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            timelineEventUp.setText(event.getPlayerName());
            timelineEventDown.setText(PENALTY);
        }

        private String getTimedEventString() {
            switch (Objects.requireNonNull(event.getLiveTimeStatus()).getTimeStatus()) {
                case LIVE:
                    return KICK_OFF;
                case HT:
                    return HALF_TIME;
                default:
                    return FULL_TIME;
            }
        }

        private String getTimedEventExtraString() {
            switch (Objects.requireNonNull(event.getLiveTimeStatus()).getTimeStatus()) {
                case LIVE:
                    return LIVE_TIME;
                default:
                    return getFormattedExtraMinutes(event.getLiveTimeStatus().getExtraMinute());
            }
        }

        private void setCenterLayout() {
            whistleImage.setVisibility(View.GONE);
            verticalLine.setVisibility(View.VISIBLE);
            minuteView.setVisibility(View.VISIBLE);
            setMinute();
        }

        private void setMinute() {
            String minuteText = event.getMinute() + "'";
            minuteView.setText(minuteText);
        }
    }
}