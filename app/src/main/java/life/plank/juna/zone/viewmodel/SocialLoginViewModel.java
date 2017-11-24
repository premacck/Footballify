package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.Profile;
import com.facebook.login.LoginResult;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.builder.JunaUserBuilder;
import life.plank.juna.zone.domain.service.AuthenticationService;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by plank-sobia on 11/21/2017.
 */

public class SocialLoginViewModel {

    private Context context;
    private AuthenticationService authenticationService;

    public SocialLoginViewModel(Context context, AuthenticationService authenticationService) {
        this.context = context;
        this.authenticationService = authenticationService;
    }

    public Observable<Response<Void>> registerFacebookUser(LoginResult loginResult) {
        return authenticationService.socialSignUp(JunaUserBuilder.getInstance()
                .withUserName(loginResult.getAccessToken().getUserId())
                .withPassword(loginResult.getAccessToken().getUserId())
                .withDisplayName(Profile.getCurrentProfile().getName())
                .withProvider(context.getString(R.string.facebook_string))
                .build());
    }

    public Observable<Response<Void>> loginFacebookUser(LoginResult loginResult) {
        return authenticationService.socialSignIn(JunaUserBuilder.getInstance()
                .withUserName(loginResult.getAccessToken().getUserId())
                .withPassword(loginResult.getAccessToken().getUserId())
                .build());
    }

    public void saveLoginDetails(LoginResult loginResult) {
        SharedPreferences.Editor loginPreferenceEditor = context.getSharedPreferences(context.getString(R.string.login_pref), Context.MODE_PRIVATE).edit();
        loginPreferenceEditor.putString(context.getString(R.string.shared_pref_username), loginResult.getAccessToken().getUserId())
                .putString(context.getString(R.string.shared_pref_password), loginResult.getAccessToken().getUserId())
                .apply();
    }
}
