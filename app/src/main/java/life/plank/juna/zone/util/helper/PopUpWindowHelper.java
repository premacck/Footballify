package life.plank.juna.zone.util.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by plank-niraj on 13-02-2018.
 *  Generic Popup window helper. Any type of views can be used.
 */

public class PopUpWindowHelper<T> {
    private T genericView;
    private int popUpHeight;
    private int popUpWidth;
    private View parentView;
    private int popUpLocationX;
    private int popUpLocationY;

    public void setView(T genericView) {
        this.genericView = genericView;
    }

    public void setPopUpHeight(int popUpHeight) {
        this.popUpHeight = popUpHeight;
    }

    public void setPopUpWidth(int popUpWidth) {
        this.popUpWidth = popUpWidth;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public void setPopUpLocationX(int popUpLocationX) {
        this.popUpLocationX = popUpLocationX;
    }

    public void setPopUpLocationY(int popUpLocationY) {
        this.popUpLocationY = popUpLocationY;
    }

    public PopupWindow genericPopUpWindow(Context context) {
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView((View) genericView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(popUpWidth);
        popupWindow.setHeight(popUpHeight);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,
                com.daasuu.bl.R.drawable.popup_window_transparent)
        );
        popupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, popUpLocationX, popUpLocationY);
        return popupWindow;
    }
}
