package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import life.plank.juna.zone.R;

public class CustomPopup {

    static PopupWindow optionPopUp;

    public static void showOptionPopup(final Activity context, Point p, String popUpType, Long currentMatchId, int offsetX, int offsetY) {

        LinearLayout viewGroup = context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.menu_pop_up, viewGroup);

        if (popUpType.equals(context.getString(R.string.board_pop_up))) {
            TextView favText = layout.findViewById(R.id.fav);
            favText.setOnClickListener(view -> optionPopUp.dismiss());

            TextView notificationText = layout.findViewById(R.id.mute_notification);
            notificationText.setText(R.string.show_notification);
            notificationText.setOnClickListener(view -> optionPopUp.dismiss());

            TextView unfollowText = layout.findViewById(R.id.unfollow);
            unfollowText.setText(R.string.unfollow_board_popup);
            unfollowText.setOnClickListener(view -> {
                optionPopUp.dismiss();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(context.getResources().getString(R.string.pref_football_match_sub) + currentMatchId);
            });

            TextView reportText = layout.findViewById(R.id.report);
            reportText.setText(R.string.report_board_popup);
            reportText.setOnClickListener(view -> optionPopUp.dismiss());
        }

        // Creating the PopupWindow
        optionPopUp = new PopupWindow(context);
        optionPopUp.setContentView(layout);
        optionPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setFocusable(true);

        //Clear the default translucent background
        optionPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + offsetX, p.y + offsetY);
    }

}
