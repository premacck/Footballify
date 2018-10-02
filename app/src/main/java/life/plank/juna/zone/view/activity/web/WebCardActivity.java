package life.plank.juna.zone.view.activity.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class WebCardActivity extends AppCompatActivity {

    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.drag_area)
    ImageView dragArea;

    public static void launch(Activity from, String url) {
        Intent intent = new Intent(from, WebCardActivity.class);
        intent.putExtra(from.getString(R.string.feed_data_base_url), url);
        from.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_card);
        ButterKnife.bind(this);
        setupSwipeGesture(this, dragArea);

        String url = getIntent().getStringExtra(getString(R.string.feed_data_base_url));
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
