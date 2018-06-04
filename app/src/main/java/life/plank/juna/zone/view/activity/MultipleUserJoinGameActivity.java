package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Arena;
import life.plank.juna.zone.util.CustomizeStatusBar;
import life.plank.juna.zone.util.UIDisplayUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MultipleUserJoinGameActivity extends AppCompatActivity {

    @Inject
    @Named("feed")
    Retrofit retrofit;

    @BindView(R.id.invitation_code_label1)
    EditText invitationCodeLabelOne;
    @BindView(R.id.invitation_code_label2)
    EditText invitationCodeLabelTwo;
    @BindView(R.id.invitation_code_label3)
    EditText invitationCodeLabelThree;
    @BindView(R.id.invitation_code_label4)
    EditText invitationCodeLabelFour;
    @BindView(R.id.relative_layout_multiple_user_joingame)
    RelativeLayout relativeLayout;

    private static final String TAG = MultipleUserJoinGameActivity.class.getSimpleName();
    private static String invitationCode;
    private Subscription subscription;
    private RestApi restApi;
    private Arena arena;
    private String gameType;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_user_join_game);
        ButterKnife.bind(this);
        CustomizeStatusBar.setTransparentStatusBarColor(getTheme(), getWindow());
        sharedPreferences = getSharedPreferences(getString(R.string.login_pref), MODE_PRIVATE);

        ((ZoneApplication) getApplication()).getMultipleUserJoinGameNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        arena = Arena.getNullArena();

        RxTextView.textChangeEvents(invitationCodeLabelOne)
                .subscribe(event -> shiftCursorFocus(invitationCodeLabelOne));

        RxTextView.textChangeEvents(invitationCodeLabelTwo)
                .subscribe(event -> shiftCursorFocus(invitationCodeLabelTwo));

        RxTextView.textChangeEvents(invitationCodeLabelThree)
                .subscribe(event -> shiftCursorFocus(invitationCodeLabelThree));

        RxTextView.textChangeEvents(invitationCodeLabelFour)
                .subscribe(event -> shiftCursorFocus(invitationCodeLabelFour));
    }

    @OnClick(R.id.home_icon)
    public void exitJoinGame() {
        startActivity(new Intent(this, GameLaunchActivity.class));
    }

    @OnClick(R.id.submit_button)
    public void submitInvitationCode() {
        invitationCode = invitationCodeLabelOne.getText().toString()
                + invitationCodeLabelTwo.getText().toString()
                + invitationCodeLabelThree.getText().toString()
                + invitationCodeLabelFour.getText().toString();

        subscription = restApi.getArenaByInvitationCode(invitationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Arena>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In getArenaByInvitationCode onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In getArenaByInvitationCode onError: " + e.getMessage());
                        if (e.getMessage().equals(getString(R.string.not_found_error)))
                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.invalid_invitation_code));
                        else
                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.server_unreachable_message));
                    }

                    @Override
                    public void onNext(Arena responseArena) {
                        Log.d(TAG, "In getArenaByInvitationCode onNext");
                        gameType = responseArena.getGameType();
                        arena.copyArena(responseArena);
                        joinArena();
                    }
                });
    }

    private void joinArena() {
        subscription = restApi.putJoinArena(invitationCode,
                JunaUserBuilder.getInstance().
                        withUserName(sharedPreferences.getString(getString(R.string.shared_pref_username), ""))
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<Void> response) {
                        if (response.code() == HttpURLConnection.HTTP_ACCEPTED) {
                            Log.d(TAG, "Put Join Arena request Accepted");
                            Intent intent = new Intent(MultipleUserJoinGameActivity.this, JoinGameActivity.class);
                            intent.putExtra("invitationCode", invitationCode);
                            intent.putExtra(getString(R.string.game_type), gameType);
                            startActivity(intent);
                        } else
                            Snackbar.make(relativeLayout, "Please enter valid invitation code", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void shiftCursorFocus(EditText editText) {
        if (editText.length() == 1) {
            View next = editText.focusSearch(View.FOCUS_RIGHT);
            if (next == null) {
                UIDisplayUtil.getInstance().hideSoftKeyboard(editText, this);
            } else next.requestFocus();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
