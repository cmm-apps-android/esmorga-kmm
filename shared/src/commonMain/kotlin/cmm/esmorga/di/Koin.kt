package cmm.esmorga.di

import cmm.esmorga.data.di.DataDIModule
import cmm.esmorga.datasource.local.di.LocalDIModule
import cmm.esmorga.datasource.remote.di.RemoteDIModule
import cmm.esmorga.domain.di.DomainDIModule
import cmm.esmorga.viewmodel.eventdetails.EventDetailsViewModel
import cmm.esmorga.viewmodel.eventlist.EventListViewModel
import cmm.esmorga.viewmodel.changepassword.ChangePasswordViewModel
import cmm.esmorga.viewmodel.login.LoginViewModel
import cmm.esmorga.viewmodel.myevents.MyEventsViewModel
import cmm.esmorga.viewmodel.profile.ProfileViewModel
import cmm.esmorga.viewmodel.registration.RegistrationViewModel
import cmm.esmorga.viewmodel.welcome.WelcomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

val sharedKoinModules = listOf(
    DataDIModule.module,
    DomainDIModule.module,
    RemoteDIModule.module,
    LocalDIModule.module
)


val viewModelModule = module {
    viewModelOf(::EventListViewModel)
    viewModel { (eventId: String) ->
        EventDetailsViewModel(get(), get(), get(), get(), eventId)
    }
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::MyEventsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ChangePasswordViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(sharedKoinModules + viewModelModule)
    }
}