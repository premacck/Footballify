package life.plank.juna.zone.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatIncomingHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.message_text_view)
    public TextView messageTextView;
    @BindView(R.id.message_image_image_view)
    public ImageView messageImageImageView;
    @BindView(R.id.parent_linear_layout)
    public LinearLayout parentLinearLayout;

    public ChatIncomingHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
