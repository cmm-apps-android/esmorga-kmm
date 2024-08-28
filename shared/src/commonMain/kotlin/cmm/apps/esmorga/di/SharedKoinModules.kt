package cmm.apps.esmorga.di

import cmm.apps.data.di.DataDIModule
import cmm.apps.datasource.local.di.LocalDIModule
import cmm.apps.datasource.remote.di.RemoteDIModule
import cmm.apps.domain.di.DomainDIModule
import org.koin.core.module.Module

val sharedKoinModules = listOf(
    DataDIModule.module,
    DomainDIModule.module,
    RemoteDIModule.module,
    LocalDIModule.module
)