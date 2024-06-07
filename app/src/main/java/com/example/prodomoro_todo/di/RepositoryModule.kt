package com.example.prodomoro_todo.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.prodomoro_todo.data.repository.AuthRepository
import com.example.prodomoro_todo.data.repository.AuthRepositoryImplementation
import com.example.prodomoro_todo.data.repository.TaskRepository
import com.example.prodomoro_todo.data.repository.TaskRepositoryImplementation
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImplementation(auth,database,appPreferences,gson)
    }

    @Provides
    @Singleton
    fun ProvidesTaskRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
    ): TaskRepository {
        return TaskRepositoryImplementation(auth,database)
    }
}