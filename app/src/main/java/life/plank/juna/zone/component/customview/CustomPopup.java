package life.plank.juna.zone.component.customview;

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

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.board.fragment.user.PrivateBoardInfoFragment;

import static life.plank.juna.zone.service.CommonDataService.findString;
import static life.plank.juna.zone.sharedpreference.SubscriptionPreferenceKt.isSubscribed;
import static life.plank.juna.zone.sharedpreference.SubscriptionPreferenceKt.subscribeTo;
import static life.plank.juna.zone.sharedpreference.SubscriptionPreferenceKt.unsubscribeFrom;
import static life.plank.juna.zone.util.common.AppConstants.BOARD_POPUP;
import static life.plank.juna.zone.util.common.AppConstants.HOME_POPUP;
import static life.plank.juna.zone.util.common.AppConstants.PRIVATE_BOARD_OWNER_POPUP;
import static life.plank.juna.zone.util.common.AppConstants.PRIVATE_BOARD_USER_POPUP;

public class CustomPopup {

    private static PopupWindow optionPopUp;

    public static void showOptionPopup(final Activity context, Point p, String popUpType, Long currentMatchId, int offsetX, int offsetY, View.OnClickListener... deletePrivateBoardListeners) {

        LinearLayout viewGroup = context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.menu_pop_up, viewGroup);

        TextView popupItemOne = layout.findViewById(R.id.popup_item_one);
        popupItemOne.setOnClickListener(view -> optionPopUp.dismiss());

        TextView popUpItemTwo = layout.findViewById(R.id.popup_item_two);
        boolean isSubscribed = isSubscribed(findString(R.string.pref_football_match_sub) + currentMatchId);
        popUpItemTwo.setCompoundDrawablesWithIntrinsicBounds(
                isSubscribed ?
                        R.drawable.ic_mute_bell_grey :
                        R.drawable.ic_notification_bell,
                0, 0, 0
        );
        popUpItemTwo.setText(isSubscribed ? R.string.mute_notification : R.string.show_notification);
        popUpItemTwo.setOnClickListener(view -> {
            if (isSubscribed) {
                unsubscribeFrom(findString(R.string.pref_football_match_sub) + currentMatchId);
            } else subscribeTo(findString(R.string.pref_football_match_sub) + currentMatchId);
            optionPopUp.dismiss();
        });

        TextView popupItemThree = layout.findViewById(R.id.popup_item_three);
        TextView popupItemFour = layout.findViewById(R.id.popup_item_four);

        switch (popUpType) {
            case BOARD_POPUP:
                popupItemThree.setVisibility(View.GONE);
                popupItemFour.setVisibility(View.GONE);
                break;
            case PRIVATE_BOARD_OWNER_POPUP:
                popupItemThree.setText(R.string.delete_board_popup);
                popupItemThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);

                if (deletePrivateBoardListeners != null && deletePrivateBoardListeners.length > 0) {
                    popupItemThree.setOnClickListener(view -> {
                        optionPopUp.dismiss();
                        deletePrivateBoardListeners[0].onClick(view);
                    });
                }
                popupItemFour.setText(R.string.settings_board_popup);
                popupItemFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gear, 0, 0, 0);
                popupItemFour.setOnClickListener(view -> optionPopUp.dismiss());
                break;
            case PRIVATE_BOARD_USER_POPUP:
                popupItemThree.setText(R.string.unfollow_board_popup);
                popupItemThree.setOnClickListener(view -> optionPopUp.dismiss());

                popupItemFour.setText(R.string.report_board_popup);
                popupItemFour.setOnClickListener(view -> optionPopUp.dismiss());
                break;
            case HOME_POPUP:
                break;
        }

        // Creating the PopupWindow
        optionPopUp = new PopupWindow(context);
        optionPopUp.setContentView(layout);
        optionPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setFocusable(true);

        //Clear the default translucent background
        optionPopUp.setBackgroundDrawable(null);

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + offsetX, p.y + offsetY);

        setOnDismissListener();
    }

    //TODO: Refactor this entire class after complete functionality.
    public static void showPrivateBoardOptionPopup(View parentView, PrivateBoardInfoFragment fragment, String userId) {
        LinearLayout viewGroup = fragment.rootView.findViewById(R.id.owner_options_popup);
        View layout = LayoutInflater.from(fragment.rootView.getContext()).inflate(R.layout.owner_options_for_admin_or_user_popup, viewGroup);

        int[] location = new int[2];

        parentView.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        Point point = new Point();
        point.x = location[0];
        point.y = location[1];

        // Displaying the popup at the specified location, + offsets.

        TextView popupItemOne = layout.findViewById(R.id.kick);
        popupItemOne.setOnClickListener(view -> {
            fragment.deletePrivateBoardMember(userId);
            optionPopUp.dismiss();
        });

        optionPopUp = new PopupWindow(fragment.getContext());
        optionPopUp.setContentView(layout);
        optionPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        optionPopUp.setFocusable(true);

        //Clear the default translucent background
        optionPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, point.x, point.y);

        setOnDismissListener();
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
        optionPopUp.setBackgroundDrawable(null);

        // Displaying the popup at the specified location, + offsets.
        optionPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, point.x, point.y);

        setOnDismissListener();
    }

    private static void setOnDismissListener() {
        optionPopUp.setOnDismissListener(() -> optionPopUp = null);
    }
}
