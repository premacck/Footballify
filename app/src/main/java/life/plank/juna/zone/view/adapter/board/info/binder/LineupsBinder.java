package life.plank.juna.zone.view.adapter.board.info.binder;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.Formation;
import life.plank.juna.zone.data.model.FormationList;
import life.plank.juna.zone.data.model.binder.LineupsBindingModel;
import life.plank.juna.zone.util.customview.LineupPlayer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static life.plank.juna.zone.util.DataUtil.getIntegratedLineups;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getEndDrawableTargetPicasso;
import static life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTargetPicasso;

public class LineupsBinder extends ItemBinder<LineupsBindingModel, LineupsBinder.LineupsViewHolder> {

    private Activity activity;
    private Picasso picasso;

    public LineupsBinder(Activity activity, Picasso picasso) {
        this.activity = activity;
        this.picasso = picasso;
    }

    @Override
    public LineupsViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new LineupsViewHolder(inflater.inflate(R.layout.item_line_up, parent, false));
    }

    @Override
    public void bind(LineupsViewHolder holder, LineupsBindingModel item) {
        ExtraIconBinder.bind(this, holder, item);
    }

    private void bindViews(LineupsViewHolder holder, LineupsBindingModel item) {
        holder.progressBar.setVisibility(View.GONE);
        if (item.getErrorMessage() != null) {
            holder.lineupCenterLines.setVisibility(View.INVISIBLE);
            holder.noDataTextView.setVisibility(View.VISIBLE);
            holder.homeTeamLineupLayout.setVisibility(View.GONE);
            holder.visitingTeamLineupLayout.setVisibility(View.GONE);
            holder.noDataTextView.setText(item.getErrorMessage());
            return;
        }

        holder.lineupCenterLines.setVisibility(View.VISIBLE);
        holder.noDataTextView.setVisibility(View.GONE);
        holder.homeTeamLineupLayout.setVisibility(View.VISIBLE);
        holder.visitingTeamLineupLayout.setVisibility(View.VISIBLE);

        holder.homeTeamName.setText(item.getHomeTeam().getName());
        holder.visitingTeamName.setText(item.getAwayTeam().getName());
        if (!isNullOrEmpty(item.getLineups().getHomeTeamFormation()) && !isNullOrEmpty(item.getLineups().getAwayTeamFormation())) {
            holder.homeTeamLineup.setText(item.getHomeFormation());
            holder.visitingTeamLineup.setText(item.getAwayFormation());

            prepareLineup(holder.homeTeamLineupLayout, item.getLineups().getHomeTeamFormation(), activity.getColor(R.color.lineup_player_red), true);
            prepareLineup(holder.visitingTeamLineupLayout, item.getLineups().getAwayTeamFormation(), activity.getColor(R.color.purple), false);
        }
        Target homeTarget = getStartDrawableTargetPicasso(holder.homeTeamName);
        Target visitingTarget = getEndDrawableTargetPicasso(holder.visitingTeamName);

        loadImage(picasso, item.getHomeTeam().getLogoLink(), homeTarget);
        loadImage(picasso, item.getAwayTeam().getLogoLink(), visitingTarget);
    }

    private void loadImage(Picasso picasso, String logo, Target target) {
        picasso.load(logo)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .resize((int) getDp(14), (int) getDp(14))
                .into(target);
    }

    private void prepareLineup(LinearLayout lineupLayout, List<FormationList> formationsList, int labelColor, boolean isHomeTeam) {
        lineupLayout.removeAllViews();
        if (!isHomeTeam) {
            Collections.reverse(formationsList);
        }
        for (List<Formation> formations : formationsList) {
            lineupLayout.addView(getLineupLayoutLine(formations, labelColor));
        }
    }

    private LinearLayout getLineupLayoutLine(List<Formation> formations, int labelColor) {
        LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setWeightSum(formations.size());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(params);
        for (Formation formation : formations) {
            LineupPlayer lineupPlayer = new LineupPlayer(activity, formation, labelColor);
            linearLayout.addView(lineupPlayer);
            ((LinearLayout.LayoutParams) lineupPlayer.getLayoutParams()).weight = 1;
        }
        return linearLayout;
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof LineupsBindingModel;
    }

    static class ExtraIconBinder extends AsyncTask<Void, Void, Void> {

        private final WeakReference<LineupsBinder> ref;
        private final LineupsViewHolder holder;
        private final LineupsBindingModel lineupsBindingModel;

        static void bind(LineupsBinder lineupsBinder, LineupsViewHolder holder, LineupsBindingModel lineupsBindingModel) {
            new ExtraIconBinder(lineupsBinder, holder, lineupsBindingModel).execute();
        }

        private ExtraIconBinder(LineupsBinder lineupsBinder, LineupsViewHolder holder, LineupsBindingModel lineupsBindingModel) {
            ref = new WeakReference<>(lineupsBinder);
            this.holder = holder;
            this.lineupsBindingModel = lineupsBindingModel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            lineupsBindingModel.setLineups(getIntegratedLineups(lineupsBindingModel.getLineups(), lineupsBindingModel.getMatchEventList()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (ref.get() != null && holder != null && lineupsBindingModel.getLineups() != null) {
                ref.get().bindViews(holder, lineupsBindingModel);
            }
        }
    }

    static class LineupsViewHolder extends ItemViewHolder<LineupsBindingModel> {

        @BindView(R.id.home_team_name)
        TextView homeTeamName;
        @BindView(R.id.home_team_lineup_text)
        TextView homeTeamLineup;

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.no_data)
        TextView noDataTextView;
        @BindView(R.id.lineup_center_lines)
        ImageView lineupCenterLines;
        @BindView(R.id.home_team_lineup_layout)
        LinearLayout homeTeamLineupLayout;
        @BindView(R.id.visiting_team_lineup_layout)
        LinearLayout visitingTeamLineupLayout;

        @BindView(R.id.visiting_team_name)
        TextView visitingTeamName;
        @BindView(R.id.visiting_team_lineup_text)
        TextView visitingTeamLineup;

        LineupsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
