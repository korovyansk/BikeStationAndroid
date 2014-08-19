package ucsoftworks.com.bikestation.modules;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ucsoftworks.com.bikestation.ui.fragments.RentFragment;

@Module(
        addsTo = AppModule.class,
        library = true,
        injects = {
                RentFragment.class
        }
)
public class RegisteredModule {

    private final String uuid;

    public RegisteredModule(String uuid) {
        this.uuid = uuid;
    }

    @Provides @Named("UUID")
    String provideUUID() {
        return uuid;
    }
}
