package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-arfaa on 07/02/18.
 */

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.feed_web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_feed_web_view);
        ButterKnife.bind(this);
        webView.loadUrl(getIntent().getStringExtra("web_url"));

    }

}
