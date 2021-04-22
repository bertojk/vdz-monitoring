package com.example.vdzmonitoring.data.network

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T? {
        val response = call.invoke()

        when {
            response.isSuccessful -> {
                return response.body()
            }
            else -> {
                val error = response.errorBody()?.string()
                val message = StringBuilder()
                error?.let {
                    try {
                        message.append(JSONObject(it).getString("message"))
                    } catch (e: JSONException) { }
                    message.append("\n")
                }

                message.append("Error Code: ${response.code()}")
                Log.e("ERROR", message.toString())
                throw IOException(message.toString())
            }
        }
    }

}