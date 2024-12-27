package com.commu.mobile.di

import android.content.Context
import androidx.room.Room
import com.commu.mobile.data.db.AppDatabase
import com.commu.mobile.data.db.ChatDao
import com.commu.mobile.data.network.ChatApi
import com.commu.mobile.data.network.MessageApi
import com.commu.mobile.data.network.UserApi
import com.commu.mobile.data.network.WebSocketClient
import com.commu.mobile.data.reposotiry.ChatRepoImpl
import com.commu.mobile.data.reposotiry.ChatRepository
import com.commu.mobile.data.reposotiry.MessageRepoImpl
import com.commu.mobile.data.reposotiry.MessageRepository
import com.commu.mobile.data.reposotiry.UserRepository
import com.commu.mobile.data.db.MessageDao
import com.commu.mobile.data.db.UserDao
import com.commu.mobile.utils.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val baseUrlLocalRealDevice = "http://192.168.0.82:8080"
        return Retrofit.Builder()
            .baseUrl(baseUrlLocalRealDevice)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageApi(retrofit: Retrofit): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWebsocket(): WebSocketClient {
        return WebSocketClient()
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        chatApi: ChatApi,
        chatDao: ChatDao
    ): ChatRepository {
        return ChatRepoImpl(chatApi, chatDao)
    }

    @Singleton
    @Provides
    fun provideMessageRepo(messageApi: MessageApi, messageDao: MessageDao): MessageRepository {
        return MessageRepoImpl(messageApi, messageDao)
    }

    @Singleton
    @Provides
    fun provideSharedPreferencesHelper(@ApplicationContext context: Context): SharedPreferencesHelper {
        return SharedPreferencesHelper(context);
    }

    @Singleton
    @Provides
    fun provideUserRepo(
        sh: SharedPreferencesHelper,
        userApi: UserApi,
        userDao: UserDao
    ): UserRepository {
        return UserRepository(sh, userApi, userDao)
    }

}