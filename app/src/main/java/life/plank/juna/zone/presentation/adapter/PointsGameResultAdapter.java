package life.plank.juna.zone.presentation.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.UserChoice;

/**
 * Created by plank-sobia on 10/12/2017.
 */

public class PointsGameResultAdapter extends RecyclerView.Adapter<PointsGameResultAdapter.ViewHolder> {

    private Integer index = 1;
    private List<UserChoice> userChoiceList = new ArrayList<>();
    Typeface aileronRegular = Typeface.createFromAsset(ZoneApplication.getContext().getAssets(),
            ZoneApplication.getContext().getString(R.string.aileron_regular));

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.index_label)
        TextView indexLabel;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.user_points)
        TextView points;
        @BindView(R.id.result)
        TextView result;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            indexLabel.setTypeface(aileronRegular);
            userName.setTypeface(aileronRegular);
            points.setTypeface(aileronRegular);
            result.setTypeface(aileronRegular);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points_game_result_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserChoice userChoice = userChoiceList.get(position);
        holder.indexLabel.setText(index.toString());
        index++;
        holder.userName.setText(userChoice.getJunaUser().getUsername());
        Integer points = userChoice.getPoints();
        holder.points.setText(points.toString());
        if (ZoneApplication.pointsGameResultMap.get(userChoice.getJunaUser())) {
            holder.result.setText(ZoneApplication.getContext().getString(R.string.result_won));
            holder.result.setBackgroundColor(ZoneApplication.getContext().getColor(R.color.Green));
        }else {
            holder.result.setText(ZoneApplication.getContext().getString(R.string.result_lost));
            holder.result.setBackgroundColor(ZoneApplication.getContext().getColor(R.color.Red));
        }
    }

    @Override
    public int getItemCount() {
        return userChoiceList.size();
    }

    public void setUserChoiceList(List<UserChoice> userChoices) {
        if (userChoices == null ) {
            return;
        }
        userChoiceList.clear();
        userChoiceList.addAll(userChoices);
        notifyDataSetChanged();
    }
}
