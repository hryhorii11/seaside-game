package com.example.suicidergame.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [
        ScoreEntity::class
    ]
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDataBase : RoomDatabase() {


    abstract fun getScoreDao(): ScoreDao

    companion object {
        private const val DATABASE_NAME = "your_database_name"


        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}
