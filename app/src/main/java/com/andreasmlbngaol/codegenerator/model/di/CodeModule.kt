package com.andreasmlbngaol.codegenerator.model.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.andreasmlbngaol.codegenerator.model.db.CodeDao
import com.andreasmlbngaol.codegenerator.model.db.CodeDatabase
import com.andreasmlbngaol.codegenerator.views.code.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CodeModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    fun provideCodeDatabase(context: Context): CodeDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = CodeDatabase::class.java,
            name = "codes.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCodeDao(codeDatabase: CodeDatabase): CodeDao {
        return codeDatabase.dao
    }

    @Provides
    @Singleton
    fun provideConverters(): Converters {
        return Converters()
    }

}