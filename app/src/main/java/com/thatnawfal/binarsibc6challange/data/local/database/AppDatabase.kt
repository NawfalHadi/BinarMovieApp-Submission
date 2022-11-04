package com.thatnawfal.binarsibc6challange.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao

    companion object {
        private const val DB_NAME = "local_storage.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase: ByteArray = SQLiteDatabase.getBytes("db_notes-hashed".toCharArray())
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
