package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.customview.ZoneToolBar;
import life.plank.juna.zone.view.adapter.NotificationAdapter;


public class UserNotificationActivity extends AppCompatActivity {
    private static final String TAG = UserNotificationActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    ZoneToolBar toolbar;
    @BindView(R.id.notification_recycler_view)
    RecyclerView notificationRecyclerView;
    NotificationAdapter notificationAdapter;
    private ArrayList<String> notificationArray = new ArrayList<>();

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, UserNotificationActivity.class));
    }

    //TODO: Remove after integrating with the backend
    private void populateNotificationArray() {
        notificationArray.add("PikaPerfect has accepted your invite.");
        notificationArray.add("AnnaBeautiful and 36 others has replied to your comment.");
        notificationArray.add("CoolRahlf and 5 others has liked your post.");
        notificationArray.add("PrettyPopo has replied to your comment");
        notificationArray.add("Xuan Xii  Cheng and 12 others has liked your comment.");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_notification);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);

        toolbar.setTitle(getString(R.string.notification));
        toolbar.setProfilePic(PreferenceManager.CurrentUser.getProfilePicUrl());
        populateNotificationArray();
        initRecyclerView();
    }

    private void initRecyclerView() {
        notificationAdapter = new NotificationAdapter(this, notificationArray);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationRecyclerView.setAdapter(notificationAdapter);

    }
}