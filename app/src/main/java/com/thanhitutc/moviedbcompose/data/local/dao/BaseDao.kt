package com.thanhitutc.moviedbcompose.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Created by ThanhDNV
 */
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: T)

    /**
//     * Insert an array of objects in the database.
//     * update if exists, otherwise will insert
//     *
//     * @param obj the objects to be inserted.
//     */
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vararg obj: T)

    /**
     * Insert an list of objects in the database.
     * update if exists, otherwise will insert
     *
     * @param obj the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(obj: List<T>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllWithIgnoreConflict(obj: List<T>)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update
    suspend fun update(obj: T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete
    suspend fun delete(obj: T)

}
