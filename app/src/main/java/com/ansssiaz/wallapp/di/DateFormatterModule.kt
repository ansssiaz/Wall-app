package com.ansssiaz.wallapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.ansssiaz.wallapp.utils.DateFormatter
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@Module
@InstallIn(SingletonComponent::class)
class DateFormatterModule {
    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter(ZoneId.systemDefault())

    @Provides
    fun provideFormatter(dateFormatter: DateFormatter): DateTimeFormatter {
        return dateFormatter.getFormatter()
    }
}