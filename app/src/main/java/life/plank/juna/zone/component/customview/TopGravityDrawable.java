package life.plank.juna.zone.component.customview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class TopGravityDrawable extends BitmapDrawable {

    private Drawable mDrawable;

    public TopGravityDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public TopGravityDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        int halfCanvas = canvas.getHeight() / 2;
        int halfDrawable = getIntrinsicHeight() / 2;
        canvas.save();
        canvas.translate(0, -halfCanvas + halfDrawable + (halfDrawable / 4));
        mDrawable.draw(canvas);
        canvas.restore();
    }
}