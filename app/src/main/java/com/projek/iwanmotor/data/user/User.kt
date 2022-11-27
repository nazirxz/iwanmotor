package com.projek.iwanmotor.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Entity data class represents a single row in the database.
 */
@Entity
class User : Serializable {
    //declaration of user table columns
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var namaLengkap: String? = null

    var email: String? = null

    var password: String? = null

}