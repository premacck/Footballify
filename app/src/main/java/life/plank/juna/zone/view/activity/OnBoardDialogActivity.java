package life.plank.juna.zone.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.HttpURLConnection;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HttpsURLConnection;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.AuthenticationListener;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.viewmodel.SocialLoginViewModel;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-hasan on 2/8/2018.
 */

public class OnBoardDialogActivity extends AppCompatActivity implements View.OnClickListener, AuthenticationListener {


    TextView titleTextView;
    ImageView closeDialogImage;
    AutoCompleteTextView teamOneEditText;
    AutoCompleteTextView teamTwoEditText;
    AutoCompleteTextView teamThreeEditText;
    Button applyButton;
    LinearLayout selectTeamLinearLayout;
    EditText emailEditText;
    EditText passwordEditText;
    TextView countryNameTextView;
    ImageView twitterIcon;
    ImageView facebookIcon;
    ImageView registerIcon;
    LinearLayout registerAndSaveLinearLayout;
    LinearLayout parentLinearLayout;
    private Dialog dialog;
    private TwitterAuthClient twitterAuthClient;
    private CallbackManager callbackManager;
    private AVLoadingIndicatorView spinner;
    private static final String TAG = OnBoardDialogActivity.class.getSimpleName();
    private Subscription subscription;
    private SocialLoginViewModel socialLoginViewModel;
    @Inject
    @Named("default")
    Retrofit retrofit;

    @Inject
    @Named("instagram")
    Retrofit instagramRetrofit;
    private static int RC_SIGN_IN = 100;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);


        ((ZoneApplication) this.getApplication()).getOnBoardSocialLoginNetworkComponent().inject(this);


        socialLoginViewModel = new SocialLoginViewModel(OnBoardDialogActivity.this, new AuthenticationService(retrofit, instagramRetrofit));

    }

    public void showOnboardingDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(this, R.layout.dialog_onboarding_user, null);
        dialog.setContentView(view);
        setUpViews();
        setAutoCompleteData();
        dialog.show();
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }


    private void setUpViews() {
        applyButton = dialog.findViewById(R.id.apply_button);
        closeDialogImage = dialog.findViewById(R.id.close_dialog_image);
        selectTeamLinearLayout = dialog.findViewById(R.id.select_team_linear_layout);
        registerAndSaveLinearLayout = dialog.findViewById(R.id.register_and_save_linear_layout);
        titleTextView = dialog.findViewById(R.id.title_text_view);
        teamOneEditText = dialog.findViewById(R.id.team_one_edit_text);
        teamTwoEditText = dialog.findViewById(R.id.team_two_edit_text);
        teamThreeEditText = dialog.findViewById(R.id.team_three_edit_text);
        twitterIcon = dialog.findViewById(R.id.twitter_icon);
        facebookIcon = dialog.findViewById(R.id.facebook_icon);
        spinner = dialog.findViewById(R.id.social_signup_spinner);
        parentLinearLayout = dialog.findViewById(R.id.parent_linear_layout);
        applyButton.setOnClickListener(this);
        closeDialogImage.setOnClickListener(this);
        twitterIcon.setOnClickListener(this);
        facebookIcon.setOnClickListener(this);
    }

    private void setAutoCompleteData() {
        teamOneEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.football_teams)));
        teamTwoEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.football_teams)));
        teamThreeEditText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.football_teams)));

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_button: {
                setUpRegisterAndSaveView();
                break;
            }
            case R.id.close_dialog_image: {
                dialog.dismiss();
                break;
            }
            case R.id.facebook_icon: {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.d(TAG, "In onSuccess");
                                if (loginResult.getRecentlyGrantedPermissions() != null)
                                    registerUser(loginResult.getAccessToken().getUserId(),
                                            Profile.getCurrentProfile().getName(),
                                            getString(R.string.facebook_string));
                            }

                            @Override
                            public void onCancel() {
                                Log.d(TAG, "In onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                if (error instanceof FacebookAuthorizationException) {
                                    if (AccessToken.getCurrentAccessToken() != null) {
                                        LoginManager.getInstance().logOut();
                                    }
                                } else {
                                    Log.d(TAG, "In onError: " + error.getMessage());
                                    showToast(getString(R.string.facebook_login_failed));
                                }
                            }
                        });
                break;
            }
            case R.id.twitter_icon: {
                twitterAuthClient = new TwitterAuthClient();
                twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterResponse) {
                        Log.d(TAG, "In onSuccess");
                        if (twitterResponse.data.getAuthToken() != null) {
                            showProgressSpinner();
                            registerUser(twitterResponse.data.getUserName(),
                                    twitterResponse.data.getUserName(),
                                    getString(R.string.twitter_string));
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d(TAG, "In onError: " + e.getMessage());
                        showToast(getString(R.string.twitter_login_failed));
                    }
                });
                break;
            }
        }
    }

    private void registerUser(String userName, String displayName, String provider) {
        subscription = socialLoginViewModel.registerUser(userName, displayName, provider)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    socialLoginViewModel.saveLoginDetails(userName, userName);
                    //Todo: Change 422 response code to 409 when backend issue - JUNA-921 is fixed
                    if (response.code() == HttpsURLConnection.HTTP_CREATED || response.code() == getResources().getInteger(R.integer.unprocessable_entity_response_code) || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        subscription = socialLoginViewModel.loginUser(userName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(voidResponse -> {
                                    if (voidResponse.code() == HttpsURLConnection.HTTP_OK || voidResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                        dialog.dismiss();
                                        hideProgressSpinner();
                                    } else {
                                        UIDisplayUtil.getInstance().displaySnackBar(parentLinearLayout, getString(R.string.login_failed_message));
                                        hideProgressSpinner();
                                        Log.d(TAG, String.valueOf(voidResponse.code()));
                                    }
                                }, throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
                    } else {
                        hideProgressSpinner();
                        UIDisplayUtil.getInstance().displaySnackBar(parentLinearLayout, getString(R.string.login_failed_message));
                        Log.d(TAG, String.valueOf(response.code()));
                    }
                }, throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
    }


    private void setUpRegisterAndSaveView() {
        selectTeamLinearLayout.setVisibility(View.GONE);
        registerAndSaveLinearLayout.setVisibility(View.VISIBLE);
        titleTextView.setText(getString(R.string.register_and_save));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //handleGoogleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCodeReceived(String auth_token) {

    }

    @Override
    public void showProgressSpinner() {
        spinner.show();
    }

    @Override
    public void hideProgressSpinner() {
        spinner.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}