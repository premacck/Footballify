package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import life.plank.juna.zone.R;

public class ObliqueStrikeTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint paint;

    public ObliqueStrikeTextView(Context context) {
        super(context);
        init(context);
    }

    public ObliqueStrikeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ObliqueStrikeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        int dividerColor = resources.getColor(R.color.grey_0_7, null);

        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStrokeWidth(resources.getDimension(R.dimen.vertical_divider_width));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getHeight(), getWidth() - (getWidth() / 5), getHeight() / 10, paint);
    }
}