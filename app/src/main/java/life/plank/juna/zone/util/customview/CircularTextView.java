package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class CircularTextView extends AppCompatTextView {

    private float strokeWidth;
    private int strokeColor;
    private int solidColor;
    private Paint circlePaint;
    private Paint strokePaint;

    public CircularTextView(Context context) {
        this(context, null);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircularTextView);

        setStrokeWidth(array.getFloat(R.styleable.CircularTextView_strokeWidth, 0));
        setStrokeColor(array.getColor(R.styleable.CircularTextView_strokeColor, context.getColor(R.color.white)));
        setSolidColor(array.getColor(R.styleable.CircularTextView_solidColor, context.getColor(R.color.red)));

        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (circlePaint == null) circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        if (strokePaint == null) strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter / 2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2, diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius - strokeWidth, circlePaint);

        super.onDraw(canvas);
    }

    public void setStrokeWidth(float dp) {
        strokeWidth = getDp(getContext(), dp);
    }

    public void setStrokeColor(int color) {
        strokeColor = color;
    }

    public void setSolidColor(int color) {
        solidColor = color;
        invalidate();
    }
}