package com.example.mylib.data.repo

import com.example.mylib.data.models.LoginRequest
import com.example.mylib.data.models.LoginResponse
import com.example.mylib.data.models.SignupRequest
import com.example.mylib.data.models.SignupResponse
import com.example.mylib.data.remote.AuthenticationApi
import com.example.mylib.data.repo.Dao.BookListDao

class AuthenticationRepository(
    private val api: AuthenticationApi,
    private val dao: BookListDao
){
    suspend fun login(username: String, password: String): LoginResponse{


        dao.insert(BookList(listId = 0, owner = username, type = "wantToRead"))
        dao.insert(BookList(listId = 0, owner = username, type = "amReading"))
        dao.insert(BookList(listId = 0, owner = username, type = "haveRead"))

        return api.login(LoginRequest(username, password))
    }
    suspend fun signup(username: String, password: String): SignupResponse {
        return api.signup(SignupRequest(username, password))
    }

    suspend fun getLoggedIn(): Map<String, String> {
        return api.getLoggedIn()
    }
}