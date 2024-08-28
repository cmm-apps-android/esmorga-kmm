package cmm.apps.datasource.local.di

import cmm.apps.datasource.local.database.EsmorgaDatabase
import cmm.apps.datasource.local.database.EsmorgaDatabaseHelper

actual fun getDatabase(): EsmorgaDatabase = EsmorgaDatabaseHelper.getDatabase()


