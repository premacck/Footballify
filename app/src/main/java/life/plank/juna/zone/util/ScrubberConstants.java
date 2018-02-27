package life.plank.juna.zone.util;

import java.util.ArrayList;
import java.util.HashMap;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.LiveFeedTileData;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.data.network.model.Tile;

/**
 * Created by plank-niraj on 06-02-2018.
 */

public class ScrubberConstants {

    public static final int SCRUBBER_VIEW_PROGRESS = 2;
    public static final int SCRUBBER_VIEW_HALF_TIME = 1;
    public static final int SCRUBBER_VIEW_GOAL = 3;
    public static final int SCRUBBER_VIEW_SUBSTITUTE = 5;
    public static final int SCRUBBER_VIEW_CARDS = 6;
    public static final int SCRUBBER_VIEW_CURSOR = 4;
    public static final int SCRUBBER_PRE_MATCH = 60;
    public static final int SCRUBBER_POST_MATCH = 60;
    public static final int SCRUBBER_VIEW_TOTAL_WINDOW = 105;
    public static final int SCRUBBER_VIEW_HALF_TIME_WINDOW = 5;
    private static String scrubberInProgress = "In Progress";
    private String scrubberHalf = "half";
    private String scrubberNormal = "normal";
    private String scrubberGoal = "goal";
    private String scrubberCursor = "cursor";
    private String scrubberSubstitute = "substitute";
    private String scrubberCard = "card";
    private String scrubberPost = "post";

    public static String getScrubberInProgress() {
        return scrubberInProgress;
    }

    public static int getScrubberViewHalfTimeWindow() {
        return SCRUBBER_VIEW_HALF_TIME_WINDOW;
    }

    public static int getScrubberViewProgress() {
        return SCRUBBER_VIEW_PROGRESS;
    }

    public static int getScrubberViewHalfTime() {
        return SCRUBBER_VIEW_HALF_TIME;
    }

    public static int getScrubberViewGoal() {
        return SCRUBBER_VIEW_GOAL;
    }

    public static int getScrubberViewSubstitute() {
        return SCRUBBER_VIEW_SUBSTITUTE;
    }

    public static int getScrubberViewCards() {
        return SCRUBBER_VIEW_CARDS;
    }

    public static int getScrubberViewCursor() {
        return SCRUBBER_VIEW_CURSOR;
    }

    public static int getScrubberPreMatch() {
        return SCRUBBER_PRE_MATCH;
    }

    public static int getScrubberPostMatch() {
        return SCRUBBER_POST_MATCH;
    }

    public static int getScrubberViewTotalWindow() {
        return SCRUBBER_VIEW_TOTAL_WINDOW;
    }

