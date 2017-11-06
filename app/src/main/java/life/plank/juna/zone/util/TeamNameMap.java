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

    private static HashMap<String, Drawable> teamNameMap = new HashMap<>();

    public static HashMap<String, Drawable> getTeamNameMap() {
        return teamNameMap;
    }

    public static void HashMaps(Context context) {
        teamNameMap.put("Charlton", ContextCompat.getDrawable(context, R.drawable.ic_charlton_logo));
        teamNameMap.put("Chelsea", ContextCompat.getDrawable(context, R.drawable.ic_chelsea_logo));
        teamNameMap.put("Coventry", ContextCompat.getDrawable(context, R.drawable.ic_coventry_city_logo));
        teamNameMap.put("Derby", ContextCompat.getDrawable(context, R.drawable.ic_derby_logo));
        teamNameMap.put("Leeds", ContextCompat.getDrawable(context, R.drawable.ic_leeds_united_fc_logo));
        teamNameMap.put("Leicester", ContextCompat.getDrawable(context, R.drawable.ic_leicester_logo));
        teamNameMap.put("Liverpool", ContextCompat.getDrawable(context, R.drawable.ic_liverpool_logo));
        teamNameMap.put("Sunderland", ContextCompat.getDrawable(context, R.drawable.ic_sunderland_logo));
        teamNameMap.put("Tottenham", ContextCompat.getDrawable(context, R.drawable.ic_tottenham_logo));
        teamNameMap.put("Man United", ContextCompat.getDrawable(context, R.drawable.ic_manchester_united_logo));
        teamNameMap.put("Arsenal", ContextCompat.getDrawable(context, R.drawable.ic_arsenal_logo));
        teamNameMap.put("Bradford", ContextCompat.getDrawable(context, R.drawable.ic_bradford_logo));
        teamNameMap.put("Ipswich", ContextCompat.getDrawable(context, R.drawable.ic_ipswich_logo));
        teamNameMap.put("Middlesbrough", ContextCompat.getDrawable(context, R.drawable.ic_middlesbrough_logo));
        teamNameMap.put("Everton", ContextCompat.getDrawable(context, R.drawable.ic_everton_logo));
        teamNameMap.put("Man City", ContextCompat.getDrawable(context, R.drawable.ic_manchester_logo));
        teamNameMap.put("Newcastle", ContextCompat.getDrawable(context, R.drawable.ic_newcastle_logo));
        teamNameMap.put("Southampton", ContextCompat.getDrawable(context, R.drawable.ic_southampton_logo));
        teamNameMap.put("West Ham", ContextCompat.getDrawable(context, R.drawable.ic_west_ham_logo));
        teamNameMap.put("Aston Villa", ContextCompat.getDrawable(context, R.drawable.ic_aston_villa_logo));
        teamNameMap.put("Bolton", ContextCompat.getDrawable(context, R.drawable.ic_bolton_wanderers_logo));
        teamNameMap.put("Blackburn", ContextCompat.getDrawable(context, R.drawable.ic_blackburn_rovers_logo));
        teamNameMap.put("Fulham", ContextCompat.getDrawable(context, R.drawable.ic_fulham_fc_logo));
        teamNameMap.put("Birmingham", ContextCompat.getDrawable(context, R.drawable.ic_birmingham_logo));
        teamNameMap.put("Middlesboro", ContextCompat.getDrawable(context, R.drawable.ic_middlesbrough_logo));
        teamNameMap.put("West Brom", ContextCompat.getDrawable(context, R.drawable.ic_west_brom_logo));
        teamNameMap.put("Portsmouth", ContextCompat.getDrawable(context, R.drawable.ic_portsmouth_logo));
        teamNameMap.put("Wolves", ContextCompat.getDrawable(context, R.drawable.ic_wolverhampton_logo));
        teamNameMap.put("Norwich", ContextCompat.getDrawable(context, R.drawable.ic_norwich_city_logo));
        teamNameMap.put("Crystal Palace", ContextCompat.getDrawable(context, R.drawable.ic_crystal_palace_logo));
        teamNameMap.put("Wigan", ContextCompat.getDrawable(context, R.drawable.ic_wigan_athletic_logo));
        teamNameMap.put("Reading", ContextCompat.getDrawable(context, R.drawable.ic_reading_logo));
        teamNameMap.put("Sheffield United", ContextCompat.getDrawable(context, R.drawable.ic_sheffield_fc_logo));
        teamNameMap.put("Watford", ContextCompat.getDrawable(context, R.drawable.ic_watford_logo));
        teamNameMap.put("Hull", ContextCompat.getDrawable(context, R.drawable.ic_hull_city_logo));
        teamNameMap.put("Stoke", ContextCompat.getDrawable(context, R.drawable.ic_stroke_city_logo));
        teamNameMap.put("Burnley", ContextCompat.getDrawable(context, R.drawable.ic_burnley_logo));
        teamNameMap.put("Blackpool", ContextCompat.getDrawable(context, R.drawable.ic_blackpool_logo));
        teamNameMap.put("QPR", ContextCompat.getDrawable(context, R.drawable.ic_queens_park_logo));
        teamNameMap.put("Swansea", ContextCompat.getDrawable(context, R.drawable.ic_swansea_logo));
        teamNameMap.put("Cardiff", ContextCompat.getDrawable(context, R.drawable.ic_cardiff_logo));
        teamNameMap.put("Bournemouth", ContextCompat.getDrawable(context, R.drawable.ic_bournemouth_logo));
        teamNameMap.put("Oldham", ContextCompat.getDrawable(context, R.drawable.ic_oldham_logo));
        teamNameMap.put("Wimbledon", ContextCompat.getDrawable(context, R.drawable.ic_wimbledon_logo));
        teamNameMap.put("Sheffield Weds", ContextCompat.getDrawable(context, R.drawable.ic_sheffield_logo));
        teamNameMap.put("Swindon", ContextCompat.getDrawable(context, R.drawable.ic_swindon_logo));
        teamNameMap.put("Nott'm Forest", ContextCompat.getDrawable(context, R.drawable.ic_nottingham_forest_logo));
        teamNameMap.put("Barnsley", ContextCompat.getDrawable(context, R.drawable.ic_barnsley_logo));
    }
}
