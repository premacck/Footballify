package life.plank.juna.zone.view.activity.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class WebCardActivity extends AppCompatActivity {

    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.drag_area)
    ImageView dragArea;

    public static void launch(Activity from, String url) {
        Intent intent = new Intent(from, WebCardActivity.class);
        intent.putExtra(from.getString(R.string.backend_demo_base_url), url);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_card);
        ButterKnife.bind(this);
        setupSwipeGesture(this, dragArea, rootCard);

        String url = getIntent().getStringExtra(getString(R.string.backend_demo_base_url));
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.float_down, R.anim.sink_down);
    }
}
