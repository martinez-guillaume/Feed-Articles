package com.example.feedarticlesjetpack


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.feedarticlesjetpack.network.ApiArticleInterface
import com.example.feedarticlesjetpack.network.ApiArticleRoutes
import com.example.feedarticlesjetpack.network.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiArticleRoutes.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiCountryInterface(retrofit: Retrofit): ApiArticleInterface {
        return retrofit.create(ApiArticleInterface::class.java)
    }

    @Singleton
    @Provides   //  garantit que le contexte a la portée de l'application entière et ne sera pas détruit avec une activité.
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepository(sharedPreferences)
    }
}