package life.plank.juna.zone.util.helper;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class StackAnimation {
    private int duration;
    private float startScale;
    private float pivot;

    public StackAnimation(int duration, float startScale, float pivot) {
        this.duration = duration;
        this.startScale = startScale;
        this.pivot = pivot;
    }

    public void animateStacks(CardView cardViewFront, CardView cardViewBack,
                              float endScale) {
        cardViewBack.bringToFront();
        scaleView(cardViewBack, startScale, endScale, startScale, endScale);
        scaleView(cardViewFront, startScale, startScale - (endScale - startScale), startScale, startScale - (endScale - startScale));
    }

    private void scaleView(View v, float startScale, float endScale, float startScaleX,
                           float endScaleX) {
        Animation anim = new ScaleAnimation(
                startScaleX, endScaleX, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_PARENT, pivot, // Pivot point of X scaling
                Animation.RELATIVE_TO_PARENT, pivot); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
        v.startAnimation(anim);
    }
}
