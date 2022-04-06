package com.example.mvvmalarm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CustomPoint::class],
    version = 1)
abstract class PointDatabase: RoomDatabase() {
    abstract fun pointDao(): PointDAO

    companion object{
        private var INSTANCE: PointDatabase? = null

        fun getInstance(context: Context): PointDatabase {
            if (INSTANCE == null) {
                synchronized(PointDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                        PointDatabase::class.java, "point.db").allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}