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

public class ChatHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.name_text_view)
    public TextView nameTextView;
    @BindView(R.id.message_text_view)
    public TextView messageTextView;
    @BindView(R.id.message_image_view)
    public ImageView messageImageView;
    @BindView(R.id.parent_linear_layout)
    public LinearLayout parentLinearLayout;
    @BindView(R.id.chat_item_linear_layout)
    public LinearLayout chatItemLinearLayout;
    @BindView(R.id.play_video_image_view)
    public ImageView playVideoImageView;

    public ChatHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
