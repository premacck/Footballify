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
//    private ArrayList<UserFeed> userFeed;

    public UserZoneAdapter(Context context) {
        this.context = context;
        //this.userFeed = userFeed;
        //, ArrayList<UserFeed> userFeed
    }

    @Override
    public UserZoneAdapter.UserZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserZoneAdapter.UserZoneViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zone_user_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(UserZoneAdapter.UserZoneViewHolder holder, int position) {

        holder.footballTextView.setText("Football");
        holder.matchStatusTextView.setText("Kick-off in 2hrs 13 mins");
        holder.matchbetweenTextView.setText("Bournemouth vs Man. United");
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class UserZoneViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.football)
        TextView footballTextView;
        @BindView(R.id.match_status)
        TextView matchStatusTextView;
        @BindView(R.id.match_between)
        TextView matchbetweenTextView;

        UserZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
