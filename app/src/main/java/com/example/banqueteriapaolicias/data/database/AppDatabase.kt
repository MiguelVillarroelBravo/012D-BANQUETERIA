package com.example.banqueteriapaolicias.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.banqueteriapaolicias.data.local.dao.UsuarioDao
import com.example.banqueteriapaolicias.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val INSERT_ADMIN =
            "INSERT INTO users (nombre, correo, telefono, clave, rol) " +
                    "VALUES ('Administrador', 'admin@paolicias.cl', '900000000', 'admin123', 'ADMIN')"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE users_new (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "nombre TEXT NOT NULL, " +
                            "correo TEXT NOT NULL, " +
                            "telefono TEXT NOT NULL, " +
                            "clave TEXT NOT NULL, " +
                            "rol TEXT NOT NULL)"
                )
                db.execSQL(
                    "INSERT INTO users_new (id, nombre, correo, telefono, clave, rol) " +
                            "SELECT id, nombre, correo, telefono, clave, 'CLIENTE' FROM users"
                )
                db.execSQL("DROP TABLE users")
                db.execSQL("ALTER TABLE users_new RENAME TO users")
                db.execSQL(INSERT_ADMIN)
            }
        }

        private val SEED_CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(INSERT_ADMIN)
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "banqueteria_database.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(SEED_CALLBACK)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}