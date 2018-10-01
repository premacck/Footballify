package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.League;
import life.plank.juna.zone.util.GlobalVariable;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.FootballFeedViewHolder> {

    private List<League> leagueList;
    private Context context;

    public OnboardingAdapter(Context activity) {
        this.context = activity;
        this.leagueList = new ArrayList<>();
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        League league = leagueList.get(position);
        holder.title.setText(league.getName());
        holder.image.setImageDrawable(context.getResources().getDrawable(league.getLeagueLogo()));
        holder.itemView.setOnClickListener(view -> {
            GlobalVariable.getInstance().setTilePosition(position);
        });
    }

    @Override
    public int getItemCount() {
        return leagueList.size();
    }

    public void setLeagueList(List<League> footballFeeds) {
        if (footballFeeds == null) {
            return;
        }
        leagueList.addAll(footballFeeds);
        notifyDataSetChanged();
    }

    static class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

