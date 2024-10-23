package com.payroll.twogrowhr.di

import android.content.Context
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.retrofit.APIInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.text.Typography.dagger

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun getRepository(context: Context): Repository {
        return Repository(mAPIInterface = getHRService(retrofit = getRetrofitBuilder(context = context)))
    }

    @Singleton
    @Provides
    fun getGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Singleton
    @Provides
    fun getHRService(retrofit: Retrofit): APIInterface {
        return retrofit.create(APIInterface::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofitBuilder(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
    }
}