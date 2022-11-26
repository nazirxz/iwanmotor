package com.projek.iwanmotor.data

import com.projek.iwanmotor.data.user.User
import kotlinx.coroutines.flow.Flow

class Repository(private val localDataSource: LocalDataSource) {
    val users = localDataSource.getUsers()
    suspend fun insert(user: User) {
        return localDataSource.insert(user)
    }
    suspend fun getUser(id: Int): Flow<User> {
        return localDataSource.getUser(id)
    }
}