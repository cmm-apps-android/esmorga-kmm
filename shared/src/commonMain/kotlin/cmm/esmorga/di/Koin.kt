package cmm.esmorga.di

import cmm.esmorga.data.di.DataDIModule
import cmm.esmorga.datasource.local.di.LocalDIModule
import cmm.esmorga.datasource.remote.di.RemoteDIModule
import cmm.esmorga.domain.di.DomainDIModule
import cmm.esmorga.viewmodel.eventattendees.EventAttendeesViewModel
import cmm.esmorga.viewmodel.eventdetails.EventDetailsViewModel
import cmm.esmorga.viewmodel.explore.ExploreViewModel
import cmm.esmorga.viewmodel.changepassword.ChangePasswordViewModel
import cmm.esmorga.viewmodel.createevent.CreateEventSession
import cmm.esmorga.viewmodel.createevent.CreateEventStep1ViewModel
import cmm.esmorga.viewmodel.createevent.CreateEventStep2ViewModel
import cmm.esmorga.viewmodel.createevent.CreateEventStep3ViewModel
import cmm.esmorga.viewmodel.createevent.CreateEventStep4ViewModel
import cmm.esmorga.viewmodel.login.LoginViewModel
import cmm.esmorga.viewmodel.myevents.MyEventsViewModel
import cmm.esmorga.viewmodel.profile.ProfileViewModel
import cmm.esmorga.viewmodel.registration.RegistrationViewModel
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
    viewModelOf(::ExploreViewModel)
    viewModel { (eventId: String) ->
        EventDetailsViewModel(get(), get(), get(), get(), eventId)
    }
    viewModel { (eventId: String) ->
        EventAttendeesViewModel(get(), get(), get(), eventId)
    }
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::MyEventsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ChangePasswordViewModel)
    
    single { CreateEventSession() }
    viewModelOf(::CreateEventStep1ViewModel)
    viewModelOf(::CreateEventStep2ViewModel)
    viewModelOf(::CreateEventStep3ViewModel)
    viewModelOf(::CreateEventStep4ViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(sharedKoinModules + viewModelModule)
    }
}
