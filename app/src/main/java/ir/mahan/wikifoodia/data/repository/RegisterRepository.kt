package ir.mahan.wikifoodia.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ir.mahan.wikifoodia.data.source.RemoteDataSource
import ir.mahan.wikifoodia.models.register.LocalRegisterData
import ir.mahan.wikifoodia.ui.register.BodyRegister
import ir.mahan.wikifoodia.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class RegisterRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remote: RemoteDataSource
) {
    suspend fun postRegisterUser(body: BodyRegister) = remote.postRegisterUser(body)

    // DataStore keys
    private object DataStoreKeys {
        val username = stringPreferencesKey(Constants.DATASTORE_KEY_USERNAME)
        val hash = stringPreferencesKey(Constants.DATASTORE_KEY_HASH)
    }

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(Constants.REGISTER_DATASTORE_KEY)

    // Save Register Data
    suspend fun saveRegisterData(username: String, hash: String) {
        context.dataStore.edit {
            it[DataStoreKeys.username] = username
            it[DataStoreKeys.hash] = hash
        }
    }

    val registerData : Flow<LocalRegisterData> = context.dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            val username = it[DataStoreKeys.username] ?:  ""
            val hash = it[DataStoreKeys.hash] ?: ""
            LocalRegisterData(
                username = username,
                hash = hash
            )
        }
}