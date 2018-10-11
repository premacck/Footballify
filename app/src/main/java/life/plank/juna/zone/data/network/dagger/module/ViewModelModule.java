package life.plank.juna.zone.data.network.dagger.module;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import life.plank.juna.zone.data.local.model.LeagueInfo;
import life.plank.juna.zone.data.model.FixtureByMatchDay;
import life.plank.juna.zone.data.model.PlayerStats;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.network.dagger.scope.ViewModelScope;

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
