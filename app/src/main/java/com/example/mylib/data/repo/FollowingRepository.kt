package com.example.mylib.data.repo

import com.example.mylib.data.models.FollowRequest
import com.example.mylib.data.remote.UserApi
import com.example.mylib.data.repo.Dao.FollowingDao
import kotlinx.coroutines.flow.Flow

class FollowingRepository(
    private val followingDao: FollowingDao,
    private val api: UserApi
) {

    suspend fun follow(loggedinUser: String, username: String) {
        api.followAccount(FollowRequest(username))

        followingDao.insert(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )
    }

    suspend fun unfollow(loggedinUser: String, username: String) {
        api.unfollowAccount(FollowRequest(username))

        followingDao.delete(
            Following(
                followingUsername = loggedinUser,
                followedUsername = username
            )
        )
    }

    suspend fun refreshFollowing(loggedinUser: String) {
        val remoteFollowing = api.getFollowing(loggedinUser)

        val followingEntities = remoteFollowing.map { followResponse ->
            Following(
                followingUsername = loggedinUser,
                followedUsername = followResponse.username
            )
        }

        followingDao.clearAndInsert(loggedinUser, followingEntities)
    }

    fun observeFollowing(loggedinUser: String): Flow<List<Following>> {
        return followingDao.getFollowedUsers(loggedinUser)
    }

    fun observeFollowers(loggedinUser: String): Flow<List<Following>> {
        return followingDao.getFollowingUsers(loggedinUser)
    }
}