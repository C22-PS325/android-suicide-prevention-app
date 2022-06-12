package com.example.suicidepreventiveapp.data.service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.suicidepreventiveapp.data.preferences.UserPreferences
import com.example.suicidepreventiveapp.data.remote.response.RefreshTokenResponse
import com.example.suicidepreventiveapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccessTokenWorker (private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        val refreshToken = inputData.getString(REFRESH_TOKEN)
        return getRefreshToken(refreshToken)
    }

    private fun getRefreshToken(token: String?) : Result {
        val service = ApiConfig.getApiService().getAccessToken(token!!)

        service.enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: Response<RefreshTokenResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val userPreferences = UserPreferences(context)
                    result?.accessToken?.let { userPreferences.setRefreshToken(it) }
                    resultStatus = Result.success()
                } else {
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                resultStatus = Result.failure()
            }

        })

        return resultStatus as Result
    }

    companion object {
        private val TAG = AccessTokenWorker::class.java.simpleName
        const val REFRESH_TOKEN = "refresh_token"

    }

}