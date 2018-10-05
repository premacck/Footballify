package life.plank.juna.zone.view.adapter.multiview.binder;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FootballTeam;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.view.adapter.SubstitutionAdapter;
import lombok.Data;

import static life.plank.juna.zone.util.DataUtil.extractSubstitutionEvents;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class SubstitutionBinder extends ItemBinder<SubstitutionBinder.SubstitutionBindingModel, SubstitutionBinder.SubstitutionViewHolder> {

    private Picasso picasso;

    public SubstitutionBinder(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public SubstitutionViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new SubstitutionViewHolder(inflater.inflate(R.layout.item_substitution_layout, parent, false));
    }

    @Override
    public void bind(SubstitutionViewHolder holder, SubstitutionBindingModel item) {
        if (item.getErrorMessage() != null || isNullOrEmpty(item.getSubstitutionEvents())) {
            holder.noSubstitutionsYet.setText(item.getErrorMessage());
            holder.noSubstitutionsYet.setVisibility(View.VISIBLE);
            holder.substitutionRecyclerView.setVisibility(View.GONE);
            return;
        }

        holder.noSubstitutionsYet.setVisibility(View.GONE);
        holder.substitutionRecyclerView.setVisibility(View.VISIBLE);

        SubstitutionAdapter adapter = new SubstitutionAdapter();
        holder.substitutionRecyclerView.setAdapter(adapter);
        adapter.update(item.getSubstitutionEvents());

        loadImage(picasso, item.getHomeTeam().getLogoLink(), holder.homeTeamLogo);
        loadImage(picasso, item.getAwayTeam().getLogoLink(), holder.visitingTeamLogo);
        loadImage(picasso, item.getHomeTeam().getLogoLink(), holder.homeTeamLogoUnderManager);
        loadImage(picasso, item.getAwayTeam().getLogoLink(), holder.visitingTeamLogoUnderManager);
    }

    private void loadImage(Picasso picasso, String logo, ImageView target) {
        picasso.load(logo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize((int) getDp(14), (int) getDp(14))
                .into(target);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof SubstitutionBindingModel;
    }

    static class SubstitutionViewHolder extends ItemViewHolder<SubstitutionBindingModel> {

        @BindView(R.id.substitution_recycler_view)
        RecyclerView substitutionRecyclerView;
        @BindView(R.id.no_substitutions_yet)
        TextView noSubstitutionsYet;
        @BindView(R.id.home_team_logo)
        ImageView homeTeamLogo;
        @BindView(R.id.visiting_team_logo)
        ImageView visitingTeamLogo;
        @BindView(R.id.home_team_logo_under_manager)
        ImageView homeTeamLogoUnderManager;
        @BindView(R.id.visiting_team_logo_under_manager)
        ImageView visitingTeamLogoUnderManager;
        @BindView(R.id.home_player_name)
        TextView homeManager;
        @BindView(R.id.visiting_player_name)
        TextView visitingManager;

        SubstitutionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Data
    public static class SubstitutionBindingModel {
        private final List<MatchEvent> substitutionEvents;
        private final FootballTeam homeTeam;
        private final FootballTeam awayTeam;
        @StringRes
        private final Integer errorMessage;

        public static SubstitutionBindingModel from(MatchDetails matchDetails) {
            List<MatchEvent> substitutionEvents = extractSubstitutionEvents(matchDetails.getMatchEvents());
            return new SubstitutionBindingModel(
                    substitutionEvents,
                    matchDetails.getHomeTeam(),
                    matchDetails.getAwayTeam(),
                    isNullOrEmpty(substitutionEvents) ? R.string.no_substitutions_yet : null
            );
        }
    }
}
