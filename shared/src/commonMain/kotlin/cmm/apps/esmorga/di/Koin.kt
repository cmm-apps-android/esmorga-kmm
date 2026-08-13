package cmm.apps.esmorga.di

import cmm.apps.data.di.DataDIModule
import cmm.apps.datasource.local.di.LocalDIModule
import cmm.apps.datasource.remote.di.RemoteDIModule
import cmm.apps.domain.di.DomainDIModule
import cmm.apps.viewmodel.eventdetails.EventDetailsViewModel
import cmm.apps.viewmodel.eventlist.EventListViewModel
import cmm.apps.viewmodel.login.LoginViewModel
import cmm.apps.viewmodel.registration.RegistrationViewModel
import cmm.apps.viewmodel.welcome.WelcomeViewModel
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
        EventDetailsViewModel(get(), eventId)
    }
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(sharedKoinModules + viewModelModule)
    }
}