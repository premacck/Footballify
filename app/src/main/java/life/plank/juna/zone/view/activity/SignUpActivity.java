package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alirezaahmadi.progressbutton.ProgressButtonComponent;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.RxHelper;
import life.plank.juna.zone.viewmodel.SignUpViewModel;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SignUpActivity extends AppCompatActivity {

    @Inject
    Retrofit retrofit;

    private static final String TAG = SignUpActivity.class.getSimpleName();
    @BindView(R.id.text_input_user_name)
    TextInputLayout userNameTextInput;
    @BindView(R.id.text_input_first_name)
    TextInputLayout firstNameTextInput;
    @BindView(R.id.text_input_last_name)
    TextInputLayout lastNameTextInput;
    @BindView(R.id.text_input_password)
    TextInputLayout passwordTextInput;
    @BindView(R.id.text_input_confirm_password)
    TextInputLayout confirmPasswordTextInput;
    @BindView(R.id.input_user_name)
    EditText userName;
    @BindView(R.id.input_first_name)
    EditText firstName;
    @BindView(R.id.input_last_name)
    EditText lastName;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.input_confirm_password)
    EditText confirmPassword;
    @BindView(R.id.button_sign_up)
    ProgressButtonComponent signUpButton;
    @BindView(R.id.signup_relative_layout)
    RelativeLayout relativeLayout;

    private Subscription subscription;
    private Boolean validUserDetails = false;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Orange));
        }

        subscription = new CompositeSubscription();
        ((ZoneApplication) getApplication()).getRegisterNetworkComponent().inject(this);
        signUpViewModel = new SignUpViewModel(this, new AuthenticationService(retrofit));
        validateUserDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        if (validUserDetails) {
            showProgressBar();
            UIDisplayUtil.getInstance().hideSoftKeyboard(relativeLayout, this);

            subscription = signUpViewModel.registerUser(userName.getText().toString().trim(), password.getText().toString().trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                                switch (response.code()) {
                                    case HttpURLConnection.HTTP_CREATED:
                                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.sign_up_successful));
                                        startSignInActivity();
                                        break;
                                    //Todo: Change this response code to 409 when backend issue - JUNA-921 is fixed
                                    case 422:
                                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.user_exists));
                                        hideProgressBar();
                                        break;
                                    default:
                                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.signup_failed_message));
                                        hideProgressBar();
                                }
                            },
                            throwable -> {
                                UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.server_unreachable_message));
                                Log.d(TAG, "In onError: " + throwable.getLocalizedMessage());
                                hideProgressBar();
                            });
        }
    }

    private void startSignInActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.text_sign_in)
    public void signInHere() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void validateUserDetails() {
        Observable<String> userNameObservable = RxHelper.getTextWatcherObservable(userName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        Observable<String> firstNameObservable = RxHelper.getTextWatcherObservable(firstName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        Observable<String> lastNameObservable = RxHelper.getTextWatcherObservable(lastName)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        Observable<String> passwordObservable = RxHelper.getTextWatcherObservable(password)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        Observable<String> confirmPasswordObservable = RxHelper.getTextWatcherObservable(confirmPassword)
                .debounce(getResources().getInteger(R.integer.debounce_time), TimeUnit.MILLISECONDS);

        subscription = signUpViewModel.validateUserDetails(userNameObservable, firstNameObservable, lastNameObservable, passwordObservable, confirmPasswordObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::enableSignUpButton,
                        throwable -> Log.d(TAG, "In onError " + throwable.getMessage()),
                        () -> Log.d(TAG, "In onCompleted"));

        subscription = signUpViewModel.getUserNameObservable()
                .subscribe(s -> setUsernameErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));

        subscription = signUpViewModel.getFirstNameObservable()
                .subscribe(s -> setFirstNameErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));

        subscription = signUpViewModel.getLastNameObservable()
                .subscribe(s -> setLastNameErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));

        subscription = signUpViewModel.getPasswordObservable()
                .subscribe(s -> setPasswordErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));

        subscription = signUpViewModel.getConfirmPasswordObservable()
                .subscribe(s -> setConfirmPasswordErrorMessage(s.getReason()),
                        throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
    }

    private void enableSignUpButton(Boolean aBoolean) {
        signUpButton.setEnabled(aBoolean);
        validUserDetails = aBoolean;
        if (aBoolean)
            signUpButton.setAlpha(1);
        else
            signUpButton.setAlpha(0.5f);
    }

    private void hideProgressBar() {
        signUpButton.setInProgress(false);
        signUpButton.setTextSize(40);
    }

    private void showProgressBar() {
        signUpButton.setInProgress(true);
        signUpButton.setTextSize(0);
    }

    public void setUsernameErrorMessage(String reason) {
        this.userNameTextInput.setError(reason);
    }

    public void setFirstNameErrorMessage(String reason) {
        this.firstNameTextInput.setError(reason);
    }

    public void setLastNameErrorMessage(String reason) {
        this.lastNameTextInput.setError(reason);
    }

    public void setPasswordErrorMessage(String reason) {
        this.passwordTextInput.setError(reason);
    }

    public void setConfirmPasswordErrorMessage(String reason) {
        this.confirmPasswordTextInput.setError(reason);
    }
}
