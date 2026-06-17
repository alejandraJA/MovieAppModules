package com.me.data.di.utils

import com.me.data.datasource.remote.api.ApiResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class FlowCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, Flow<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Flow<ApiResponse<R>> = callbackFlow {
        call.enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                trySend(ApiResponse.create(response))
                close()
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                trySend(ApiResponse.Companion.create(throwable))
                close()
            }
        })

        awaitClose { call.cancel() }
    }
}