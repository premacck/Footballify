package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.RxHelper;
import life.plank.juna.zone.viewmodel.LoginViewModel;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.text_input_user_name)
    TextInputLayout userNameTextInput;
    @BindView(R.id.text_input_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.input_user_name)
    EditText userName;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.button_sign_in)
    Button signInButton;
    @BindView(R.id.login_relative_layout)
    RelativeLayout relativeLayout;

    private Subscription subscription;
    private LoginViewModel loginViewModel;
    private Boolean validUserDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Orange));
        }

        subscription = new CompositeSubscription();
        ((ZoneApplication) getApplication()).getLoginNetworkComponent().inject(this);
        loginViewModel = new LoginViewModel(new AuthenticationService(retrofit));

        validateLoginDetails();
        subscribeToLoginSubject();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.button_sign_in)
    public void signIn() {
        if (validUserDetails) {
            UIDisplayUtil.getInstance().hideSoftKeyboard(relativeLayout, this);

            PreferenceManager prefManager = new PreferenceManager(this);
            prefManager.saveString(getString(R.string.shared_pref_username), userName.getText().toString().trim());

            loginViewModel.loginUser(userName.getText().toString().trim(), password.getText().toString().trim(), this, relativeLayout);
        }
    }

    @OnClick(R.id.text_sign_up)
    public void signUpHere() {
        startActivity(new Intent(this, SocialLoginActivity.class));
    }

    private void validateLoginDetails() {
        Observable<String> userNameObservable = RxHelper.getTextWatcherObservable(userName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        Observable<String> passwordObservable = RxHelper.getTextWatcherObservable(password)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        subscription = loginViewModel.validateUserDetails(this, userNameObservable, passwordObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::enableSignInButton,
                        throwable -> Log.d(TAG, "In onError " + throwable.getMessage()),
                        () -> Log.d(TAG, "In onCompleted"));

        subscription = loginViewModel.getUsernameObservable()
                .subscribe(s -> setUsernameErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));

        subscription = loginViewModel.getPasswordObservable()
                .subscribe(s -> setPasswordErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
    }

    private void subscribeToLoginSubject() {
        subscription = loginViewModel.getLoginSubject()
                .subscribe(aBoolean -> {
                            if (aBoolean.equals(true))
                                startHomeActivity();
                        },
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
    }

    private void startHomeActivity() {
        startActivity(new Intent(this, ZoneHomeActivity.class));
    }

    private void enableSignInButton(Boolean aBoolean) {
        signInButton.setEnabled(aBoolean);
        validUserDetails = aBoolean;
    }

    public void setUsernameErrorMessage(String reason) {
        this.userNameTextInput.setError(reason);
    }

    public void setPasswordErrorMessage(String reason) {
        this.passwordTextInput.setError(reason);
    }
}