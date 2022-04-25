package com.sachet.notes.di

import com.sachet.notes.network.NotesRepository
import com.sachet.notes.network.NotesApi
import com.sachet.notes.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNotesRepository(notesApi: NotesApi) = NotesRepository(notesApi)

    @Singleton
    @Provides
    fun providesNotesApi():NotesApi{
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotesApi::class.java)
    }
}