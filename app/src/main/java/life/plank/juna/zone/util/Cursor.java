package life.plank.juna.zone.util;

import android.app.Service;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import life.plank.juna.zone.ZoneApplication;

public class Cursor {

    public static void shiftCursorFocus(EditText editText) {
        if (editText.length() == 1) {
            View next = editText.focusSearch(View.FOCUS_RIGHT);
            if (next == null) {
                InputMethodManager inputMethodManager = (InputMethodManager) ZoneApplication.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } else next.requestFocus();
        }
    }
}