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

    public ChatModel(String tag, String text, Drawable image) {
        this.tag = tag;
        this.text = text;
        this.image = image;
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

    public static List<ChatModel> getChats(Context context){
        List<ChatModel> chatModelList = new ArrayList<>();
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image2)));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text_two),null));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.ic_fourth_dummy)));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image0)));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),ContextCompat.getDrawable(context,R.drawable.image1)));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),null));
        return chatModelList;

    }
}
