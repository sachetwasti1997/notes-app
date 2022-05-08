package com.sachet.notes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sachet.notes.db.UserCredDatabase
import com.sachet.notes.db.UserCredRepository
import com.sachet.notes.network.NotesRepository
import com.sachet.notes.network.NotesApi
import com.sachet.notes.network.UserApi
import com.sachet.notes.network.UserRepository
import com.sachet.notes.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesUserCredDatabase(application: Application): UserCredDatabase{
        return Room.databaseBuilder(
            application,
            UserCredDatabase::class.java,
            UserCredDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserCredRepository(db: UserCredDatabase): UserCredRepository{
        return UserCredRepository(db.userCredDao)
    }

    @Singleton
    @Provides
    fun provideUserRepository(userApi: UserApi) = UserRepository(userApi)

    @Singleton
    @Provides
    fun provideUserApi(): UserApi{
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit
            .Builder()
            .baseUrl(Constants.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNotesRepository(notesApi: NotesApi) = NotesRepository(notesApi)

    @Singleton
    @Provides
    fun providesNotesApi():NotesApi{
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NotesApi::class.java)
    }
}