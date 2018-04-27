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
    private ArrayList<Integer> visitingTeamFormation;
    private ArrayList<Integer> homeTeamFormation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_up);
        ButterKnife.bind(this);
        setUpFormations();
        setWeightSumToVisitingTeamLinearLayout();
        setWeightSumToHomeTeamLinearLayout();
        setUpVisitingTeamGrid();
        setUpHomeTeamGrid();
    }

    private void setUpFormations(){
        visitingTeamFormation = new ArrayList<>();
        homeTeamFormation = new ArrayList<>();
        homeTeamFormation.add(1);
        homeTeamFormation.add(4);
        homeTeamFormation.add(3);
        homeTeamFormation.add(3);
        visitingTeamFormation.add(1);
        visitingTeamFormation.add(2);
        visitingTeamFormation.add(4);
        visitingTeamFormation.add(3);
        visitingTeamFormation.add(1);
    }

    private void setWeightSumToVisitingTeamLinearLayout(){
        visitingTeamLinearLayout.setWeightSum(visitingTeamFormation.size());
    }

    private void setWeightSumToHomeTeamLinearLayout(){
        homeTeamLinearLayout.setWeightSum(homeTeamFormation.size());
    }

    private void setUpVisitingTeamGrid(){
        for (Integer formationSegment : visitingTeamFormation) {
            LinearLayout visitingTeamLineUpLinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1);
            visitingTeamLineUpLinearLayout.setLayoutParams(layoutParams);
            visitingTeamLineUpLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            visitingTeamLinearLayout.addView(visitingTeamLineUpLinearLayout);
            for ( int j =1 ; j<= formationSegment; j++){
                visitingTeamLineUpLinearLayout.setWeightSum(formationSegment);
                View child = getLayoutInflater().inflate(R.layout.layout_line_up_text_view_visiting_team, null);
                child.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
                visitingTeamLineUpLinearLayout.addView(child);
            }
        }
    }

    private void setUpHomeTeamGrid(){
        for (Integer formationSegment : homeTeamFormation) {
            LinearLayout homeTeamLineUpLinearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,1);
            homeTeamLineUpLinearLayout.setLayoutParams(layoutParams);
            homeTeamLineUpLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            homeTeamLinearLayout.addView(homeTeamLineUpLinearLayout);
            for ( int j =1 ; j<= formationSegment; j++){
                homeTeamLineUpLinearLayout.setWeightSum(formationSegment);
                View playerView = getLayoutInflater().inflate(R.layout.layout_line_up_text_view_home_team, null);
                playerView.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT, 1f));
                homeTeamLineUpLinearLayout.addView(playerView);
            }
        }
    }
}
