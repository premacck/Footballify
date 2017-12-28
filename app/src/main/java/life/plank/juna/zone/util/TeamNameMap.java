package life.plank.juna.zone.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;

import life.plank.juna.zone.R;

/**
 * Created by plank-sobia on 10/6/2017.
 */

public class TeamNameMap {

    private static HashMap<String, Drawable> teamLogoNameMap = new HashMap<>();
    private static HashMap<String, Drawable> teamNameMap = new HashMap<>();

    public static HashMap<String, Drawable> getTeamLogoNameMap() {
        return teamLogoNameMap;
    }

    public static HashMap<String, Drawable> getTeamNameMap() {
        return teamNameMap;
    }

    public static void setTeamLogoHashMap(Context context) {
        teamLogoNameMap.put("Charlton", ContextCompat.getDrawable(context, R.drawable.ic_charlton_logo));
        teamLogoNameMap.put("Chelsea", ContextCompat.getDrawable(context, R.drawable.ic_chelsea_logo));
        teamLogoNameMap.put("Coventry", ContextCompat.getDrawable(context, R.drawable.ic_coventry_city_logo));
        teamLogoNameMap.put("Derby", ContextCompat.getDrawable(context, R.drawable.ic_derby_logo));
        teamLogoNameMap.put("Leeds", ContextCompat.getDrawable(context, R.drawable.ic_leeds_united_fc_logo));
        teamLogoNameMap.put("Leicester", ContextCompat.getDrawable(context, R.drawable.ic_leicester_logo));
        teamLogoNameMap.put("Liverpool", ContextCompat.getDrawable(context, R.drawable.ic_liverpool_logo));
        teamLogoNameMap.put("Sunderland", ContextCompat.getDrawable(context, R.drawable.ic_sunderland_logo));
        teamLogoNameMap.put("Tottenham", ContextCompat.getDrawable(context, R.drawable.ic_tottenham_logo));
        teamLogoNameMap.put("Man United", ContextCompat.getDrawable(context, R.drawable.ic_manchester_united_logo));
        teamLogoNameMap.put("Arsenal", ContextCompat.getDrawable(context, R.drawable.ic_arsenal_logo));
        teamLogoNameMap.put("Bradford", ContextCompat.getDrawable(context, R.drawable.ic_bradford_logo));
        teamLogoNameMap.put("Ipswich", ContextCompat.getDrawable(context, R.drawable.ic_ipswich_logo));
        teamLogoNameMap.put("Middlesbrough", ContextCompat.getDrawable(context, R.drawable.ic_middlesbrough_logo));
        teamLogoNameMap.put("Everton", ContextCompat.getDrawable(context, R.drawable.ic_everton_logo));
        teamLogoNameMap.put("Man City", ContextCompat.getDrawable(context, R.drawable.ic_manchester_logo));
        teamLogoNameMap.put("Newcastle", ContextCompat.getDrawable(context, R.drawable.ic_newcastle_logo));
        teamLogoNameMap.put("Southampton", ContextCompat.getDrawable(context, R.drawable.ic_southampton_logo));
        teamLogoNameMap.put("West Ham", ContextCompat.getDrawable(context, R.drawable.ic_west_ham_logo));
        teamLogoNameMap.put("Aston Villa", ContextCompat.getDrawable(context, R.drawable.ic_aston_villa_logo));
        teamLogoNameMap.put("Bolton", ContextCompat.getDrawable(context, R.drawable.ic_bolton_wanderers_logo));
        teamLogoNameMap.put("Blackburn", ContextCompat.getDrawable(context, R.drawable.ic_blackburn_rovers_logo));
        teamLogoNameMap.put("Fulham", ContextCompat.getDrawable(context, R.drawable.ic_fulham_fc_logo));
        teamLogoNameMap.put("Birmingham", ContextCompat.getDrawable(context, R.drawable.ic_birmingham_logo));
        teamLogoNameMap.put("Middlesboro", ContextCompat.getDrawable(context, R.drawable.ic_middlesbrough_logo));
        teamLogoNameMap.put("West Brom", ContextCompat.getDrawable(context, R.drawable.ic_west_brom_logo));
        teamLogoNameMap.put("Portsmouth", ContextCompat.getDrawable(context, R.drawable.ic_portsmouth_logo));
        teamLogoNameMap.put("Wolves", ContextCompat.getDrawable(context, R.drawable.ic_wolverhampton_logo));
        teamLogoNameMap.put("Norwich", ContextCompat.getDrawable(context, R.drawable.ic_norwich_city_logo));
        teamLogoNameMap.put("Crystal Palace", ContextCompat.getDrawable(context, R.drawable.ic_crystal_palace_logo));
        teamLogoNameMap.put("Wigan", ContextCompat.getDrawable(context, R.drawable.ic_wigan_athletic_logo));
        teamLogoNameMap.put("Reading", ContextCompat.getDrawable(context, R.drawable.ic_reading_logo));
        teamLogoNameMap.put("Sheffield United", ContextCompat.getDrawable(context, R.drawable.ic_sheffield_fc_logo));
        teamLogoNameMap.put("Watford", ContextCompat.getDrawable(context, R.drawable.ic_watford_logo));
        teamLogoNameMap.put("Hull", ContextCompat.getDrawable(context, R.drawable.ic_hull_city_logo));
        teamLogoNameMap.put("Stoke", ContextCompat.getDrawable(context, R.drawable.ic_stroke_city_logo));
        teamLogoNameMap.put("Burnley", ContextCompat.getDrawable(context, R.drawable.ic_burnley_logo));
        teamLogoNameMap.put("Blackpool", ContextCompat.getDrawable(context, R.drawable.ic_blackpool_logo));
        teamLogoNameMap.put("QPR", ContextCompat.getDrawable(context, R.drawable.ic_queens_park_logo));
        teamLogoNameMap.put("Swansea", ContextCompat.getDrawable(context, R.drawable.ic_swansea_logo));
        teamLogoNameMap.put("Cardiff", ContextCompat.getDrawable(context, R.drawable.ic_cardiff_logo));
        teamLogoNameMap.put("Bournemouth", ContextCompat.getDrawable(context, R.drawable.ic_bournemouth_logo));
        teamLogoNameMap.put("Oldham", ContextCompat.getDrawable(context, R.drawable.ic_oldham_logo));
        teamLogoNameMap.put("Wimbledon", ContextCompat.getDrawable(context, R.drawable.ic_wimbledon_logo));
        teamLogoNameMap.put("Sheffield Weds", ContextCompat.getDrawable(context, R.drawable.ic_sheffield_logo));
        teamLogoNameMap.put("Swindon", ContextCompat.getDrawable(context, R.drawable.ic_swindon_logo));
        teamLogoNameMap.put("Nott'm Forest", ContextCompat.getDrawable(context, R.drawable.ic_nottingham_forest_logo));
        teamLogoNameMap.put("Barnsley", ContextCompat.getDrawable(context, R.drawable.ic_barnsley_logo));
    }

