package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class UserZoneAdapter extends RecyclerView.Adapter<UserZoneAdapter.UserZoneViewHolder> {
    private Context context;

    public UserZoneAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UserZoneAdapter.UserZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserZoneAdapter.UserZoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone_user_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(UserZoneAdapter.UserZoneViewHolder holder, int position) {
        //TODO: Set with data provided by backend
    }

    @Override
    public int getItemCount() {
        //TODO: Remove hardcoded value after data is returned from the backend
        return 4;
    }

    class UserZoneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.football)
        TextView footballTextView;
        @BindView(R.id.match_status)
        TextView matchStatusTextView;
        @BindView(R.id.match_between)
        TextView matchBetweenTextView;

        UserZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
