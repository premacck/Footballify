package life.plank.juna.zone.viewmodel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatModel{
    private String tag;
    private String text;
    private Drawable image;
    private boolean isMyMessage;

    public ChatModel(String tag, String text, Drawable image,boolean isMyMessage) {
        this.tag = tag;
        this.text = text;
        this.image = image;
        this.isMyMessage = isMyMessage;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    public static List<ChatModel> getChats(Context context){
        List<ChatModel> chatModelList = new ArrayList<>();
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null,false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image2),true));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text_two),null,false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null,true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.ic_fourth_dummy),false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null,false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image0),true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image1),false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null,false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image0),true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image0),false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text_two),null,false));
        return chatModelList;

    }
}
