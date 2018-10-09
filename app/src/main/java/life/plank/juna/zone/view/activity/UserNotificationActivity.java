package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.customview.ZoneToolBar;


public class UserNotificationActivity extends AppCompatActivity {
    private static final String TAG = UserNotificationActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    ZoneToolBar toolbar;

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, UserNotificationActivity.class));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_notification);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);

        toolbar.setTitle(getString(R.string.notification));
    }

}