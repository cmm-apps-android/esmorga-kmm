package cmm.esmorga.domain.di

import cmm.esmorga.domain.event.GetEventDetailsUseCase
import cmm.esmorga.domain.event.GetEventDetailsUseCaseImpl
import cmm.esmorga.domain.event.GetEventListUseCase
import cmm.esmorga.domain.event.GetEventListUseCaseImpl
import cmm.esmorga.domain.event.GetMyEventListUseCase
import cmm.esmorga.domain.event.GetMyEventListUseCaseImpl
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.domain.user.GetSavedUserUseCaseImpl
import cmm.esmorga.domain.user.PerformLoginUseCase
import cmm.esmorga.domain.user.PerformLoginUseCaseImpl
import cmm.esmorga.domain.user.PerformRegistrationUserCase
import cmm.esmorga.domain.user.PerformRegistrationUserCaseImpl
import org.koin.dsl.module


object DomainDIModule {

    val module = module {
        factory<GetEventListUseCase> { GetEventListUseCaseImpl(get()) }
        factory<GetMyEventListUseCase> { GetMyEventListUseCaseImpl(get()) }
        factory<GetEventDetailsUseCase> { GetEventDetailsUseCaseImpl(get()) }
        factory<PerformLoginUseCase> { PerformLoginUseCaseImpl(get()) }
        factory<GetSavedUserUseCase> { GetSavedUserUseCaseImpl(get()) }
        factory<PerformRegistrationUserCase> { PerformRegistrationUserCaseImpl(get()) }
    }

}