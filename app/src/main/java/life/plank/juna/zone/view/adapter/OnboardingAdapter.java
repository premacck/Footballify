package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FootballTeam;
import life.plank.juna.zone.interfaces.OnClickZoneItemListener;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.FootballFeedViewHolder> {

    private List<FootballTeam> teamList;
    private Context context;
    private OnClickZoneItemListener onClickZoneItemListener;

    public OnboardingAdapter(Context activity, List<FootballTeam> teamList, OnClickZoneItemListener onClickZoneItemListener) {
        this.context = activity;
        this.teamList = teamList;
        this.onClickZoneItemListener = onClickZoneItemListener;
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false);
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        FootballTeam team = teamList.get(position);
        holder.title.setText(team.getName());
        Glide.with(context).load(team.getLogoLink())
                .apply(RequestOptions.centerInsideTransform()
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(holder.image);

        holder.cardView.setOnClickListener(view -> {
            ViewGroup.LayoutParams params = holder.cardView.getLayoutParams();
            if (holder.followTick.getVisibility() == View.VISIBLE) {
                holder.followTick.setVisibility(View.INVISIBLE);
                holder.cardView.setAlpha(context.getResources().getInteger(R.integer.opaque));
                holder.title.setVisibility(View.VISIBLE);

                params.width = holder.cardView.getMeasuredWidth() + context.getResources().getInteger(R.integer.team_grid_layout_width_param);
                params.height = holder.cardView.getMeasuredHeight() + context.getResources().getInteger(R.integer.team_grid_layout_height_param);
                onClickZoneItemListener.onItemClick(team.getName(), false);

            } else {
                holder.followTick.setVisibility(View.VISIBLE);
                holder.cardView.setAlpha(context.getResources().getInteger(R.integer.visiblilty_160));
                holder.title.setVisibility(View.INVISIBLE);

                params.width = holder.cardView.getMeasuredWidth() - context.getResources().getInteger(R.integer.team_grid_layout_width_param);
                params.height = holder.cardView.getMeasuredHeight() - context.getResources().getInteger(R.integer.team_grid_layout_height_param);
                onClickZoneItemListener.onItemClick(team.getName(), true);

            }
            holder.cardView.setLayoutParams(params);
        });
        //TODO: Set card colour after backend returns it
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void setTeamList(List<FootballTeam> footballTeams) {
        if (footballTeams == null) {
            return;
        }
        teamList.clear();
        teamList.addAll(footballTeams);
        notifyDataSetChanged();
    }

    static class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.follow_image_view)
        ImageView followTick;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.card)
        CardView cardView;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

