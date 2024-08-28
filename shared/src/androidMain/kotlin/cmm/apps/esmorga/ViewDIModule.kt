package cmm.apps.esmorga

import cmm.apps.viewmodel.eventdetails.EventDetailsViewModel
import cmm.apps.viewmodel.eventlist.EventListViewModel
import cmm.apps.viewmodel.login.LoginViewModel
import cmm.apps.viewmodel.registration.RegistrationViewModel
import cmm.apps.viewmodel.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object ViewDIModule {

    val modules = module {
        viewModel {
            EventListViewModel(get())
        }
        viewModel { (eventId: String) ->
            EventDetailsViewModel(get(), eventId)
        }
        viewModel {
            WelcomeViewModel(get())
        }
        viewModel {
            LoginViewModel(get())
        }
        viewModel {
            RegistrationViewModel(get())
        }
    }
}