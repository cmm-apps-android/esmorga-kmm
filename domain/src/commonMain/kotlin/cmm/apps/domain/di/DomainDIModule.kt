package cmm.apps.domain.di

import cmm.apps.domain.event.GetEventDetailsUseCase
import cmm.apps.domain.event.GetEventDetailsUseCaseImpl
import cmm.apps.domain.event.GetEventListUseCase
import cmm.apps.domain.event.GetEventListUseCaseImpl
import cmm.apps.domain.user.GetSavedUserUseCase
import cmm.apps.domain.user.GetSavedUserUseCaseImpl
import cmm.apps.domain.user.PerformLoginUseCase
import cmm.apps.domain.user.PerformLoginUseCaseImpl
import cmm.apps.domain.user.PerformRegistrationUserCase
import cmm.apps.domain.user.PerformRegistrationUserCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
        factory<PerformLoginUseCase> { PerformLoginUseCaseImpl(get()) }
        factory<GetSavedUserUseCase> { GetSavedUserUseCaseImpl(get()) }
        factory<PerformRegistrationUserCase> { PerformRegistrationUserCaseImpl(get()) }
    }

}