package life.plank.juna.zone.view.adapter;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
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
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.model.UserChoice;
import life.plank.juna.zone.util.TeamNameMap;

/**
 * Created by plank-sobia on 12/5/2017.
 */

public class SuddenDeathResultAdapter extends RecyclerView.Adapter<SuddenDeathResultAdapter.ViewHolder> {

    private Integer index = 1;
    private List<UserChoice> userChoiceList = new ArrayList<>();
    private String selectedTeamName;
    private Typeface aileronRegular = Typeface.createFromAsset(ZoneApplication.getContext().getAssets(),
            ZoneApplication.getContext().getString(R.string.aileron_regular));

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.index_label)
        TextView indexLabel;
        @BindView(R.id.user_name)
        TextView userName;
        @BindView(R.id.selected_team_logo)
        ImageView selectedTeamLogo;
        @BindView(R.id.result)
        TextView result;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            indexLabel.setTypeface(aileronRegular);
            userName.setTypeface(aileronRegular);
            result.setTypeface(aileronRegular);
        }
    }

    @Override
    public SuddenDeathResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sudden_death_result_row, parent, false);
        TeamNameMap.HashMaps(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuddenDeathResultAdapter.ViewHolder holder, int position) {
        UserChoice userChoice = userChoiceList.get(position);

        holder.indexLabel.setText(String.valueOf(index));
        index++;

        holder.userName.setText(JunaUserBuilder.getInstance()
                .withUserName(userChoice.getJunaUser().getUsername())
                .build()
                .getDisplayName());

        holder.selectedTeamLogo.setImageDrawable(TeamNameMap.getTeamNameMap().get(selectedTeamName));

        if (ZoneApplication.suddenDeathGameResultMap.get(userChoice.getJunaUser())) {
            holder.result.setText(ZoneApplication.getContext().getString(R.string.result_won));
            holder.result.setBackgroundColor(ContextCompat.getColor(ZoneApplication.getContext(), R.color.Green));
        } else {
            holder.result.setText(ZoneApplication.getContext().getString(R.string.result_lost));
            holder.result.setBackgroundColor(ContextCompat.getColor(ZoneApplication.getContext(), R.color.Red));
        }
    }

    @Override
    public int getItemCount() {
        return userChoiceList.size();
    }

    public void setUserChoiceList(List<UserChoice> userChoices, String selectedTeamName) {
        if (userChoices == null) {
            return;
        }
        this.selectedTeamName = selectedTeamName;
        userChoiceList.clear();
        userChoiceList.addAll(userChoices);
        notifyDataSetChanged();
    }
}