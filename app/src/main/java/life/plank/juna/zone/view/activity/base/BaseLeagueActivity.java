package life.plank.juna.zone.view.activity.base;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import life.plank.juna.zone.data.network.model.League;

public abstract class BaseLeagueActivity extends AppCompatActivity {

    public abstract Picasso getPicasso();

    public abstract Gson getGson();

    public abstract League getLeague();
}