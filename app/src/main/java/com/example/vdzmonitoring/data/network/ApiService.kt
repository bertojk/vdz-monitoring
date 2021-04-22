package com.example.vdzmonitoring.data.network

import com.example.vdzmonitoring.data.entities.*
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://vdztraffic.com/vdzapp/"

private const val REGISTER = "register.php"
private const val LOGIN = "login.php"

private const val USERS = "users"
private const val ROUTES = "routes"
private const val VDZ = "vdz"
private const val LOGS = "logs"
private const val DETAILS = "details"


interface ApiService {

    @POST(REGISTER)
    suspend fun userSignUp(
        @Body u: User
    ) : Response<User>

    @FormUrlEncoded
    @POST(LOGIN)
    suspend fun userLogIn(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Response<User>

    @GET("$ROUTES/.json")
    suspend fun fetchRoutes() : Response<List<Route>>

    @GET("$VDZ/{routeID}.json")
    suspend fun fetchVDZs(
        @Path("routeID") routeID: Long
    ) : Response<List<VDZ>>

    @PUT("$LOGS/{uid}/{id}.json")
    suspend fun putLog(
        @Path("uid") uid: String,
        @Path("id") routeLogId: Long,
        @Body log: RouteLog
    ) : Response<RouteLog>

    @GET("$LOGS/{uid}.json")
    suspend fun getLogs(
        @Path("uid") uid: String
    ) : Response<List<RouteLog>>

    @PUT("$DETAILS/{uid}/{id}.json")
    suspend fun putLogDetails(
        @Path("uid") uid: String,
        @Path("id") routeLogDetailId: Long,
        @Body log: RouteLogDetail
    ) : Response<RouteLogDetail>

    @GET("$DETAILS/{uid}.json")
    suspend fun getLogDetails(
        @Path("uid") uid: String
    ) : Response<List<RouteLogDetail>>

    companion object {
        operator fun invoke(): ApiService {
            val okHttpClient = OkHttpClient.Builder().build()

            val url = HttpUrl.get(BASE_URL)
                .newBuilder()
                .build()

            val moshi = Moshi.Builder()
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(url)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiService::class.java)
        }
    }
}