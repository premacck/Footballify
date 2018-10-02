package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.UserPreference;
import life.plank.juna.zone.view.activity.SwipePageActivity;

public class UserZoneAdapter extends RecyclerView.Adapter<UserZoneAdapter.UserZoneViewHolder> {
    private Context context;
    private List<UserPreference> userPreferenceList;

    public UserZoneAdapter(Context context, List<UserPreference> userPreferenceList) {
        this.context = context;
        this.userPreferenceList = userPreferenceList;
    }

    @Override
    public UserZoneAdapter.UserZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserZoneAdapter.UserZoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone_user_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(UserZoneAdapter.UserZoneViewHolder holder, int position) {
        UserPreference userPreference = userPreferenceList.get(position);
        holder.footballTextView.setText(userPreference.getZone().name);
        holder.followerCount.setText(String.valueOf(userPreference.getZone().followerCount));
        holder.totalPostCount.setText(String.valueOf(userPreference.getZone().contributionCount));
        holder.interaction_count.setText(String.valueOf(userPreference.getZone().interactionCount));
    }

    @Override
    public int getItemCount() {
        return userPreferenceList.size();
    }

    class UserZoneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.football)
        TextView footballTextView;
        @BindView(R.id.match_status)
        TextView matchStatusTextView;
        @BindView(R.id.match_between)
        TextView matchBetweenTextView;
        @BindView(R.id.follower_count)
        TextView followerCount;
        @BindView(R.id.total_post_count)
        TextView totalPostCount;
        @BindView(R.id.interaction_count)
        TextView interaction_count;

        UserZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //TODO: navigate to appropriate zone view
            itemView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, SwipePageActivity.class));
                //TODO: uncomment after onboarding is complete
                //  onboardingBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            });
        }
    }
}
