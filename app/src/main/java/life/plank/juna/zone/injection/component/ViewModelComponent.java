package life.plank.juna.zone.injection.component;

import dagger.Component;
import life.plank.juna.zone.data.viewmodel.LeagueViewModel;
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel;
import life.plank.juna.zone.injection.module.ViewModelModule;
import life.plank.juna.zone.injection.scope.ViewModelScope;

@ViewModelScope
@Component(modules = {ViewModelModule.class})
public interface ViewModelComponent {

    void inject(LeagueViewModel leagueViewModel);

    void inject(MatchDetailViewModel matchDetailViewModel);
}