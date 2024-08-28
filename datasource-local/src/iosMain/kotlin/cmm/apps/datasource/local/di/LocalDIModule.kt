package cmm.apps.datasource.local.di

import database.EsmorgaDatabaseHelper

actual fun getDatabase() = EsmorgaDatabaseHelper.getDatabase()