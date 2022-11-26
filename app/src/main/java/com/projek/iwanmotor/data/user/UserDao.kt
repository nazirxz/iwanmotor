
package com.projek.iwanmotor.data.user


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface UserDao {

    @Query("SELECT * from user ORDER BY namaLengkap ASC")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * from user WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing User into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}
