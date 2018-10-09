package life.plank.juna.zone.util;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class OnSwipeTouchListener implements OnTouchListener {

    private static final String TAG = OnSwipeTouchListener.class.getSimpleName();
    private static final float SWIPE_THRESHOLD_PERCENT = 0.4f;
    private final GestureDetector gestureDetector;
    private final int screenHeight;
    private final float screenWidth;
    private View backgroundLayout;
    private View backgroundView;
    private View dragView;
    private View rootView;
    private float motionOriginX;
    private float motionOriginY;
    private boolean isDragging;
    private float viewOriginX;
    private float viewOriginY;

    public OnSwipeTouchListener(Activity activity, View dragView, View rootView) {
        this(activity, dragView, rootView, null);
    }

    public OnSwipeTouchListener(Activity activity, View dragView, View rootView, ViewGroup backgroundLayout) {
        gestureDetector = new GestureDetector(activity, new GestureListener(this)) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                boolean result = super.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handleActionDown(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        handleActionUp(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handleActionMove(event);
                        break;
                }
                return result;
            }
        };
        this.dragView = dragView;
        this.rootView = rootView;
        viewOriginX = rootView.getTranslationX();
        viewOriginY = rootView.getTranslationY();
        int[] screenSize = UIDisplayUtil.getScreenSize(activity.getWindowManager().getDefaultDisplay());
        screenWidth = (screenSize[0] - getDp(16));
        screenHeight = screenSize[1];
        if (backgroundLayout != null) {
            this.backgroundLayout = backgroundLayout;
            this.backgroundView = backgroundLayout.getChildAt(0);
            this.backgroundLayout.setPivotX(screenSize[0] / 2);
            this.backgroundLayout.setPivotY(0);
        }
    }

    public enum SwipeDirection {
        Left, Right, Up, Down;

        public static final List<SwipeDirection> FREEDOM_NO_TOP = Arrays
                .asList(SwipeDirection.Down, SwipeDirection.Left, SwipeDirection.Right);
        public static final List<SwipeDirection> HORIZONTAL = Arrays
                .asList(SwipeDirection.Left, SwipeDirection.Right);
        public static final List<SwipeDirection> VERTICAL = Arrays
                .asList(SwipeDirection.Up, SwipeDirection.Down);

        public static List<SwipeDirection> from(int value) {
            switch (value) {
                case 0:
                    return HORIZONTAL;
                case 1:
                    return VERTICAL;
                default:
                    return FREEDOM_NO_TOP;
            }
        }
    }

    public enum Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void handleActionDown(MotionEvent event) {
        motionOriginX = event.getRawX();
        motionOriginY = event.getRawY();
    }

    private void handleActionUp(MotionEvent event) {
        if (isDragging) {
            isDragging = false;

            float motionCurrentX = event.getRawX();
            float motionCurrentY = event.getRawY();

            Quadrant quadrant = getQuadrant(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
            double radian = getRadian(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
            double degree;
            SwipeDirection direction = null;
            switch (quadrant) {
                case TOP_LEFT:
                    degree = Math.toDegrees(radian);
                    degree = 180 - degree;
                    radian = Math.toRadians(degree);
                    if (Math.cos(radian) < -0.5) {
                        direction = SwipeDirection.Left;
                    } else {
                        direction = SwipeDirection.Up;
                    }
                    break;
                case TOP_RIGHT:
                    degree = Math.toDegrees(radian);
                    radian = Math.toRadians(degree);
                    if (Math.cos(radian) < 0.5) {
                        direction = SwipeDirection.Up;
                    } else {
                        direction = SwipeDirection.Right;
                    }
                    break;
                case BOTTOM_LEFT:
                    degree = Math.toDegrees(radian);
                    degree = 180 + degree;
                    radian = Math.toRadians(degree);
                    if (Math.cos(radian) < -0.5) {
                        direction = SwipeDirection.Left;
                    } else {
                        direction = SwipeDirection.Down;
                    }
                    break;
                case BOTTOM_RIGHT:
                    degree = Math.toDegrees(radian);
                    degree = 360 - degree;
                    radian = Math.toRadians(degree);
                    if (Math.cos(radian) < 0.5) {
                        direction = SwipeDirection.Down;
                    } else {
                        direction = SwipeDirection.Right;
                    }
                    break;
            }

            float percent;
            if (direction == SwipeDirection.Left || direction == SwipeDirection.Right) {
                percent = getPercentX();
            } else {
                percent = getPercentY();
            }

            if (Math.abs(percent) > SWIPE_THRESHOLD_PERCENT) {
                if (direction == SwipeDirection.Down) {
                    onSwipeDown();
//                    Container swiped
                } else {
                    moveToOrigin(rootView);
                    moveToOrigin(dragView);
                    resetBackgroundView();
//                    Container moved to origin
                }
            } else {
                moveToOrigin(rootView);
                moveToOrigin(dragView);
                resetBackgroundView();
//                    Container moved to origin
            }
        }

        motionOriginX = event.getRawX();
        motionOriginY = event.getRawY();
    }

    private void handleActionMove(MotionEvent event) {
        isDragging = true;
        float translationY = viewOriginY + event.getRawY() - motionOriginY;
        rootView.setTranslationY(translationY);
        dragView.setTranslationY(translationY);
        float alpha = (translationY / screenHeight);

        if (backgroundLayout != null && backgroundLayout.getScaleX() <= screenWidth) {
            backgroundLayout.setScaleX(1 + (alpha / 10));
            backgroundLayout.setScaleY(1 + (alpha / 10));
        }
        if (backgroundView != null) {
            backgroundView.setAlpha(alpha);
        }
    }

    private void moveToOrigin(View view) {
        view.animate().translationX(viewOriginX)
                .translationY(viewOriginY)
                .alpha(1)
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator(1.0f))
                .start();
    }

    private void resetBackgroundView() {
        if (backgroundLayout != null) {
            backgroundLayout.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(200)
                    .setInterpolator(new OvershootInterpolator(1.0f))
                    .start();
        }
        if (backgroundView != null) {
            backgroundView.animate()
                    .alpha(0)
                    .setDuration(200)
                    .setInterpolator(new OvershootInterpolator(1.0f))
                    .start();
        }
    }

    private float getPercentX() {
        float percent = 2f * (rootView.getTranslationX() - viewOriginX) / rootView.getWidth();
        if (percent > 1) {
            percent = 1;
        }
        if (percent < -1) {
            percent = -1;
        }
        return percent;
    }

    private float getPercentY() {
        float percent = 2f * (rootView.getTranslationY() - viewOriginY) / rootView.getHeight();
        if (percent > 1) {
            percent = 1;
        }
        if (percent < -1) {
            percent = -1;
        }
        return percent;
    }

    private static double getRadian(float x1, float y1, float x2, float y2) {
        float width = x2 - x1;
        float height = y1 - y2;
        return Math.atan(Math.abs(height) / Math.abs(width));
    }

    private static Quadrant getQuadrant(float x1, float y1, float x2, float y2) {
        if (x2 > x1) { // Right
            if (y2 > y1) { // Bottom
                return Quadrant.BOTTOM_RIGHT;
            } else { // Top
                return Quadrant.TOP_RIGHT;
            }
        } else { // Left
            if (y2 > y1) { // Bottom
                return Quadrant.BOTTOM_LEFT;
            } else { // Top
                return Quadrant.TOP_LEFT;
            }
        }
    }

    private static final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        private final WeakReference<OnSwipeTouchListener> ref;

        public GestureListener(OnSwipeTouchListener onSwipeTouchListener) {
            ref = new WeakReference<>(onSwipeTouchListener);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            ref.get().onSwipeRight();
                        } else {
                            ref.get().onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        ref.get().onSwipeDown();
                    } else {
                        ref.get().onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                Log.e(TAG, "onFling: " + exception.getMessage());
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeDown() {
    }
}