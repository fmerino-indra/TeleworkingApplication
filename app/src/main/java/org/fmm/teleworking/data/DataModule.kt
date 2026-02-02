package org.fmm.teleworking.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fmm.teleworking.data.database.repository.DBCalendarRepository
import org.fmm.teleworking.data.database.room.TeleworkingDatabase
import org.fmm.teleworking.data.network.api.ApiService
import org.fmm.teleworking.data.network.repository.CalendarRepository
import org.fmm.teleworking.domain.repository.ICalendarRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideICalendarRepository(@ApplicationContext context: Context): ICalendarRepository {
        return  DBCalendarRepository(db = TeleworkingDatabase.getInstance(context))
    }

}