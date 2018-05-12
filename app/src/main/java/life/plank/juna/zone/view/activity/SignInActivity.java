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

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.ActivityUtil;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.helper.StackAnimation;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @Nullable
    @BindView(R.id.submit_button)
    ImageView submitImageview;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.email_editext__sign_in)
    EditText emailEditTextSignIn;
    @BindView(R.id.password_editext_sign_in)
    EditText passwordEditTextSignIn;
    @BindView(R.id.card_view_sign_in)
    CardView cardViewSignIn;
    @BindView(R.id.card_view_sign_up)
    CardView cardViewSignUp;
    StackAnimation stackAnimation;
    private RestApi restApi;
    private Subscriber subscriber;
    String passwordText,emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        ActivityUtil.setCollapsedHintMiddle(usernameInputLayout, this);
        ActivityUtil.setCollapsedHintMiddle(passwordInputLayout, this);
        initStackAnimation();
         passwordText = passwordEditTextSignIn.getText().toString();
         emailText = emailEditTextSignIn.getText().toString();
    }

    private void initStackAnimation() {
        stackAnimation = new StackAnimation(AppConstants.ANIMATION_DURATION,
                AppConstants.ANIMATION_START_SCALE,
                AppConstants.ANIMATION_PIVOT_VALUE);
    }

    @OnClick({R.id.forgot_password, R.id.card_view_sign_in, R.id.card_view_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_password:
                Intent intent = new Intent(SignInActivity.this,AuthForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.swipe_up_animation,R.animator.no_change);
                break;
            case R.id.submit_button:
                if(emailText.isEmpty())
                {
                    emailEditTextSignIn.setError( "Enter Valid Email Address" );
                }
                else {
                    getSignInResponse();
                }
                break;
            case R.id.card_view_sign_in:
                stackAnimation.animateStacks(cardViewSignIn, cardViewSignUp, AppConstants.ANIMATION_END_SCALE);
                break;
            case R.id.card_view_sign_up:
                stackAnimation.animateStacks(cardViewSignUp, cardViewSignIn, AppConstants.ANIMATION_END_SCALE);
                break;
        }
    }


    private void getSignInResponse() {
        restApi.getSignIn("emailId")
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "onCompleted: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e( "", "onError: " + e );

                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        Log.e( "", "onNext: "+jsonObjectResponse );
                        Intent intentSubmit = new Intent( SignInActivity.this, SwipePageActivity.class );
                        startActivity( intentSubmit );

                    }

                } );
    }

}
