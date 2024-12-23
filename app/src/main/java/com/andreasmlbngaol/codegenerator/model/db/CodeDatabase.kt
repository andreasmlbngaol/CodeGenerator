package com.andreasmlbngaol.codegenerator.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andreasmlbngaol.codegenerator.views.code.Converters

@Database(
    entities = [Code::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CodeDatabase: RoomDatabase() {

    abstract val dao: CodeDao

}