package life.plank.juna.zone.util;

public interface AuthenticationListener {

    void onCodeReceived(String authToken);

    void showProgressSpinner();

    void hideProgressSpinner();

}
