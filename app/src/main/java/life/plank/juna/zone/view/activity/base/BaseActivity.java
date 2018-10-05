package life.plank.juna.zone.view.activity.base;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected Bitmap blurBg;

    public abstract void setBlurBg(Bitmap blurBg);
}