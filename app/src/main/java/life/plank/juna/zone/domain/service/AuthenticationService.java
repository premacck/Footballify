package life.plank.juna.zone.domain.service;

import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.JunaUser;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by plank-sobia on 11/8/2017.
 */

public class AuthenticationService {

    private RestApi restApi;

    public AuthenticationService(Retrofit retrofit) {
        restApi = retrofit.create(RestApi.class);
    }

    public Observable<Response<Void>> login(JunaUser junaUser) {
        return restApi.loginUser(junaUser);
    }

    public Observable<Response<Void>> register(JunaUser junaUser) {
        return restApi.registerUser(junaUser);
    }

    public Observable<Response<Void>> socialSignIn(JunaUser junaUser) {
        return restApi.socialSignIn(junaUser);
    }

    public Observable<Response<Void>> socialSignUp(JunaUser junaUser) {
        return restApi.socialSignUp(junaUser);
    }
}
