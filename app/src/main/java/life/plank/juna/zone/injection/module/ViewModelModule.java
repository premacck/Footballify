package life.plank.juna.zone.injection.module;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.model.football.LeagueInfo;
import life.plank.juna.zone.data.model.football.FixtureByMatchDay;
import life.plank.juna.zone.data.model.football.PlayerStats;
import life.plank.juna.zone.data.model.football.Standings;
import life.plank.juna.zone.data.model.football.TeamStats;
import life.plank.juna.zone.injection.scope.ViewModelScope;

@ViewModelScope
@Module
public class ViewModelModule {

    @ViewModelScope
    @Provides
    public MutableLiveData<LeagueInfo> provideLeagueInfoLiveData() {
        return new MutableLiveData<>();
    }

    @ViewModelScope
    @Provides
    public MutableLiveData<List<FixtureByMatchDay>> provideFixtureLiveData() {
        return new MutableLiveData<>();
    }

    @ViewModelScope
    @Provides
    public MutableLiveData<List<Standings>> provideStandingsLiveData() {
        return new MutableLiveData<>();
    }

    @ViewModelScope
    @Provides
    public MutableLiveData<List<TeamStats>> provideTeamStatsLiveData() {
        return new MutableLiveData<>();
    }

    @ViewModelScope
    @Provides
    public MutableLiveData<List<PlayerStats>> providePlayerStatsLiveData() {
        return new MutableLiveData<>();
    }
}
