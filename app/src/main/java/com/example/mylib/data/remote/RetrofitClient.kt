package com.example.mylib.data.remote

import com.example.mylib.MainActivity
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient


class OAuthInterceptor(private val tokenType: String):
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()

        println("access token in header: "+ MainActivity.bearerToken)
        request = request.newBuilder().
        header("Authorization", "${tokenType} ${MainActivity.bearerToken}")
            .build()

        return chain.proceed(request)
    }
}
val client =  OkHttpClient.Builder()
    .addInterceptor(OAuthInterceptor("Bearer"))
    .build()


object RetrofitClient{
    private const val API_URL = "https://mylib-15dd.onrender.com/"

    private val retrofit: Retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    }

    val authenticationApi: AuthenticationApi by lazy{retrofit.create(AuthenticationApi::class.java)}
    val bookApi: BookApi by lazy{retrofit.create(BookApi::class.java)}
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java)}

    val reviewApi: ReviewApi by lazy { retrofit.create(ReviewApi::class.java)}
    val postApi: PostApi by lazy {retrofit.create(PostApi::class.java)}
    val listApi: ListApi by lazy {retrofit.create(ListApi::class.java)}

}