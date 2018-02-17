package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.holder.ChatIncomingHolder;
import life.plank.juna.zone.view.holder.ChatOutgoingHolder;
import life.plank.juna.zone.viewmodel.ChatModel;


/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatIncomingHolder> {

    private Context context;
    private List<ChatModel> chatModels  = new ArrayList<>();
    private  LinearLayout.LayoutParams layoutParams;
    private int ITEM_VIEW_INCOMING = 0;
    private int ITEM_VIEW_OUTGOING = 1;


    public ChatAdapter(Context context) {
        this.context = context;
        chatModels.addAll(ChatModel.getChats(context));
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public ChatIncomingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_VIEW_INCOMING) {
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_incoming, parent, false);
             return new ChatIncomingHolder(view);
        }else {
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_outgoing, parent, false);
            return new ChatIncomingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ChatIncomingHolder holder, int position) {
        if ("text".contentEquals(chatModels.get(position).getTag())) {
            holder.messageImageImageView.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(chatModels.get(position).getText());

        } else {
            holder.messageTextView.setVisibility(View.GONE);
            holder.messageImageImageView.setVisibility(View.VISIBLE);
            holder.messageImageImageView.setImageDrawable(chatModels.get(position).getImage());
        }

       /* if (holder instanceof ChatIncomingHolder) {
            ChatIncomingHolder chatIncomingHolder = (ChatIncomingHolder) holder;
           *//* if (chatModels.get(position).isMyMessage()) {
                layoutParams.setMargins(80, 0, 0, 0);
                layoutParams.gravity = (Gravity.END);
                chatIncomingHolder.parentLinearLayout.setLayoutParams(layoutParams);
                chatIncomingHolder.parentLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_outgoing));
            } else {
                layoutParams.setMargins(0, 0, 0, 80);
                layoutParams.gravity = (Gravity.START);
                chatIncomingHolder.parentLinearLayout.setLayoutParams(layoutParams);
                chatIncomingHolder.parentLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_incoming));
            }*//*

            if ("text".contentEquals(chatModels.get(position).getTag())) {
                chatIncomingHolder.messageImageImageView.setVisibility(View.GONE);
                chatIncomingHolder.messageTextView.setVisibility(View.VISIBLE);
                chatIncomingHolder.messageTextView.setText(chatModels.get(position).getText());

            } else {
                chatIncomingHolder.messageTextView.setVisibility(View.GONE);
                chatIncomingHolder.messageImageImageView.setVisibility(View.VISIBLE);
                chatIncomingHolder.messageImageImageView.setImageDrawable(chatModels.get(position).getImage());
            }
        }else {
            ChatOutgoingHolder chatOutgoingHolder = (ChatOutgoingHolder) holder;
           *//* if (chatModels.get(position).isMyMessage()) {
                layoutParams.setMargins(80, 0, 0, 0);
                layoutParams.gravity = (Gravity.END);
                chatOutgoingHolder.parentLinearLayout.setLayoutParams(layoutParams);
                chatOutgoingHolder.parentLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_outgoing));
            } else {
                layoutParams.setMargins(0, 0, 0, 80);
                layoutParams.gravity = (Gravity.START);
                chatOutgoingHolder.parentLinearLayout.setLayoutParams(layoutParams);
                chatOutgoingHolder.parentLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.chat_background_incoming));
            }*//*

            if ("text".contentEquals(chatModels.get(position).getTag())) {
                chatOutgoingHolder.messageImageImageView.setVisibility(View.GONE);
                chatOutgoingHolder.messageTextView.setVisibility(View.VISIBLE);
                chatOutgoingHolder.messageTextView.setText(chatModels.get(position).getText());

            } else {
                chatOutgoingHolder.messageTextView.setVisibility(View.GONE);
                chatOutgoingHolder.messageImageImageView.setVisibility(View.VISIBLE);
                chatOutgoingHolder.messageImageImageView.setImageDrawable(chatModels.get(position).getImage());
            }
        }*/
    }
    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatModels.get(position).isMyMessage()){
            return ITEM_VIEW_OUTGOING;
        }else {
            return ITEM_VIEW_INCOMING;
        }
    }
}
