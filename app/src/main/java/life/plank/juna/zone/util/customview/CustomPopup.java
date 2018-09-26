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
import life.plank.juna.zone.view.activity.PrivateBoardActivity;

public class CustomPopup {

    private static PopupWindow optionPopUp;

    public static void showOptionPopup(final Activity context, Point p, String popUpType, Long currentMatchId, int offsetX, int offsetY) {

        LinearLayout viewGroup = context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.menu_pop_up, viewGroup);

        TextView popupItemOne = layout.findViewById(R.id.popup_item_one);
        popupItemOne.setOnClickListener(view -> optionPopUp.dismiss());

        TextView popUpItemTwo = layout.findViewById(R.id.popup_item_two);
        popUpItemTwo.setText(R.string.show_notification);
        popUpItemTwo.setOnClickListener(view -> optionPopUp.dismiss());

        TextView popupItemThree = layout.findViewById(R.id.popup_item_three);
        TextView popupItemFour = layout.findViewById(R.id.popup_item_four);

        if (popUpType.equals(context.getString(R.string.board_pop_up))) {

            popupItemThree.setText(R.string.unfollow_board_popup);
            popupItemThree.setOnClickListener(view -> {
                optionPopUp.dismiss();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(context.getResources().getString(R.string.pref_football_match_sub) + currentMatchId);
            });

            popupItemFour.setText(R.string.report_board_popup);
            popupItemFour.setOnClickListener(view -> optionPopUp.dismiss());


        } else if (popUpType.equals(context.getString(R.string.private_board_owner_popup))) {

            popupItemThree.setText(R.string.delete_board_popup);
            popupItemThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);

            popupItemThree.setOnClickListener(view -> {
                optionPopUp.dismiss();
                PrivateBoardActivity.deletePrivateBoard();
            });

            popupItemFour.setText(R.string.settings_board_popup);
            popupItemFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gear, 0, 0, 0);
            popupItemFour.setOnClickListener(view -> optionPopUp.dismiss());


        } else if (popUpType.equals(context.getString(R.string.private_board_user_popup))) {

            popupItemThree.setText(R.string.unfollow_board_popup);
            popupItemThree.setOnClickListener(view -> {
                optionPopUp.dismiss();
            });

            popupItemFour.setText(R.string.report_board_popup);
            popupItemFour.setOnClickListener(view -> optionPopUp.dismiss());
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


    public static void showPrivateBoardOptionPopup(View view, View fragmentRootView) {
        LinearLayout viewGroup = fragmentRootView.findViewById(R.id.owner_options_popup);
        LayoutInflater layoutInflater = (LayoutInflater) fragmentRootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.owner_options_for_admin_or_user_popup, viewGroup);


        int[] location = new int[2];

        view.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        Point point = new Point();
        point.x = location[0];
        point.y = location[1];

        // Displaying the popup at the specified location, + offsets.

        optionPopUp = new PopupWindow(fragmentRootView.getContext());
        optionPopUp.setContentView(layout);
        optionPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setFocusable(true);

        //Clear the default translucent background
        optionPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, point.x, point.y);
    }

    public static void showAdminOptionsPopup(View view, View fragmentRootView) {
        LinearLayout viewGroup = fragmentRootView.findViewById(R.id.admin_options_popup);
        LayoutInflater layoutInflater = (LayoutInflater) fragmentRootView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.admin_options_for_user_popup, viewGroup);


        int[] location = new int[2];

        view.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        Point point = new Point();
        point.x = location[0];
        point.y = location[1];

        // Displaying the popup at the specified location, + offsets.

        optionPopUp = new PopupWindow(fragmentRootView.getContext());
        optionPopUp.setContentView(layout);
        optionPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setFocusable(true);

        //Clear the default translucent background
        optionPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, point.x, point.y);
    }

}
