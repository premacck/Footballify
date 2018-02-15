package life.plank.juna.zone.util;

import java.util.ArrayList;
import java.util.HashMap;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.LiveFeedTileData;
import life.plank.juna.zone.data.network.model.ScrubberViewData;

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

    public static void getHighLights(HashMap<Integer, ScrubberViewData> scrubberViewDataHolder) {
        ArrayList<Integer> tileImages = new ArrayList<>();
        scrubberViewDataHolder.put(2, new ScrubberViewData(": Kante bursts down the inside-right channel, a most proactive move. He’s bundled off the ball, the Arsenal defence closing ranks and doing their job. But maybe there’s a very early sign that Chelseaaren’t just here to sit back and soak up pressure from the home side",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(4, new ScrubberViewData("Monreal upendes Hazard on the halfway line. Nothing serious, just letting Chelsea’s playmaker know he’s there. Then Iwobi plays a loose pass on the edge of his own area, inviting needless pressure. Bakayoko looks for the top right from distance, but the shot is deflected out for a corner. From that, Moses takes another shot from the edge of the box, but it’s wide right and lame. The visitors very much on the front foot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(5, new ScrubberViewData("Chelsea put the ball in the net, but it’s offside. Azpilicueta, quarterbacking from deep, wedges a ball over the Arsenal back line. Pedro, running into the box, meets the dropping ball with a header, guiding it into the top right. It’s a fine finish, but he’d gone far too soon and was clearly offside. He doesn’t bother complaining.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(6, new ScrubberViewData("What a bright start by Chelsea, though. Arsenal have barely sniffed the ball yet.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(9, new ScrubberViewData("Arsenal need to score twice this evening now, though Chelsea’s away goal won’t count until the end of extra time. With this in mind, the home side finally string a few passes together. Ozil and Lacazette exchange passes down the left and nearly open Chelsea up. Then another phase sees Iwobi sweep a pass into the Chelsea box from the left. Wilshere is racing down the inside-left channel, hoping to meet the ball first and poke it past Caballero. The Chelsea keeper gets there first, though only just, palming the ball away from Wilshere, and from danger.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(10, new ScrubberViewData(" Caballero took a whack on the knee in making that save. A mop down with the magic sponge sees him up and about again.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(16, new ScrubberViewData("Rudiger causes a whole load of fuss in the Arsenal box. Then Willian works hard for another corner. That second one is intercepted immediately by Arsenal, who break upfield through Ozi",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(17, new ScrubberViewData("and he’s brought down on the halfway line in the cynical fashion by Hazard. That’s the first booking of the match",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        scrubberViewDataHolder.put(18, new ScrubberViewData(": Chelsea had started so brightly, but look slightly miffed in the wake of that crazy equaliser. Under no pressure, Azpilicueta sprays a wild ball out of play down the left, and frowns quite a lot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(20, new ScrubberViewData(": Chelsea had started so brightly, but look slightly miffed in the wake of that crazy equaliser. Under no pressure, Azpilicueta sprays a wild ball out of play down the left, and frowns quite a lot.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(21, new ScrubberViewData("Arsenal play a long pass down the middle of the park, which nearly sends Lacazette clear! Christensen sticks to his shoulder, though, and doesn’t give up a shooting chance. Eventually the ball bobbles through to Caballero, and the danger is over. This is a gloriously open game. Let’s hope neither coach works out how to shut it down.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(23, new ScrubberViewData("Chelsea stroke it around the middle awhile, reestablishing their early dominance in terms of possession, if not territory.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(25, new ScrubberViewData("Chelsea win a corner down the left, then pin Arsenal back in their own box for a bit. Moses has a shot from distance that’s blocked, then the dependably brilliant Azpilicueta swings a pass to the left. Ospina comes off his line rashly, and is beaten to the ball near the byline by Pedro, who pulls back for Hazard ... who sends a rabona cross into the centre. Nobody there in blue, and Arsenal clear easily, but full marks for style",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(27, new ScrubberViewData("Willian pulls up, holding his hamstring. It doesn’t look like he’ll be able to continue. It’s a big blow for Chelsea.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(28, new ScrubberViewData("Willian, a picture of misery, trudges off. On the touchline, Ross Barkley prepares to make his debut for Chelsea. For now, the game restarts and the visitors continue with ten men",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(30, new ScrubberViewData("On comes Barkley. Within seconds, he’s chasing a long pass down the left wing. He nearly beats Koscielny in the foot race, but can’t control the ball as it goes out for a goal kick. Koscielny nudges him in the back to make sure he slides across the turf nipples first. The crowd enjoyed that.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(31, new ScrubberViewData("Barkley’s had a busy introduction to the game all right. Now he’s scythed to the floor by Wilshere, who had miscontrolled the ball in the midfield and went after it with scant regard for his immediate environs. It’s a no-brainer of a booking",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(33, new ScrubberViewData("And now Barkley’s taken out Bellerin as the Arsenal man chased down the right! No booking, but it’s a needless challenge, just to the right of the Chelsea box. A free kick in a very dangerous position. Xhaka takes, looking to surprise Caballero by whipping one from a tight angle towards the top right. It nearly goes in, too, clipping off the back of Hazard in a two-man wall. (Barkley the other man in that wall, needless to say, he’s everywhere right now, one way or another.) The ball flies over the bar, and the resulting corner leads to nothing.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(35, new ScrubberViewData(": Chelsea stroke the ball hither and yon, making Arsenal do a lot of chasing. They don’t really go anywhere in particular, but they’ll be running down Arsenal’s energy. They’ve enjoyed the lion’s share of possession in this first half.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(37, new ScrubberViewData("Hazard zips down the left wing, checks, spins and goes again. Bellerin is beaten. Hazard makes his way to the byline, and into the area, and his low cross is hoicked out for a corner by Koscielny. The corner leads to diddly squat.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(39, new ScrubberViewData("After a wild start, this game has settled down considerably. Chelsea are hogging the ball, but to no great effect at the moment.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(40, new ScrubberViewData(" Ozil dribbles with purpose and style down the left, and is stopped unfairly by Christensen. A free kick, and a chance for Arsenal to load the box. Ozil floats the set piece in. Monreal meets it with a header, but there’s no pinball wizardry this time. The ball sails harmlessly into the arms of Caballero.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(42, new ScrubberViewData("More of the sterile Chelsea passing. Arsenal do a lot of chasing down. Suddenly Pedro and Alonso combine down the left to cause a bit of bother down the wing, but Pedro’s cross is easily cleared by Mustafi. “It’s not a good sign when Elneny seems to be the Arsenal player most in possession,” opines Charles Antaki. “That usually means the square pass and the worrying location of Xhaka somewhere risky. All we’re missing is Alexis Sánchez gesturing furiously out on the left.”",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(44, new ScrubberViewData("Moses breaks down the right, nearly releases Barkley, then has another run down the same wing. His cross causes a little bit of panic in the Arsenal area, but Bakayoko can’t shift his feet to get a snapshot away.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(45, new ScrubberViewData(": Wilshere dribbles down the inside-right channel and flicks a ball forward for Ozil, who is suddenly in the area with only Christensen and Caballero in front of him. He looks for the bottom left, and a clip off Christensen nearly spins the ball into the corner, but it drifts wide of the post at the last. The corner leads to nothing.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(46, new ScrubberViewData("Space for Moses down the right. He eventually runs into trouble, but for a second it looked like Chelsea were going to start fast again, with Hazard screaming for the ball down the inside-right channel and the home team light at the back.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(48, new ScrubberViewData(" Iwobi nips in from the left and flicks to Lacazette, who bustles to make some space on the edge of the area. He slightly miscontrols, the ball breaking to Xhaka, who fizzes a rising shot well over the bar. Lacazette seethes slightly; he was trying to work something out for himself there.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(50, new ScrubberViewData("For the first time in the match, Arsenal pin Chelsea back. Iwobi nearly dribbles his way through down the left. Not quite. Then Monreal crosses. Suddenly Chelsea break! Hazard skedaddles down the middle of the park and is preparing to shoot from the edge of the Arsenal box when Koscielny slides in to save the day. Good end-to-end fun.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(52, new ScrubberViewData(": Rudiger nibbles at Lacazette down the Arsenal right and that’s a free kick in a very dangerous position. Arsenal load the box; Xhaka takes; the flag goes up for offside. This is better from Arsenal, though.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(54, new ScrubberViewData("Hazard goes dribbling down the inside-right channel, and stumbles to the ground just before he reaches the area. Chelseaclaim a light clip by Mustafi, trotting behind, but there doesn’t seem to be any contact, and the referee waves play on. The ball is sent out for a throw on the right. From that, Hazard shoots towards the bottom right but the effort is deflected. The corner isn’t any cop.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(55, new ScrubberViewData("Now it’s Arsenal’s turn to ping the ball around without doing anything particularly constructive. But they’ll be happy to make Chelsea do the chasing for once. Arsenal have been much improved since the restart",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(57, new ScrubberViewData("Elneny hooks a ball down the left wing to release Ozil. Again the flag goes up for offside on a hair trigger. Ozil looked level. That’s happened on a couple of occasions to Arsenal tonight.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(58, new ScrubberViewData("Elneny hooks a ball down the left wing to release Ozil. Again the flag goes up for offside on a hair trigger. Ozil looked level. That’s happened on a couple of occasions to Arsenal tonight.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(59, new ScrubberViewData(": Iwobi races down the left, his low cross hooked over his own bar by Christensen. The corner isn’t all that. But Arsenal are the dominant force in this game for the first time tonight",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(62, new ScrubberViewData("Moses flies in on Iwobi and is booked. The decision goes to VAR: was Moses out of control, studs up? The decision comes: no. We move on.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(64, new ScrubberViewData(" Chelsea can’t get out right now. Bellerin and Wilshire probe down the Arsenal right, and the visitors do very well to keep their shape",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(65, new ScrubberViewData("Chelsea make their second change of the night, Batshuayi replacing Pedro",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(67, new ScrubberViewData(": Hazard tries to turn the tide, dribbling with purpose down the right. He’s upended by Monreal, who is booked for his trouble. Chelsea have a free kick, which is sent into a crowded box. Arsenal clear without too much ceremony.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(69, new ScrubberViewData("Xhaka slides a pass down the right for Bellerin, who reaches the area and checks. A pass inside for Lacazette, who lays off for the man who started the whole move off. Xhaka rushes in and blooters harmessly goalward.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(70, new ScrubberViewData("Kante charges Lacazette to the ground. It’s a fair bump with the shoulder, but Lacazette wants the yellow card shown. The referee simply reminds Kante not to get overly aggressive.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(71, new ScrubberViewData("A magnificent Gazza-esque burst down the inside-left channel by Wilshere nearly opens Chelsea up. He lays off to Ozil on the left. Ozil checks, and the momentum is gone. Wilshere has turned his career around marvellously this season.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(72, new ScrubberViewData("Zappacosta comes on for Moses. That’s Chelsea’s last throw of the dice.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(73, new ScrubberViewData("For once, a ricochet goes Chelsea’s way, Bellerin’s cross from the right coming off Alonso, back onto the Arsenal wing back, and out for a goal kick.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(74, new ScrubberViewData("Chelsea press Arsenal back for the first time in a while. Alonso and Hazard threaten to open the hosts up down the left. Not quite. And with that, the rare period of pressure is released. On the touchline, Antonio Conte looks concerned, and no wonder: his side were on top in the first half, but since the break they’ve been second best by some distance.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(75, new ScrubberViewData("Alonso has a blast from 25 yards on the left. It’s as wild as it is ambitious",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(76, new ScrubberViewData("Iwobi should have wrapped this tie up! Arsenal ping the ball down the right in pretty triangles, Lacazette, Ozil and Bellerin at the heart of it. Bellerin rolls a pass down the right, Ozil rolls it into the centre, and Iwobi, free, aims for the bottom right. But doesn’t meet it truly, and hits it straight at Caballero, who kicks clear. What a chance to book a date at Wembley!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(78, new ScrubberViewData("Chelsea go up the other end and earn a corner, Hazard flashing a shot-cum-cross through the six-yard box from the left. Nothing comes of the set piece. But the nerves are beginning to jangle at the Emirates, as both defences begin to tire and make a few mistakes. Gaps are opening up.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(80, new ScrubberViewData("Yes, mistakes are being made all right. Ozil slips a pass down the right to send Lacazette into space, then goes off into the centre to await a simple return. Lacazette shanks a terrible cross backwards, setting Zappacosta away down the opposite flank! Fortunately for Arsenal, Monreal is on the case and runs the Chelseaman out of play.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(82, new ScrubberViewData("Zappacosta, out on the right, hooks deep for Alonso, tight on the byline to the left of goal. Alonso tries his best Marco van Basten impersonation, but his screaming volley is blocked. From the corner, the ball hits Rudiger on the back, six yards out. The big man deserves a bit of luck tonight when it comes to strange bounces and odd deflections, but this one balloons over the bar harmlessly.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(84, new ScrubberViewData("Lacazette is replaced by Kolasinac, while Iwobi is sacrificed for Ramsey. Arsenal plan to hold on to what they have.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(85, new ScrubberViewData("This game is now a nervy mess. Nobody can string more than two passes together! And they say the League Cup doesn’t mean anything these days. Pah!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(86, new ScrubberViewData("Mustafi hoicks a pass down the middle, and Xhaka is free and in space! But the ball is dropping over his shoulder, and with Chelsea breath on his neck, he can only help it to harmlessly bounce wide left of the goal.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(87, new ScrubberViewData("Barkley tries to hold the ball up, 30 yards from the Arsenal goal. Mustafi comes through the back of him. Not a booking, but it’s a free kick in a very interesting position for Chelsea. The Emirates falls silent in fear/anticipation (delete according to viewpoint).",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(88, new ScrubberViewData("Alonso converts in the rugby style. That was as wild as they come.",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));


        scrubberViewDataHolder.put(90, new ScrubberViewData("Hazard, dropping deep to influence a desperate Chelseaattack, lifts a pass down the right for Zappacosta, who can’t keep the ball in play. Goal kick. There will be four added minutes. Put another way: Arsenal are four minutes away from a final tie with Manchester City!",
                ScrubberConstants.getScrubberViewCursor(), new LiveFeedTileData(tileImages), false));

        tileImages.add(R.drawable.image4);
        tileImages.add(R.drawable.ic_grid_one);

        scrubberViewDataHolder.put(7, new ScrubberViewData("Goal!! Eden Hazard - 7",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages), true));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image5);
        tileImages.add(R.drawable.image6);
        tileImages.add(R.drawable.ic_grid_five);
        tileImages.add(R.drawable.ic_grid_one);
        scrubberViewDataHolder.put(12, new ScrubberViewData("Goal! Rudiger - 12",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages), true));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image6);
        tileImages.add(R.drawable.ic_grid_two);
        tileImages.add(R.drawable.ic_grid_one);
        tileImages.add(R.drawable.image1);
        tileImages.add(R.drawable.image4);
        tileImages.add(R.drawable.ic_grid_three);
        tileImages.add(R.drawable.ic_grid_four);
        scrubberViewDataHolder.put(60 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Granit - 60",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages), true));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(17, new ScrubberViewData("Yellow card Hazard - 17",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages), true));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(31, new ScrubberViewData("Yellow card Wilshere - 31",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages), true));


        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image17);
        scrubberViewDataHolder.put(46, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();

        scrubberViewDataHolder.put(47, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.ic_grid_five);

        scrubberViewDataHolder.put(48, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();

        scrubberViewDataHolder.put(49, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.ic_grid_six);

        scrubberViewDataHolder.put(50, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(62 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Victor -62",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card2);
        scrubberViewDataHolder.put(67 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Nacho -66",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(30, new ScrubberViewData("Willian - OUT Barkley - IN  -30",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(65 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Pedro - OUT Batshuayi - IN - 65",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(72 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Moses - OUT Zappacosta - IN - 72",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages), true));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        tileImages.add(R.drawable.meme3);
        scrubberViewDataHolder.put(84 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Lacazette - IN Kolasinac -IN - 84",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages), true));
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
