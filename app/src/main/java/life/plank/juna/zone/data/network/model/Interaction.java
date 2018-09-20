package life.plank.juna.zone.data.network.model;

import lombok.Data;

@Data
public class Interaction {
    private int likes;
    private int dislikes;
    private int shares;
    private int pins;
    private int comments;
    private int posts;
    private int blocks;
    private int bans;
    private int mutes;
    private int reports;
    private int activeUsers;
    private int followers;
}
