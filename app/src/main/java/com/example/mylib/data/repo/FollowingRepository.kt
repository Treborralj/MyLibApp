package com.example.mylib.data.repo

import com.example.mylib.data.remote.UserApi
import com.example.mylib.data.repo.Dao.FollowingDao
import kotlinx.coroutines.flow.Flow

class FollowingRepository (
    private val followingDao: FollowingDao,
    private val api: UserApi
    ){

    suspend fun follow(loggedinUser: String, username: String){
        val hashMap = HashMap<String, Any>()
        hashMap["username"] = username

        api.follow(hashMap)
        followingDao.insert(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )
    }


    suspend fun unfollow(loggedinUser: String, username: String){
        val hashMap = HashMap<String, Any>()
        hashMap["username"] = username

        api.unfollow(hashMap)
        followingDao.delete(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )

    }

    suspend fun refreshFollowing(loggedinUser: String){
        val remoteFollowing = api.getFollowing(loggedinUser)
        followingDao.clearAndInsert(loggedinUser, remoteFollowing)
    }

    fun observeFollowing(loggedinUser: String): Flow<List<Following>> {
        return followingDao.getFollowedUsers(loggedinUser)
    }

    fun observeFollowers(loggedinUser: String): Flow<List<Following>> {
        return followingDao.getFollowingUsers(loggedinUser)
    }




}