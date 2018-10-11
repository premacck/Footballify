package life.plank.juna.zone.data.network.dagger.component;

import dagger.Component;
import life.plank.juna.zone.data.network.dagger.module.ViewModelModule;
import life.plank.juna.zone.data.viewmodel.LeagueViewModel;
import life.plank.juna.zone.data.viewmodel.MatchDetailViewModel;

@Component(modules = {ViewModelModule.class})
public interface ViewModelComponent {

    void inject(LeagueViewModel leagueViewModel);

    void inject(MatchDetailViewModel matchDetailViewModel);
}