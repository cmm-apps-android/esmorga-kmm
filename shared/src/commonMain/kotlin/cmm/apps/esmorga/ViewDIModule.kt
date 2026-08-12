package cmm.apps.esmorga

import cmm.apps.viewmodel.eventdetails.EventDetailsViewModel
import cmm.apps.viewmodel.eventlist.EventListViewModel
import cmm.apps.viewmodel.login.LoginViewModel
import cmm.apps.viewmodel.registration.RegistrationViewModel
import cmm.apps.viewmodel.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ViewDIModule {

    val modules = module {
        viewModelOf(::EventListViewModel)
        viewModel { (eventId: String) ->
            EventDetailsViewModel(get(), eventId)
        }
        viewModelOf(::WelcomeViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegistrationViewModel)
    }
}
