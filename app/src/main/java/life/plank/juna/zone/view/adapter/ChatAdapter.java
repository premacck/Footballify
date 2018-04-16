package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.ScrollRecyclerView;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.activity.ChatDetailsActivity;
import life.plank.juna.zone.view.activity.VideoPlayerActivity;
import life.plank.juna.zone.view.holder.ChatHolder;
import life.plank.juna.zone.viewmodel.ChatModel;

/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

    private final int ITEM_VIEW_INCOMING = 0;
    private final int ITEM_VIEW_OUTGOING = 1;
    private Context context;
    private List<ChatModel> chatModelList = new ArrayList<>();
    private String text = "text";
    private String video = "video";
    private ScrollRecyclerView scrollRecyclerView;
    private String image = "image";

    public ChatAdapter(Context context, ScrollRecyclerView scrollRecyclerView) {
        this.context = context;
        this.scrollRecyclerView = scrollRecyclerView;
        chatModelList.addAll(ChatModel.getChats(context));
    }

    public void addMessage(ChatModel chatModel) {
        chatModelList.add(chatModel);
        notifyItemInserted(chatModelList.size() - 1);
        scrollRecyclerView.scrollRecyclerViewToBottom();
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
        if (text.contentEquals(chatModelList.get(position).getTag())) {
            holder.messageImageView.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(chatModelList.get(position).getText());
            holder.playVideoImageView.setVisibility(View.GONE);
        } else {
            holder.messageTextView.setVisibility(View.GONE);
            holder.messageImageView.setVisibility(View.VISIBLE);
            if (video.contentEquals(chatModelList.get(position).getTag())) {
                holder.playVideoImageView.setVisibility(View.VISIBLE);
            } else {
                holder.playVideoImageView.setVisibility(View.GONE);
            }
            if (!"".contentEquals(chatModelList.get(position).getImageUrl())) {
                Picasso.with(context)
                        .load(chatModelList.get(position).getImageUrl())
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(holder.messageImageView);
            } else {
                Picasso.with(context)
                        .load(AppConstants.DEFAULT_IMAGE_URL)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .into(holder.messageImageView);
            }
        }
        if (chatModelList.get(position).isMyMessage()) {
            holder.nameTextView.setText(context.getString(R.string.me));
        }
        holder.timingTextView.setText(getCurrentTime());
        holder.chatItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image.contentEquals(chatModelList.get(position).getTag())) {
                    Intent intent = new Intent(context, ChatDetailsActivity.class);
                    intent.putExtra(AppConstants.CHAT_DETAILS_IMAGE, chatModelList.get(position).getImageUrl());
                    context.startActivity(intent);
                } else if (video.contentEquals(chatModelList.get(position).getTag())) {
                    Intent intentvideo = new Intent(context, VideoPlayerActivity.class);
                    context.startActivity(intentvideo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatModelList.get(position).isMyMessage() ? ITEM_VIEW_OUTGOING : ITEM_VIEW_INCOMING;
    }

    private String getCurrentTime() {
        return (String) DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime());
    }
}
