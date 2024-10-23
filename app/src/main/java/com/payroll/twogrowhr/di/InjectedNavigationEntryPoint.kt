package com.payroll.twogrowhr.di

import com.payroll.twogrowhr.repository.Repository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InjectedNavigationEntryPoint {
    val repository: Repository
}