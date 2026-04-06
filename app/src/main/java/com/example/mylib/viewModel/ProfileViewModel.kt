package com.example.mylib.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylib.MainActivity
import com.example.mylib.MainActivity.Companion.loggedInUser
import com.example.mylib.data.models.FollowResponse
import com.example.mylib.data.models.PostResponse
import com.example.mylib.data.models.ProfileResponse
import com.example.mylib.data.models.ReviewResponse
import com.example.mylib.data.repo.FollowingRepository
import com.example.mylib.data.repo.PostRepository
import com.example.mylib.data.repo.ReviewRepository
import com.example.mylib.data.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File


data class ProfileUiState (
    val viewingReviews: Boolean = false,
    val viewingFollowers: Boolean = false,
    val viewingFollowing: Boolean = false,
    val bio: String = "",
    val profilePictureBase64: String? = null,

    val error: String = "",
    val loading: Boolean = false,

    val posts: List<PostReviewItem.PostItem> = emptyList(),
    val amFollowing: Boolean = false,
    val reviews: List<PostReviewItem.ReviewItem> = emptyList(),
    val followers: List<FollowResponse> = emptyList(),
    val following: List<FollowResponse> = emptyList(),

    val profileData: ProfileResponse? = null,
)


class ProfileViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val reviewRepository: ReviewRepository,
    private val followingRepository: FollowingRepository,

) : ViewModel(){

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    var postFlow: Flow<List<PostReviewItem.PostItem>> = flow { emit(List(1,{PostReviewItem.PostItem(
        PostResponse(
            id=-1,
            username="",
            title ="",
            text ="",
            time ="",
            imageType = "png",
            imageBase64 = null,
            profilePic = null,
        ))}))}
    var reviewFlow: Flow<List<PostReviewItem.ReviewItem>> = flow { emit(List(1,{PostReviewItem.ReviewItem(
        ReviewResponse(
            id = -1,
            username = "",
            text = "",
            time = "",
            score = 0.0,
            bookId = -1,
            profilePic = null,
        )
    )}))}

    var followerFlow: Flow<List<FollowResponse>> = flow { emit(List(1,{
        FollowResponse(
        username = ""
    )}))}
    var followingFlow: Flow<List<FollowResponse>> = flow { emit(List(1,{
        FollowResponse(
            username = ""
        )}))}

    var uiPostState: StateFlow<List<PostReviewItem.PostItem>> = postFlow.stateIn(
        scope = viewModelScope,                      // where it lives
        started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
        initialValue = List(1,{ PostReviewItem.PostItem(PostResponse(
            id = -1,
            username = "",
            title="",text="",
            time="",
            imageBase64 = "",
            imageType = "",
            profilePic = null,
        ))}  )
    )
    var uiReviewState: StateFlow<List<PostReviewItem.ReviewItem>> = reviewFlow.stateIn(
        scope = viewModelScope,                      // where it lives
        started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
        initialValue = List(1,{ PostReviewItem.ReviewItem(ReviewResponse(
            id = -1,
            username = "",
            text = "",
            time = "",
            score = 0.0,
            bookId = -1,
            profilePic = null,
        ))}  )
    )

    var uiFollowerState: StateFlow<List<FollowResponse>> = followerFlow.stateIn(
        scope = viewModelScope,                      // where it lives
        started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
        initialValue = List(1,{ FollowResponse(
            username = ""
        )}  )
    )
    var uiFollowingState: StateFlow<List<FollowResponse>> = followingFlow.stateIn(
        scope = viewModelScope,                      // where it lives
        started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
        initialValue = List(1,{ FollowResponse(
            username = ""
        )}  )
    )

    fun setUser(username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                error = "",
                loading = true,
            )

            postRepository.getAccountPosts(username)
            postFlow = postRepository.observePostsByUsername(username)
            uiPostState = postFlow.map{ it.map{
                PostReviewItem.PostItem(PostResponse(
                    id=it.post.id,
                    username = it.post.username,
                    title = it.post.title,
                    text = it.post.text,
                    time = it.post.time,
                    imageBase64 = it.post.imageBase64,
                    imageType = it.post.imageType,
                    profilePic = userRepository.getProfilePicBase64(it.post.username)
                ))
            }
            }.stateIn(
                scope = viewModelScope,                      // where it lives
                started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                initialValue = List(1,{ PostReviewItem.PostItem(PostResponse(
                    id = -1,
                    username = "",
                    title="",text="",
                    time="",
                    imageBase64 = "",
                    imageType = "",
                    profilePic = "",
                ))}  )
            )

            reviewRepository.fetchUserReviews(username)
            reviewFlow = reviewRepository.observeReviewsByUsername(username)
            uiReviewState = reviewFlow.map{ it.map{
                PostReviewItem.ReviewItem(ReviewResponse(
                    id =it.review.id,
                    username = username,
                    bookId = it.review.bookId,
                    text = it.review.text,
                    time = it.review.time,
                    score = it.review.score,
                    profilePic = userRepository.getProfilePicBase64(username)
                ))
            }
            }.stateIn(
                scope = viewModelScope,                      // where it lives
                started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                initialValue = List(1,{ PostReviewItem.ReviewItem(ReviewResponse(
                    id = -1,
                    username = "",
                    text = "",
                    time = null,
                    score = 0.0,
                    bookId = -1,
                    profilePic = "",
                ))}  )
            )

            userRepository.getFollowers(username)
            followerFlow = followingRepository.observeFollowersByUsername(username)
            uiFollowerState = followerFlow.stateIn(
                scope = viewModelScope,                      // where it lives
                started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                initialValue = List(1,{ FollowResponse(
                    username = ""
                )}  )
            )

            userRepository.getFollowing(username)
            followingFlow = followingRepository.observeFollowingByUsername(username)
            uiFollowingState = followingFlow.stateIn(
                scope = viewModelScope,                      // where it lives
                started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                initialValue = List(1,{ FollowResponse(
                    username = ""
                )}  )
            )

            _uiState.value = _uiState.value.copy(
                error = "",
                loading = false,
            )
        }
    }



    fun setViewingFollowers(b: Boolean) {
        _uiState.value = _uiState.value.copy(
            viewingFollowers = b,
        )
        if (b) {
            _uiState.value = _uiState.value.copy(
                viewingFollowing = false,
            )
        }
    }
    fun setViewingFollowing(b: Boolean) {
        _uiState.value = _uiState.value.copy(
            viewingFollowing = b,
        )
        if (b) {
            _uiState.value = _uiState.value.copy(
                viewingFollowers = false,
            )
        }
    }

    fun follow(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(error = "", loading = true)

                userRepository.followAccount(loggedInUser,username)

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    amFollowing = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to follow account"
                )
            }
        }
    }

    fun unfollow(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(error = "", loading = true)

                userRepository.unfollowAccount(loggedInUser,username)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    amFollowing = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to unfollow account"
                )
            }
        }
    }


    fun fetchProfile(username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    error = "",
                    loading = true,
                )


                val data = userRepository.getUserProfile(username);

                _uiState.value = _uiState.value.copy(
                    profileData = data,
                    bio = data.bio?:"No Bio"
                )

                var foundAmFollowing = false
                data.followers?.forEach { follower ->
                    if (follower.username == MainActivity.loggedInUser) {
                        foundAmFollowing = true
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    amFollowing = foundAmFollowing
                )

               // val postsConverted = data.posts?.map { PostReviewItem.PostItem(it) } ?: emptyList()
               // val reviewsConverted = data.reviews?.map { PostReviewItem.ReviewItem(it) } ?: emptyList()

                _uiState.value = _uiState.value.copy(
                   // posts = postsConverted,
                  //  reviews = reviewsConverted,
                    loading = false,
                )

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to load profile"
                )
            }
        }
    }


    fun fetchPosts(username: String, viewingReviews: Boolean = false) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(error = "", loading = true, viewingReviews = viewingReviews)

                postRepository.getAccountPosts(username)
                postFlow = postRepository.observePostsByUsername(username)
                uiPostState = postFlow.map{ it.map{
                    PostReviewItem.PostItem(PostResponse(
                        id=it.post.id,
                        username = it.post.username,
                        title = it.post.title,
                        text = it.post.text,
                        time = it.post.time,
                        imageBase64 = it.post.imageBase64,
                        imageType = it.post.imageType,
                        profilePic = userRepository.getProfilePicBase64(it.post.username)
                    ))
                }
                }.stateIn(
                    scope = viewModelScope,                      // where it lives
                    started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                    initialValue = List(1,{ PostReviewItem.PostItem(PostResponse(
                        id = -1,
                        username = "",
                        title="",text="",
                        time="",
                        imageBase64 = "",
                        imageType = "",
                        profilePic = "",
                    ))}  )
                )

                _uiState.value = _uiState.value.copy(
                    loading = false,
                   // posts = postsConverted,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to load posts"
                )
            }
        }
    }

    fun fetchReviews(username: String, viewingReviews: Boolean = true) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(loading = true, error = "", viewingReviews = viewingReviews)

                reviewRepository.fetchUserReviews(username)
                reviewFlow = reviewRepository.observeReviewsByUsername(username)
                uiReviewState = reviewFlow.map{ it.map{
                    PostReviewItem.ReviewItem(ReviewResponse(
                        id =it.review.id,
                        username = username,
                        bookId = it.review.bookId,
                        text = it.review.text,
                        time = it.review.time,
                        score = it.review.score,
                        profilePic = userRepository.getProfilePicBase64(username)
                    ))
                }
                }.stateIn(
                    scope = viewModelScope,                      // where it lives
                    started = SharingStarted.Eagerly,//SharingStarted.WhileSubscribed(5_000), // keep alive 5 s after last collector
                    initialValue = List(1,{ PostReviewItem.ReviewItem(ReviewResponse(
                        id = -1,
                        username = "",
                        text = "",
                        time = null,
                        score = 0.0,
                        bookId = -1,
                        profilePic = "",
                    ))}  )
                )

                _uiState.value = _uiState.value.copy(
                    loading = false,
                    //reviews = reviews.map { PostReviewItem.ReviewItem(it) },
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "Failed to load reviews"
                )
            }
        }
    }
    fun updateProfilePicture(file: File) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    loading = true,
                    error = ""
                )

                userRepository.updateProfilePicture(loggedInUser, file)

                fetchProfile(MainActivity.loggedInUser)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Failed to update profile picture"
                )
            }
        }
    }
}
