package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

import static life.plank.juna.zone.util.UIDisplayUtil.emoji;
import static life.plank.juna.zone.util.UIDisplayUtil.getEmojiByUnicode;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    private Context context;

    public EmojiAdapter(Context activity) {
        this.context = activity;
    }

    @Override
    public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmojiViewHolder holder, int position) {
        holder.emojiView.setText(getEmojiByUnicode(emoji[position]));
    }

    @Override
    public int getItemCount() {
        return emoji.length;
    }

    static class EmojiViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.emoji_view)
        TextView emojiView;

        EmojiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

