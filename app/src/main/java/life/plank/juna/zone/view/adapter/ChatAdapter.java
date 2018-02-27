package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.holder.ChatHolder;
import life.plank.juna.zone.viewmodel.ChatModel;


/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

    private Context context;
    private List<ChatModel> chatModels = new ArrayList<>();
    private final int ITEM_VIEW_INCOMING = 0;
    private final int ITEM_VIEW_OUTGOING = 1;
    private String text = "text";


    public ChatAdapter(Context context) {
        this.context = context;
        chatModels.addAll(ChatModel.getChats(context));
    }


    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_VIEW_INCOMING ? new ChatHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_incoming, parent, false)) : new ChatHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_outgoing, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        //TODO added to differentiate between media types, will be removed after getting the api
        if (text.contentEquals(chatModels.get(position).getTag())) {
            holder.messageImageView.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(chatModels.get(position).getText());
        } else {
            holder.messageTextView.setVisibility(View.GONE);
            holder.messageImageView.setVisibility(View.VISIBLE);
            holder.messageImageView.setImageDrawable(chatModels.get(position).getImage());
        }
        if (chatModels.get(position).isMyMessage()) {
            holder.nameTextView.setText(context.getString(R.string.me));
        }
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatModels.get(position).isMyMessage() ? ITEM_VIEW_OUTGOING : ITEM_VIEW_INCOMING;
    }
}