package life.plank.juna.zone.data.network.model;

import java.util.List;

public class NewsFeedActivity {

    public String id;
    public String actor;
    public String verb;
    public String object;
    public String time;
    public List<Object> to = null;

    public NewsFeedActivity withId(String id) {
        this.id = id;
        return this;
    }

    public NewsFeedActivity withActor(String actor) {
        this.actor = actor;
        return this;
    }

    public NewsFeedActivity withVerb(String verb) {
        this.verb = verb;
        return this;
    }

    public NewsFeedActivity withObject(String object) {
        this.object = object;
        return this;
    }

    public NewsFeedActivity withTime(String time) {
        this.time = time;
        return this;
    }

    public NewsFeedActivity withTo(List<Object> to) {
        this.to = to;
        return this;
    }

}