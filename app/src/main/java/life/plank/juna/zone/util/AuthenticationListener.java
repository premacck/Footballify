package life.plank.juna.zone.util;

public interface AuthenticationListener {

    void onCodeReceived(String auth_token);

    void showProgressSpinner();

    void hideProgressSpinner();

}
