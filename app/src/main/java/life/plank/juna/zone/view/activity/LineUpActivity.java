package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class LineUpActivity extends AppCompatActivity {

    @BindView(R.id.visiting_team_linear_layout)
    LinearLayout visitingTeamLinearLayout;
    @BindView(R.id.home_team_linear_layout)
    LinearLayout homeTeamLinearLayout;
    private ArrayList<Integer> formationVisitingTeam;
    private ArrayList<Integer> formationHomeTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_up);
        ButterKnife.bind(this);
        setUpArrayList();
        setWeightSumToVisitingTeamLinearLayout();
        setWeightSumToHomeTeamLinearLayout();
        setUpVisitingTeamGrid();
        setUpHomeTeamGrid();
    }

    private void setUpArrayList(){
        formationVisitingTeam = new ArrayList<>();
        formationHomeTeam = new ArrayList<>();

        formationHomeTeam.add(1);
        formationHomeTeam.add(4);
        formationHomeTeam.add(3);
        formationHomeTeam.add(3);

        formationVisitingTeam.add(1);
        formationVisitingTeam.add(2);
        formationVisitingTeam.add(4);
        formationVisitingTeam.add(3);
        formationVisitingTeam.add(1);
    }

    private void setWeightSumToVisitingTeamLinearLayout(){
        visitingTeamLinearLayout.setWeightSum(formationVisitingTeam.size());
    }
    private void setWeightSumToHomeTeamLinearLayout(){
        homeTeamLinearLayout.setWeightSum(formationHomeTeam.size());
    }

    private void setUpVisitingTeamGrid(){
        for (Integer formation : formationVisitingTeam) {
            LinearLayout visitingTeamLinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1);
            visitingTeamLinearLayout.setLayoutParams(layoutParams);
            visitingTeamLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            this.visitingTeamLinearLayout.addView(visitingTeamLinearLayout);
            for ( int j =1 ; j<= formation; j++){
                visitingTeamLinearLayout.setWeightSum(formation);
                View child = getLayoutInflater().inflate(R.layout.layout_line_up_text_view_visiting_team, null);
                child.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
                visitingTeamLinearLayout.addView(child);
            }
        }
    }

    private void setUpHomeTeamGrid(){
        for (Integer formation : formationHomeTeam) {
            LinearLayout homeTeamLinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1);
            homeTeamLinearLayout.setLayoutParams(layoutParams);
            homeTeamLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            this.homeTeamLinearLayout.addView(homeTeamLinearLayout);
            for ( int j =1 ; j<= formation; j++){
                homeTeamLinearLayout.setWeightSum(formation);
                View child = getLayoutInflater().inflate(R.layout.layout_line_up_text_view_home_team, null);
                child.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
                homeTeamLinearLayout.addView(child);
            }
        }
    }
}
