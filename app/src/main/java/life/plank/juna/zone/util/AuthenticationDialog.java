package life.plank.juna.zone.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import life.plank.juna.zone.R;

public class AuthenticationDialog extends Dialog {

    private final AuthenticationListener listener;
    private Context context;

    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);

        final String url = context.getString(R.string.instagram_base_url)
                + context.getString(R.string.client_id_query_param)
                + context.getString(R.string.instagram_client_id)
                + context.getString(R.string.redirect_uri_query_param)
                + context.getString(R.string.instagram_redirect_url)
                + context.getString(R.string.response_type_query_param)
                + context.getString(R.string.display_scope_query_param);

        initializeWebView(url);
    }

    private void initializeWebView(String url) {
        WebView web_view = findViewById(R.id.web_view);
        web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            String access_token;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains(context.getString(R.string.access_token_query_param)) && !authComplete) {
                    Uri uri = Uri.parse(url);
                    access_token = uri.getEncodedFragment();
                    // get the whole token after the '=' sign
                    access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                    Log.i("", "CODE : " + access_token);
                    authComplete = true;
                    listener.onCodeReceived(access_token);
                    listener.showProgressSpinner();
                    dismiss();

                } else if (url.contains(context.getString(R.string.error_query_param))) {
                    Toast.makeText(context, "Error Occured", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
    }
}
