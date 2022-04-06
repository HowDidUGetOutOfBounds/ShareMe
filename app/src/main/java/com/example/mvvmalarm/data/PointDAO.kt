package com.example.mvvmalarm.data

import androidx.room.*
import androidx.room.FtsOptions.Order
import kotlinx.coroutines.flow.Flow


@Dao
interface PointDAO {
    @Query("SELECT * FROM custompoint")
    fun getAllPoints(): Flow<List<CustomPoint>>

    @Query("SELECT * FROM custompoint WHERE message = :firstParam")
    fun getByMessage(firstParam: String): CustomPoint
//
//    @Query(
//        "SELECT * FROM custompoint WHERE lat = :firstParam" +
//                " AND lon = :secondParam"
//    )
//    fun getByLocation(firstParam: Double, secondParam: Double): CustomPoint

//    @Query("SELECT * FROM custompoint WHERE date = :firstParam")
//    fun getByDate(firstParam: String): CustomPoint

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoint(point: CustomPoint)

    @Delete
    fun deletePoint(point: CustomPoint)

    @Query ("SELECT * FROM custompoint ORDER BY date DESC LIMIT 1")
    fun getLastRecord(): CustomPoint

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(point: CustomPoint)

    @Query("DELETE FROM custompoint")
    fun deleteAll()
}