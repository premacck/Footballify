package life.plank.juna.zone.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

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
        teamNameMap.put("Charlton", context.getDrawable(R.drawable.ic_charlton_logo));
        teamNameMap.put("Chelsea", context.getDrawable(R.drawable.ic_chelsea_logo));
        teamNameMap.put("Coventry", context.getDrawable(R.drawable.ic_coventry_city_logo));
        teamNameMap.put("Derby", context.getDrawable(R.drawable.ic_derby_logo));
        teamNameMap.put("Leeds", context.getDrawable(R.drawable.ic_leeds_united_fc_logo));
        teamNameMap.put("Leicester", context.getDrawable(R.drawable.ic_leicester_logo));
        teamNameMap.put("Liverpool", context.getDrawable(R.drawable.ic_liverpool_logo));
        teamNameMap.put("Sunderland", context.getDrawable(R.drawable.ic_sunderland_logo));
        teamNameMap.put("Tottenham", context.getDrawable(R.drawable.ic_tottenham_logo));
        teamNameMap.put("Man United", context.getDrawable(R.drawable.ic_manchester_united_logo));
        teamNameMap.put("Arsenal", context.getDrawable(R.drawable.ic_arsenal_logo));
        teamNameMap.put("Bradford", context.getDrawable(R.drawable.ic_bradford_logo));
        teamNameMap.put("Ipswich", context.getDrawable(R.drawable.ic_ipswich_logo));
        teamNameMap.put("Middlesbrough", context.getDrawable(R.drawable.ic_middlesbrough_logo));
        teamNameMap.put("Everton", context.getDrawable(R.drawable.ic_everton_logo));
        teamNameMap.put("Man City", context.getDrawable(R.drawable.ic_manchester_logo));
        teamNameMap.put("Newcastle", context.getDrawable(R.drawable.ic_newcastle_logo));
        teamNameMap.put("Southampton", context.getDrawable(R.drawable.ic_southampton_logo));
        teamNameMap.put("West Ham", context.getDrawable(R.drawable.ic_west_ham_logo));
        teamNameMap.put("Aston Villa", context.getDrawable(R.drawable.ic_aston_villa_logo));
        teamNameMap.put("Bolton", context.getDrawable(R.drawable.ic_bolton_wanderers_logo));
        teamNameMap.put("Blackburn", context.getDrawable(R.drawable.ic_blackburn_rovers_logo));
        teamNameMap.put("Fulham", context.getDrawable(R.drawable.ic_fulham_fc_logo));
        teamNameMap.put("Birmingham", context.getDrawable(R.drawable.ic_birmingham_logo));
        teamNameMap.put("Middlesboro", context.getDrawable(R.drawable.ic_middlesbrough_logo));
        teamNameMap.put("West Brom", context.getDrawable(R.drawable.ic_west_brom_logo));
        teamNameMap.put("Portsmouth", context.getDrawable(R.drawable.ic_portsmouth_logo));
        teamNameMap.put("Wolves", context.getDrawable(R.drawable.ic_wolverhampton_logo));
        teamNameMap.put("Norwich", context.getDrawable(R.drawable.ic_norwich_city_logo));
        teamNameMap.put("Crystal Palace", context.getDrawable(R.drawable.ic_crystal_palace_logo));
        teamNameMap.put("Wigan", context.getDrawable(R.drawable.ic_wigan_athletic_logo));
        teamNameMap.put("Reading", context.getDrawable(R.drawable.ic_reading_logo));
        teamNameMap.put("Sheffield United", context.getDrawable(R.drawable.ic_sheffield_fc_logo));
        teamNameMap.put("Watford", context.getDrawable(R.drawable.ic_watford_logo));
        teamNameMap.put("Hull", context.getDrawable(R.drawable.ic_hull_city_logo));
        teamNameMap.put("Stoke", context.getDrawable(R.drawable.ic_stroke_city_logo));
        teamNameMap.put("Burnley", context.getDrawable(R.drawable.ic_burnley_logo));
        teamNameMap.put("Blackpool", context.getDrawable(R.drawable.ic_blackpool_logo));
        teamNameMap.put("QPR", context.getDrawable(R.drawable.ic_queens_park_logo));
        teamNameMap.put("Swansea", context.getDrawable(R.drawable.ic_swansea_logo));
        teamNameMap.put("Cardiff", context.getDrawable(R.drawable.ic_cardiff_logo));
        teamNameMap.put("Bournemouth", context.getDrawable(R.drawable.ic_bournemouth_logo));
        teamNameMap.put("Oldham", context.getDrawable(R.drawable.ic_oldham_logo));
        teamNameMap.put("Wimbledon", context.getDrawable(R.drawable.ic_wimbledon_logo));
        teamNameMap.put("Sheffield Weds", context.getDrawable(R.drawable.ic_sheffield_logo));
        teamNameMap.put("Swindon", context.getDrawable(R.drawable.ic_swindon_logo));
        teamNameMap.put("Nott'm Forest", context.getDrawable(R.drawable.ic_nottingham_forest_logo));
        teamNameMap.put("Barnsley", context.getDrawable(R.drawable.ic_barnsley_logo));
    }
}
