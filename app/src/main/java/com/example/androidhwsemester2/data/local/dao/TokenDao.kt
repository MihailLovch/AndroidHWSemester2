package com.example.androidhwsemester2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidhwsemester2.data.local.entity.TokenEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface TokenDao {

    @Insert
    fun saveToken(token: TokenEntity)

    @Query("select token from tokens where id = (SELECT max(id) from tokens)")
    fun getToken(): Single<String>
}