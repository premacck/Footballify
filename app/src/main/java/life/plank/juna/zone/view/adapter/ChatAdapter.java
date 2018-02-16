package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.holder.ChatHolder;
import life.plank.juna.zone.viewmodel.ChatModel;


/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<ChatModel> chatModels  = new ArrayList<>();



    public ChatAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        chatModels.addAll(ChatModel.getChats(context));

    }


    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_live_zone, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        if ("text".contentEquals(chatModels.get(position).getTag())){
            holder.messageImageImageView.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.VISIBLE);
            holder.messageTextView.setText(chatModels.get(position).getText());

        }else {
            holder.messageTextView.setVisibility(View.GONE);
            holder.messageImageImageView.setVisibility(View.VISIBLE);
            holder.messageImageImageView.setImageDrawable(chatModels.get(position).getImage());

        }

    }
    @Override
    public int getItemCount() {
        return chatModels.size();
    }
}
