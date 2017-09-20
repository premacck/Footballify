package life.plank.juna.zone.data.network.model;

import java.util.List;

public class Activity {

    public String id;
    public String actor;
    public String verb;
    public String object;
    public String time;
    public List<Object> to = null;

    public Activity withId(String id) {
        this.id = id;
        return this;
    }

    public Activity withActor(String actor) {
        this.actor = actor;
        return this;
    }

    public Activity withVerb(String verb) {
        this.verb = verb;
        return this;
    }

    public Activity withObject(String object) {
        this.object = object;
        return this;
    }

    public Activity withTime(String time) {
        this.time = time;
        return this;
    }

    public Activity withTo(List<Object> to) {
        this.to = to;
        return this;
    }

}