    public static void setTeamMap(Context context) {
        teamNameMap.put("Chelsea", ContextCompat.getDrawable(context, R.drawable.ic_club_chelsea));
        teamNameMap.put("Man United", ContextCompat.getDrawable(context, R.drawable.ic_club_man_united));
        teamNameMap.put("Arsenal", ContextCompat.getDrawable(context, R.drawable.ic_club_arsenal));
        teamNameMap.put("Tottenham", ContextCompat.getDrawable(context, R.drawable.ic_club_arsenal));
        teamNameMap.put("Leicester", ContextCompat.getDrawable(context, R.drawable.ic_club_leicester));
        teamNameMap.put("Liverpool", ContextCompat.getDrawable(context, R.drawable.ic_club_liverpool));
        teamNameMap.put("Everton", ContextCompat.getDrawable(context, R.drawable.ic_club_everton));
        teamNameMap.put("Man City", ContextCompat.getDrawable(context, R.drawable.ic_club_man_city));

        teamNameMap.put("Charlton", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Coventry", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Derby", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Leeds", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Sunderland", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Bradford", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Ipswich", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Middlesbrough", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Newcastle", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Southampton", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("West Ham", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Aston Villa", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Bolton", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Blackburn", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Fulham", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Birmingham", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Middlesboro", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("West Brom", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Portsmouth", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Wolves", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Norwich", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Crystal Palace", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Wigan", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Reading", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Sheffield United", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Watford", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Hull", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Stoke", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Burnley", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Blackpool", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("QPR", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Swansea", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Cardiff", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Bournemouth", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Oldham", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Wimbledon", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Sheffield Weds", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Swindon", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Nott'm Forest", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));
        teamNameMap.put("Barnsley", ContextCompat.getDrawable(context, R.drawable.ic_default_logo));

    }
}
