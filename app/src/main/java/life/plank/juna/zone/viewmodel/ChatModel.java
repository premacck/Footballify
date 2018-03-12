package life.plank.juna.zone.viewmodel;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/16/2018.
 */

public class ChatModel{
    //TODO tag is to check whether the view is imageUrl or text. It will be changed once the api is ready
    private String tag;
    private String text;
    private String imageUrl;
    private boolean isMyMessage;

    public ChatModel(String tag, String text, String imageUrl,boolean isMyMessage) {
        this.tag = tag;
        this.text = text;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public void setMyMessage(boolean myMessage) {
        isMyMessage = myMessage;
    }

    public static List<ChatModel> getChats(Context context){
        List<ChatModel> chatModelList = new ArrayList<>();
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),"",false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"http://worldsoccertalk.com/wp-content/uploads/2017/09/425715293c16a2475deab21a67e2746286c86741.jpg",true));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text_two),"",false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),"",true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"http://www.upstart.net.au/wp-content/uploads/2017/08/2016-17_FC_Barcelona_at_the_Match_of_Champions.jpg",false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),"",false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"https://cdn.allwallpaper.in/wallpapers/640x960/11757/cutout-real-madrid-cf-cr7-football-player-640x960-wallpaper.jpg",true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"https://www.tubefilter.com/wp-content/uploads/2016/05/youtube-soccer-spain.jpg",false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text),"",false));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"http://img.bleacherreport.net/img/images/photos/003/661/021/hi-res-cccf19f0309762518219309b7c8fb8dc_crop_north.jpg?h=533&w=800&q=70&crop_x=center&crop_y=top",true));
        chatModelList.add(new ChatModel("image", context.getString(R.string.lorem_ipsum_text),"https://www.thesun.co.uk/wp-content/uploads/2017/07/nintchdbpict000342293828.jpg?strip=all&w=960&quality=100",false));
        chatModelList.add(new ChatModel("text", context.getString(R.string.lorem_ipsum_text_two),"",false));
        return chatModelList;

    }
}
