package life.plank.juna.zone.view.activity.base;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import life.plank.juna.zone.data.model.League;

public abstract class BaseLeagueActivity extends SwipeableCardActivity {

    public abstract Picasso getPicasso();

    public abstract Gson getGson();

    public abstract League getLeague();
}