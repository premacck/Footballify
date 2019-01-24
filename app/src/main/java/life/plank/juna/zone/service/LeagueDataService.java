package life.plank.juna.zone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.football.League;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;

public class LeagueDataService {

    public static List<League> getStaticLeagues() {
        List<League> footballFeeds = new ArrayList<>();
        footballFeeds.add(new League(
                8,
                "Premier League",
                false,
                "2018/2019",
                "England",
                "https://image.ibb.co/msPsep/img_epl_logo.png",
                R.color.black_currant,
                R.drawable.img_epl_logo
        ));
        footballFeeds.add(new League(
                564,
                "La Liga",
                false,
                "2018/2019",
                "Spain",
                "https://cdn.bleacherreport.net/images/team_logos/328x328/la_liga.png",
                R.color.eclipse,
                R.drawable.img_laliga_logo
        ));
        footballFeeds.add(new League(
                82,
                "Bundesliga",
                false,
                "2018/2019",
                "Germany",
                "http://logok.org/wp-content/uploads/2014/12/Bundesliga-logo-880x655.png",
                R.color.sangria,
                R.drawable.img_bundesliga_logo
        ));
        footballFeeds.add(new League(
                2,
                "Champions League",
                false,
                "2018/2019",
                "Europe",
                "https://www.seeklogo.net/wp-content/uploads/2013/06/uefa-champions-league-eps-vector-logo.png",
                R.color.maire,
                R.drawable.img_champions_league_logo
        ));
        footballFeeds.add(new League(
                384,
                "Serie A",
                false,
                "2018/2019",
                "Italy",
                "http://www.tvsette.net/wp-content/uploads/2017/06/SERIE-A-LOGO.png",
                R.color.crusoe,
                R.drawable.img_serie_a_logo
        ));
        footballFeeds.add(new League(
                301,
                "Ligue 1",
                false,
                "2018/2019",
                "France",
                "http://logok.org/wp-content/uploads/2014/11/Ligue-1-logo-france-880x660.png",
                R.color.shuttle_grey,
                R.drawable.img_ligue_1_logo
        ));
        footballFeeds.add(new League(
                24,
                "FA Cup",
                true,
                "2018/2019",
                "England",
                "https://vignette.wikia.nocookie.net/logopedia/images/3/33/The_Emirates_FA_Cup.png",
                R.color.sapphire,
                R.drawable.img_fa_cup_logo
        ));
        footballFeeds.add(new League(
                570,
                "Copa Del Rey",
                true,
                "2018/2019",
                "Spain",
                "https://www.primeradivision.pl/luba/dane/pliki/bank_zdj/duzy/copadelrey.jpg",
                R.color.carmine,
                R.drawable.img_delrey_logo
        ));
        footballFeeds.add(new League(
                390,
                "Coppa Italia",
                true,
                "2018/2019",
                "Italy",
                "https://cdn.ghanasoccernet.com/2018/07/5b3f92288827c.jpg",
                R.color.husk,
                R.drawable.img_coppa_italia_logo
        ));
        footballFeeds.add(new League(
                5,
                "Europa League",
                false,
                "2018/2019",
                "Europe",
                "https://cdn.foxsports.com.br/sites/foxsports-br/files/img/competition/shields-original/logo-uefa-europa-league.png",
                R.color.carrot_orange,
                R.drawable.img_europa_logo
        ));
        return footballFeeds;
    }

    public static League getSpecifiedLeague(String leagueName) {
        if (isNullOrEmpty(leagueName)) return null;

        List<League> leagues = getStaticLeagues();
        for (League league : leagues) {
            if (Objects.equals(league.getName(), leagueName) || league.getName().equalsIgnoreCase(leagueName)) {
                return league;
            }
        }
        return null;
    }
}