    public static void getHighLightsMatchOne(HashMap<Integer, ScrubberViewData> scrubberViewDataHolder) {
        ArrayList<Tile> tileArrayList = new ArrayList<>();
        scrubberViewDataHolder.put(2, new ScrubberViewData(": Kante bursts down the inside-right channel, a most proactive move. He’s bundled off the ball, the Arsenal defence closing ranks and doing their job. But maybe there’s a very early sign that Chelseaaren’t just here to sit back and soak up pressure from the home side",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(4, new ScrubberViewData("Monreal upendes Hazard on the halfway line. Nothing serious, just letting Chelsea’s playmaker know he’s there. Then Iwobi plays a loose pass on the edge of his own area, inviting needless pressure. Bakayoko looks for the top right from distance, but the shot is deflected out for a corner. From that, Moses takes another shot from the edge of the box, but it’s wide right and lame. The visitors very much on the front foot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(5, new ScrubberViewData("Chelsea put the ball in the net, but it’s offside. Azpilicueta, quarterbacking from deep, wedges a ball over the Arsenal back line. Pedro, running into the box, meets the dropping ball with a header, guiding it into the top right. It’s a fine finish, but he’d gone far too soon and was clearly offside. He doesn’t bother complaining.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(6, new ScrubberViewData("What a bright start by Chelsea, though. Arsenal have barely sniffed the ball yet.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(9, new ScrubberViewData("Arsenal need to score twice this evening now, though Chelsea’s away goal won’t count until the end of extra time. With this in mind, the home side finally string a few passes together. Ozil and Lacazette exchange passes down the left and nearly open Chelsea up. Then another phase sees Iwobi sweep a pass into the Chelsea box from the left. Wilshere is racing down the inside-left channel, hoping to meet the ball first and poke it past Caballero. The Chelsea keeper gets there first, though only just, palming the ball away from Wilshere, and from danger.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(10, new ScrubberViewData(" Caballero took a whack on the knee in making that save. A mop down with the magic sponge sees him up and about again.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(16, new ScrubberViewData("Rudiger causes a whole load of fuss in the Arsenal box. Then Willian works hard for another corner. That second one is intercepted immediately by Arsenal, who break upfield through Ozi",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(17, new ScrubberViewData("and he’s brought down on the halfway line in the cynical fashion by Hazard. That’s the first booking of the match",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(18, new ScrubberViewData(": Chelsea had started so brightly, but look slightly miffed in the wake of that crazy equaliser. Under no pressure, Azpilicueta sprays a wild ball out of play down the left, and frowns quite a lot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(20, new ScrubberViewData(": Chelsea had started so brightly, but look slightly miffed in the wake of that crazy equaliser. Under no pressure, Azpilicueta sprays a wild ball out of play down the left, and frowns quite a lot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(21, new ScrubberViewData("Arsenal play a long pass down the middle of the park, which nearly sends Lacazette clear! Christensen sticks to his shoulder, though, and doesn’t give up a shooting chance. Eventually the ball bobbles through to Caballero, and the danger is over. This is a gloriously open game. Let’s hope neither coach works out how to shut it down.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(23, new ScrubberViewData("Chelsea stroke it around the middle awhile, reestablishing their early dominance in terms of possession, if not territory.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(25, new ScrubberViewData("Chelsea win a corner down the left, then pin Arsenal back in their own box for a bit. Moses has a shot from distance that’s blocked, then the dependably brilliant Azpilicueta swings a pass to the left. Ospina comes off his line rashly, and is beaten to the ball near the byline by Pedro, who pulls back for Hazard ... who sends a rabona cross into the centre. Nobody there in blue, and Arsenal clear easily, but full marks for style",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(27, new ScrubberViewData("Willian pulls up, holding his hamstring. It doesn’t look like he’ll be able to continue. It’s a big blow for Chelsea.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(28, new ScrubberViewData("Willian, a picture of misery, trudges off. On the touchline, Ross Barkley prepares to make his debut for Chelsea. For now, the game restarts and the visitors continue with ten men",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(30, new ScrubberViewData("On comes Barkley. Within seconds, he’s chasing a long pass down the left wing. He nearly beats Koscielny in the foot race, but can’t control the ball as it goes out for a goal kick. Koscielny nudges him in the back to make sure he slides across the turf nipples first. The crowd enjoyed that.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(31, new ScrubberViewData("Barkley’s had a busy introduction to the game all right. Now he’s scythed to the floor by Wilshere, who had miscontrolled the ball in the midfield and went after it with scant regard for his immediate environs. It’s a no-brainer of a booking",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(33, new ScrubberViewData("And now Barkley’s taken out Bellerin as the Arsenal man chased down the right! No booking, but it’s a needless challenge, just to the right of the Chelsea box. A free kick in a very dangerous position. Xhaka takes, looking to surprise Caballero by whipping one from a tight angle towards the top right. It nearly goes in, too, clipping off the back of Hazard in a two-man wall. (Barkley the other man in that wall, needless to say, he’s everywhere right now, one way or another.) The ball flies over the bar, and the resulting corner leads to nothing.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(35, new ScrubberViewData(": Chelsea stroke the ball hither and yon, making Arsenal do a lot of chasing. They don’t really go anywhere in particular, but they’ll be running down Arsenal’s energy. They’ve enjoyed the lion’s share of possession in this first half.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(37, new ScrubberViewData("Hazard zips down the left wing, checks, spins and goes again. Bellerin is beaten. Hazard makes his way to the byline, and into the area, and his low cross is hoicked out for a corner by Koscielny. The corner leads to diddly squat.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(39, new ScrubberViewData("After a wild start, this game has settled down considerably. Chelsea are hogging the ball, but to no great effect at the moment.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(40, new ScrubberViewData(" Ozil dribbles with purpose and style down the left, and is stopped unfairly by Christensen. A free kick, and a chance for Arsenal to load the box. Ozil floats the set piece in. Monreal meets it with a header, but there’s no pinball wizardry this time. The ball sails harmlessly into the arms of Caballero.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(42, new ScrubberViewData("More of the sterile Chelsea passing. Arsenal do a lot of chasing down. Suddenly Pedro and Alonso combine down the left to cause a bit of bother down the wing, but Pedro’s cross is easily cleared by Mustafi. “It’s not a good sign when Elneny seems to be the Arsenal player most in possession,” opines Charles Antaki. “That usually means the square pass and the worrying location of Xhaka somewhere risky. All we’re missing is Alexis Sánchez gesturing furiously out on the left.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(44, new ScrubberViewData("Moses breaks down the right, nearly releases Barkley, then has another run down the same wing. His cross causes a little bit of panic in the Arsenal area, but Bakayoko can’t shift his feet to get a snapshot away.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(45, new ScrubberViewData(": Wilshere dribbles down the inside-right channel and flicks a ball forward for Ozil, who is suddenly in the area with only Christensen and Caballero in front of him. He looks for the bottom left, and a clip off Christensen nearly spins the ball into the corner, but it drifts wide of the post at the last. The corner leads to nothing.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(46, new ScrubberViewData("Space for Moses down the right. He eventually runs into trouble, but for a second it looked like Chelsea were going to start fast again, with Hazard screaming for the ball down the inside-right channel and the home team light at the back.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(48, new ScrubberViewData(" Iwobi nips in from the left and flicks to Lacazette, who bustles to make some space on the edge of the area. He slightly miscontrols, the ball breaking to Xhaka, who fizzes a rising shot well over the bar. Lacazette seethes slightly; he was trying to work something out for himself there.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(50, new ScrubberViewData("For the first time in the match, Arsenal pin Chelsea back. Iwobi nearly dribbles his way through down the left. Not quite. Then Monreal crosses. Suddenly Chelsea break! Hazard skedaddles down the middle of the park and is preparing to shoot from the edge of the Arsenal box when Koscielny slides in to save the day. Good end-to-end fun.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(52, new ScrubberViewData(": Rudiger nibbles at Lacazette down the Arsenal right and that’s a free kick in a very dangerous position. Arsenal load the box; Xhaka takes; the flag goes up for offside. This is better from Arsenal, though.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(54, new ScrubberViewData("Hazard goes dribbling down the inside-right channel, and stumbles to the ground just before he reaches the area. Chelseaclaim a light clip by Mustafi, trotting behind, but there doesn’t seem to be any contact, and the referee waves play on. The ball is sent out for a throw on the right. From that, Hazard shoots towards the bottom right but the effort is deflected. The corner isn’t any cop.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(55, new ScrubberViewData("Now it’s Arsenal’s turn to ping the ball around without doing anything particularly constructive. But they’ll be happy to make Chelsea do the chasing for once. Arsenal have been much improved since the restart",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(57, new ScrubberViewData("Elneny hooks a ball down the left wing to release Ozil. Again the flag goes up for offside on a hair trigger. Ozil looked level. That’s happened on a couple of occasions to Arsenal tonight.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(58, new ScrubberViewData("Elneny hooks a ball down the left wing to release Ozil. Again the flag goes up for offside on a hair trigger. Ozil looked level. That’s happened on a couple of occasions to Arsenal tonight.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(59, new ScrubberViewData(": Iwobi races down the left, his low cross hooked over his own bar by Christensen. The corner isn’t all that. But Arsenal are the dominant force in this game for the first time tonight",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(62, new ScrubberViewData("Moses flies in on Iwobi and is booked. The decision goes to VAR: was Moses out of control, studs up? The decision comes: no. We move on.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(64, new ScrubberViewData(" Chelsea can’t get out right now. Bellerin and Wilshire probe down the Arsenal right, and the visitors do very well to keep their shape",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(65, new ScrubberViewData("Chelsea make their second change of the night, Batshuayi replacing Pedro",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(67, new ScrubberViewData(": Hazard tries to turn the tide, dribbling with purpose down the right. He’s upended by Monreal, who is booked for his trouble. Chelsea have a free kick, which is sent into a crowded box. Arsenal clear without too much ceremony.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(69, new ScrubberViewData("Xhaka slides a pass down the right for Bellerin, who reaches the area and checks. A pass inside for Lacazette, who lays off for the man who started the whole move off. Xhaka rushes in and blooters harmessly goalward.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(70, new ScrubberViewData("Kante charges Lacazette to the ground. It’s a fair bump with the shoulder, but Lacazette wants the yellow card shown. The referee simply reminds Kante not to get overly aggressive.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(71, new ScrubberViewData("A magnificent Gazza-esque burst down the inside-left channel by Wilshere nearly opens Chelsea up. He lays off to Ozil on the left. Ozil checks, and the momentum is gone. Wilshere has turned his career around marvellously this season.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(72, new ScrubberViewData("Zappacosta comes on for Moses. That’s Chelsea’s last throw of the dice.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(73, new ScrubberViewData("For once, a ricochet goes Chelsea’s way, Bellerin’s cross from the right coming off Alonso, back onto the Arsenal wing back, and out for a goal kick.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(74, new ScrubberViewData("Chelsea press Arsenal back for the first time in a while. Alonso and Hazard threaten to open the hosts up down the left. Not quite. And with that, the rare period of pressure is released. On the touchline, Antonio Conte looks concerned, and no wonder: his side were on top in the first half, but since the break they’ve been second best by some distance.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(75, new ScrubberViewData("Alonso has a blast from 25 yards on the left. It’s as wild as it is ambitious",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(76, new ScrubberViewData("Iwobi should have wrapped this tie up! Arsenal ping the ball down the right in pretty triangles, Lacazette, Ozil and Bellerin at the heart of it. Bellerin rolls a pass down the right, Ozil rolls it into the centre, and Iwobi, free, aims for the bottom right. But doesn’t meet it truly, and hits it straight at Caballero, who kicks clear. What a chance to book a date at Wembley!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(78, new ScrubberViewData("Chelsea go up the other end and earn a corner, Hazard flashing a shot-cum-cross through the six-yard box from the left. Nothing comes of the set piece. But the nerves are beginning to jangle at the Emirates, as both defences begin to tire and make a few mistakes. Gaps are opening up.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(80, new ScrubberViewData("Yes, mistakes are being made all right. Ozil slips a pass down the right to send Lacazette into space, then goes off into the centre to await a simple return. Lacazette shanks a terrible cross backwards, setting Zappacosta away down the opposite flank! Fortunately for Arsenal, Monreal is on the case and runs the Chelseaman out of play.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(82, new ScrubberViewData("Zappacosta, out on the right, hooks deep for Alonso, tight on the byline to the left of goal. Alonso tries his best Marco van Basten impersonation, but his screaming volley is blocked. From the corner, the ball hits Rudiger on the back, six yards out. The big man deserves a bit of luck tonight when it comes to strange bounces and odd deflections, but this one balloons over the bar harmlessly.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(84, new ScrubberViewData("Lacazette is replaced by Kolasinac, while Iwobi is sacrificed for Ramsey. Arsenal plan to hold on to what they have.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(85, new ScrubberViewData("This game is now a nervy mess. Nobody can string more than two passes together! And they say the League Cup doesn’t mean anything these days. Pah!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(86, new ScrubberViewData("Mustafi hoicks a pass down the middle, and Xhaka is free and in space! But the ball is dropping over his shoulder, and with Chelsea breath on his neck, he can only help it to harmlessly bounce wide left of the goal.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(87, new ScrubberViewData("Barkley tries to hold the ball up, 30 yards from the Arsenal goal. Mustafi comes through the back of him. Not a booking, but it’s a free kick in a very interesting position for Chelsea. The Emirates falls silent in fear/anticipation (delete according to viewpoint).",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(88, new ScrubberViewData("Alonso converts in the rugby style. That was as wild as they come.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));


        scrubberViewDataHolder.put(90, new ScrubberViewData("Hazard, dropping deep to influence a desperate Chelseaattack, lifts a pass down the right for Zappacosta, who can’t keep the ball in play. Goal kick. There will be four added minutes. Put another way: Arsenal are four minutes away from a final tie with Manchester City!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        tileArrayList.add(new Tile("image",R.drawable.goal_image_three,0,"Why would Mourinho do that? Isn't he done with"));
        tileArrayList.add(new Tile("video",R.drawable.ic_football_dummy_image,0,"Goal!! Eden Hazard - 7"));
        scrubberViewDataHolder.put(7, new ScrubberViewData("Goal!! Eden Hazard - 7",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("text",0,0,"Why would Mourinho do that? Isn't he done with"));
        tileArrayList.add(new Tile("sticker",R.drawable.goal_image_two,R.drawable.ic_sticker_one,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(12, new ScrubberViewData("Goal! Rudiger - 12",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.goal_image_three,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(60 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Granit - 60",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.yellow_card,0,"Yellow card Hazard - 17"));
        tileArrayList.add(new Tile("video", R.drawable.image3, 0, "Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(17, new ScrubberViewData("Yellow card Hazard - 17",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("text",0,0,"Yellow card Wilshere - 31"));
        scrubberViewDataHolder.put(31, new ScrubberViewData("Yellow card Wilshere - 31",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));


        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.half_time_image_one,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(46, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();

        scrubberViewDataHolder.put(47, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.football_image_one,0,"Why would Mourinho do that? Isn't he done with"));

        scrubberViewDataHolder.put(48, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("video", R.drawable.image_dummy_three, 0, "Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(49, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(50, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.football_image_one,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(62 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Victor -62",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("text",0,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(67 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Nacho -66",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.image_dummy_four,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(30, new ScrubberViewData("Willian - OUT Barkley - IN  -30",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(65 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Pedro - OUT Batshuayi - IN - 65",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("sticker",R.drawable.image_dummy_one,R.drawable.ic_sticker_two,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(72 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Moses - OUT Zappacosta - IN - 72",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.image0,0,"Why would Mourinho do that? Isn't he done with"));
        tileArrayList.add(new Tile("image",R.drawable.image5,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(84 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Lacazette - IN Kolasinac -IN - 84",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

    }

    public static void getHighLightsMatchTwo(HashMap<Integer, ScrubberViewData> scrubberViewDataHolder) {

        ArrayList<Tile> tileArrayList = new ArrayList<>();
        scrubberViewDataHolder.put(1, new ScrubberViewData("Peep peep! City, in sky blue, kick off from left to right. Leicester are in their black away kit.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(3, new ScrubberViewData("There was a degree of catharsis in that goal for Sterling, who missed embarrassingly from a very similar position at Burnley last weekend",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(4, new ScrubberViewData("That was suich a good cross from De Bruyne. As with so many of his balls into the box, it brought to mind the greatest crosser who has ever lived, David Beckham.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(6, new ScrubberViewData("I know it is fashionable to knock today’s players in a financially bloated industry but here goes,” says Andy. “Mahrez and his like should go to reality-check classes to realize just how lucky they are to be doing something they love. Most are not so lucky and a lot are really struggling in an increasingly self-interested society",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(10, new ScrubberViewData("min Leicester pump a long ball in behind for Vardy. He gets away from Laporte but the impressive Ederson comes out of his box to clear.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(12, new ScrubberViewData("The only good thing for Leicester is that they will still be playing on the counter-attack, even at 1-0 down. At the moment, the ball in behind for Vardy seems to cover Plans A-F. You can’t blame them for that, especially after the success he had against City last season.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(13, new ScrubberViewData("That was Sterling’s 20th goal of the season. His previous best was 11. It’s so good for England to have five or six players working under Pep Guardiola",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(15, new ScrubberViewData("Leicester can’t lay a finger on City at the moment. City don’t get enough credit for an exceptional defensive record this season - they’ve conceded only 19 goals in the league, the second best behind Manchester United.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(17, new ScrubberViewData("Sterling does superbly to keep an overhit pass in play before whipping a cross to the near post that is pushed away unconvincingly by Schmeichel. A second City goal is in the post.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(18, new ScrubberViewData("min I genuinely can’t remember the last time Leicester crossed the halfway line. It’s not their fault",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(21, new ScrubberViewData("Aguero moves into the box on the left after some smooth one-touch play, but Maguire makes a good interception. This is, as Jamie Carragher has just said on Sky, a training session. Attack and defence, invasion and repulsion.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(23, new ScrubberViewData("Leicester’s first decent attack leads to a corner on the left. It’s headed away and City break four on three. Sterling plays a good angled pass to Aguero, who is unusually indecisive in the box and loses the ball to Fuchs. He clips the ball back towards the halfway line, and then...",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(26, new ScrubberViewData("Gundogan’s long-range curler is comfortably held by Schmeichel",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(29, new ScrubberViewData("Bernardo Silva twists Fuchs inside out on the right of the box before curving a crosshot from a very tight angle that is beaten away by Schmeichel. Moments later, Fernandinho’s deflected 20-yard shot is held to his left by Schmeichel.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(30, new ScrubberViewData("Re Andy at 6 minutes and it being fashionable to knock today’s players .... I’d like to give great credit to Mesut Ozil among others for having the good sense to take the money and run,” says Gene Salorio. “Every profession has members who over-rate themselves and think they should be top dog -- the character actor who thinks he should be a star, members of rock groups who insist on disastrous solo careers, Michael Gove etc. -- and it’s refreshing to see football players who recognize that a good deal beats yet more ego gratification.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(31, new ScrubberViewData("Either Sky have the crowd mics turned down or the atmosphere is terribly subdued at the Etihad. They’re watching probably the most magical team in City’s history!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(32, new ScrubberViewData("Sterling, just inside the penalty area on the left, goes down after a challenge from Silva. Mike Jones ignores it. We haven’t seen a replay yet but I think it was the right decision",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(33, new ScrubberViewData("De Bruyne, the only City player showing much urgency, wallops a low shot from the left of the box that is palmed behind at the near post by Schmeichel",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(34, new ScrubberViewData("The replay suggests Mike Jones was right to ignore that City penalty appeal. It might have been a dive by Sterling, though it’s hard to be sure these days.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(36, new ScrubberViewData(" So as both a journalist and a human you cannot take a position without knowing the facts?” says Ian Copestake. “How quaint.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(37, new ScrubberViewData("It’s interesting how often teams who have effectively won the title by Christmas cannot help but sleepwalk through the second half of the season: Manchester United 2000-01, Chelsea 2005-06, Manchester United 2012-13, Chelsea 2014-15. City aren’t sleepwalking yet but nor are they playing at the awesome levels they reached earlier in the season.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(41, new ScrubberViewData("The wonderful De Bruyne plays a cute angled pass into the area for Sterling, who goes round Schmeichel and flicks the ball towards goal from a tight angle. It would have gone in but for Dragovic, who slid in front of the near post to make a superb block.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(42, new ScrubberViewData("“Can I offer an alternative view on Mahrez?” says David Hopkins. “He was offered the chance for an enhanced career move, then had it taken away. Those in other well-paid industries would be peeved, and might well ‘work from home’ for a few days. Why aren’t footballers extended the same courtesy?”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(43, new ScrubberViewData("The pressure is building on Leicester, who could really use half-time.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(45, new ScrubberViewData("Even by the standards of the Premier League, the atmosphere is funereal. It might be the crowd mics I suppose",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(46, new ScrubberViewData("Peep peep! Leicester begin the second half. They have made a tactical change, with Adrien Silva replaced by Danny Simpson.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(47, new ScrubberViewData("It looks like Leicester have switched to a 4-5-1, with Chilwell and Diabate playing wide in midfield and Albrighton in the centre.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(48, new ScrubberViewData("“Your observation about sleepwalking is even more interesting if you draw parallels to Guardiola’s seasons at Bayern Munich,” says Konstantin Sauer. “In his first season he (infamously) stated the “league was over” after winning the Bundesliga championship and started fielding teams entirely made from youth players, which finally led to a drop in tension and crashing defeats in the Champions League semis. Even if he learned from that mistake, Bayern’s superiority in the league would still make them vulnerable during the later stages in the Champions League the following seasons. I wonder what Guardiola will do this time to keep up the pace and prevent that same situation unfold again.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(51, new ScrubberViewData("Sterling runs square across the Leicester area, bounces off Albrighton and appeals for a penalty. There was nothing in that",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(54, new ScrubberViewData("For a normal player that third assist would have been a gem; for De Bruyne it was almost mundane. I’m struggling to think of a better hat-trick of assists in one game.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(57, new ScrubberViewData("I do get what David Hopkins says but there is one thing we are forgetting,” says Andy. “He signed a contract which no doubt financially remunerated him exceptionally well. If he wants to break that by leaving earlier then the employer who, as far as I am aware haven’t treated him that badly, not as bad as dpd to their ‘self-employed’ employees, are perfectly within their rights to say yes...but on our own terms. You make a pact with the devil...well be prepared to accept there is a price.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(58, new ScrubberViewData("Another poor clearance from Schmeichel goes to Sterling, who runs 30 yards down the left and into the area before slipping the ball back towards Gundogan. His first-time shot is crucially blocked by the diligent Albrighton and flies behind for a corner.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(59, new ScrubberViewData("De Bruyne’s outswinging corner is headed a few yards wide of the far post by Otamendi",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(60, new ScrubberViewData("Here’s Ian Copestake. “Matt Loten (half-time chit-chat) puts into words how I feel about Karius at Liverpool.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(61, new ScrubberViewData("Leicester are about to make a double change, with Manchester City players of the past and a parallel universe coming on: Kelechi Iheanacho and Riyad Mahrez",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(62, new ScrubberViewData("Mahrez replaces Fousseni Diabate, who had a quiet game, and is cheered by both sets of fans",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(63, new ScrubberViewData("now Iheanacho replaces Ben Chilwell.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(64, new ScrubberViewData(" After good play from Sterling and Gundogan, Aguero crunches the ball high and wide from a tight angle.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(65, new ScrubberViewData("Danilo replaces the quietly impressive Oleksandra Zinchenko. That is probably because of the introduction of Mahrez, who will be cutting in on his left foot all the time. Danilo is right-footed, Zinchenko isn’t.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(66, new ScrubberViewData("“De Bruyne is indeed one of the best passers of the ball that I’ve ever seen but the problem with him being the best in the world now is that there’s a left peg on a certain Argentinian who’s been doing that for a decade now,” says Paul Fitzgerald.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(68, new ScrubberViewData("“De Bruyne is indeed one of the best passers of the ball that I’ve ever seen but the problem with him being the best in the world now is that there’s a left peg on a certain Argentinian who’s been doing that for a decade now,” says Paul Fitzgerald.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(70, new ScrubberViewData("Bernardo Silva shuffles infield from the right and eases a lovely curling shot that drifts just past the far top corner",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(71, new ScrubberViewData("Some people say De Bruyne has that rare, magical ability to play as if he has a bird’s eye view of the pitch. I don’t agree with that. He sees things that most of us wouldn’t spot if we had a bird’s eye view and the ability to freeze time.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(73, new ScrubberViewData("Laporte is robbed on the halfway line by Vardy, which allows Mahrez to move towards the penalty area. Gundogan brings him down 25 yards from goal and is booked.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(74, new ScrubberViewData("Leicester make a balls of a training ground free-kick.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(75, new ScrubberViewData("Otamendi’s deft chest-volley is held by Schmeichel, and at the other end Ederson hares from his line to just beat Vardy to a through ball. Good goalkeeping.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(76, new ScrubberViewData("Otamendi’s deft chest-volley is held by Schmeichel, and at the other end Ederson hares from his line to just beat Vardy to a through ball. Good goalkeeping.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(80, new ScrubberViewData("City substitution A: Phil Foden replaces Fernandinho.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(81, new ScrubberViewData("Another City change: Otamendi off, John Stones on.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(83, new ScrubberViewData("Vardy and Laporte are booked after sticking their heads together off the ball.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(85, new ScrubberViewData("This match could be used as a riddle. Which game included two hat-tricks and ended 4-1? Aguero’s three goals will go in the book but De Bruyne’s three assists are the thing we’ll remember about the game.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(89, new ScrubberViewData("De Bruyne shoots from 35 yards and almost belts it out of the ground. When you see standards slip like that, you have to ask: has Kevin de Bruyne taken Kevin de Bruyne as far as he can?",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        scrubberViewDataHolder.put(90, new ScrubberViewData("A-hem, as I was saying. Sergio Aguero gets his fourth goal with an utter screamer! He received the ball from Foden 22 yards out, moved it onto his right foot and whacked a vicious, dipping shot that went straight above Schmeichel’s head and in off the crossbar.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileArrayList), false));

        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.football_image_one,0,"Why would Mourinho do that? Isn't he done with"));
        scrubberViewDataHolder.put(46, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();

        scrubberViewDataHolder.put(47, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.football_image_one,0,"Why would Mourinho do that? Isn't he done with"));

        scrubberViewDataHolder.put(48, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();

        scrubberViewDataHolder.put(49, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));
        tileArrayList = new ArrayList<>();
        tileArrayList.add(new Tile("image",R.drawable.football_image_one,0,"Why would Mourinho do that? Isn't he done with"));

        scrubberViewDataHolder.put(50, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(3, new ScrubberViewData("Goal!! Raheem sterling - 3",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(24, new ScrubberViewData("Goal!! Jamie Vardy - 24",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(48 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Sergio Aguero - 48",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(53 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Sergio Aguero - 53",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(77 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Sergio Aguero - 77",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(90 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Sergio Aguero - 90",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(71 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Wilfred Ndidi - 71",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(73 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Ikay Gundogan - 73",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(83 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Aymeric Laporte - 83",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(83 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Jamie Vardy -83",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(89 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Harry Maguire -89",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(45, new ScrubberViewData("Adrien Silva - OUT - 45 Danny Simpson - IN - 45",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(62 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Fousseni Diabaté - OUT  Riyad Mahrez - IN - 62",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(62 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Ben Chillwell - OUT - 62 Kelechi Iheanacho - IN - 62",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(65 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Zinchenko - OUT - 65 Danilo - IN - 65",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(80 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Fernandinho - OUT - 80 Foden - IN - 80",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

        scrubberViewDataHolder.put(81 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Otamendi - OUT - 81 Stones - IN - 81",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileArrayList), true));

    }

    public String getScrubberHalf() {
        return scrubberHalf;
    }

    public String getScrubberNormal() {
        return scrubberNormal;
    }

    public String getScrubberGoal() {
        return scrubberGoal;
    }

    public String getScrubberCursor() {
        return scrubberCursor;
    }

    public String getScrubberSubstitute() {
        return scrubberSubstitute;
    }

    public String getScrubberCard() {
        return scrubberCard;
    }

    public String getScrubberPost() {
        return scrubberPost;
    }
}
