package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.SignInModel;
import life.plank.juna.zone.util.ActivityUtil;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.StackAnimation;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    String TAG = SignInActivity.class.getCanonicalName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @Nullable
    @BindView(R.id.image_button)
    ImageView submitImageView;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.email_editext_sign_in)
    EditText emailEditTextSignIn;
    @BindView(R.id.password_editext_sign_in)
    EditText passwordEditTextSignIn;
    @BindView(R.id.card_view_sign_in)
    CardView cardViewSignIn;
    @BindView(R.id.card_view_sign_up)
    CardView cardViewSignUp;
    StackAnimation stackAnimation;
    String emailText;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ((ZoneApplication) getApplication()).getSignInUserNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        ButterKnife.bind(this);
        ActivityUtil.setCollapsedHintMiddle(usernameInputLayout, this);
        ActivityUtil.setCollapsedHintMiddle(passwordInputLayout, this);
        initStackAnimation();
    }

    private void initStackAnimation() {
        stackAnimation = new StackAnimation(AppConstants.ANIMATION_DURATION,
                AppConstants.ANIMATION_START_SCALE,
                AppConstants.ANIMATION_PIVOT_VALUE);
    }

    @OnClick({R.id.forgot_password, R.id.image_button, R.id.card_view_sign_in, R.id.card_view_sign_up})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.forgot_password:
                Intent intent = new Intent(SignInActivity.this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.swipe_up_animation, R.animator.no_change);
                break;
            case R.id.image_button:
                emailText = emailEditTextSignIn.getText().toString();
                if (emailText.isEmpty()) {
                    emailEditTextSignIn.setError(getString(R.string.username_empty));
                } else {
                    getSignInResponse(emailText);
                }
                break;
            case R.id.card_view_sign_in:
                // stackAnimation.animateStacks( cardViewSignIn, cardViewSignUp, AppConstants.ANIMATION_END_SCALE );
                break;
            case R.id.card_view_sign_up:
                // stackAnimation.animateStacks( cardViewSignUp, cardViewSignIn, AppConstants.ANIMATION_END_SCALE );
                break;
        }
    }

    private void getSignInResponse(String emailAddress) {
        restApi.getUser(emailAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<SignInModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong_500, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<SignInModel> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                //TODO: Investigate why the response.body is saved
                                UIDisplayUtil.saveSignInUserDetails(SignInActivity.this, response.body());
                                Intent intentSubmit = new Intent(SignInActivity.this, SwipePageActivity.class);
                                startActivity(intentSubmit);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.user_name_not_found, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

}
