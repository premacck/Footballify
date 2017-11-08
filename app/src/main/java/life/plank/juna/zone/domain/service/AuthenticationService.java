package life.plank.juna.zone.domain.service;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.net.HttpURLConnection;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.JunaUser;
import life.plank.juna.zone.util.UIDisplayUtil;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class AuthenticationService {

    private static final String TAG = AuthenticationService.class.getSimpleName();
    private RestApi restApi;

    public AuthenticationService(Retrofit retrofit) {
        restApi = retrofit.create(RestApi.class);
    }

    public Observable<Boolean> login(JunaUser junaUser, Context context, View view) {
        return restApi.loginUser(junaUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        UIDisplayUtil.getInstance().displaySnackBar(view, context.getString(R.string.login_successful));
                    } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        UIDisplayUtil.getInstance().displaySnackBar(view, context.getString(R.string.invalid_credentials));
                    } else
                        UIDisplayUtil.getInstance().displaySnackBar(view, context.getString(R.string.login_failed_message));
                })
                .doOnError(throwable -> {
                    UIDisplayUtil.getInstance().displaySnackBar(view, context.getString(R.string.server_unreachable_message));
                    Log.d(TAG, "In onError: " + throwable.getLocalizedMessage());
                })
                .doOnCompleted(() -> Log.d(TAG, "In onCompleted"))
                .flatMap( voidResponse -> Observable.just(voidResponse.isSuccessful()));
    }
}
