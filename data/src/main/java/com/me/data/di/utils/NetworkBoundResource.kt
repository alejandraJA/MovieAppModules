package com.me.data.di.utils

import com.me.data.remote.api.ApiResponse
import com.me.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow(): Flow<Resource<ResultType>> = flow {
        emit(Resource.loading(null))

        val dbValue = loadFromDb().firstOrNull()

        if (shouldFetch(dbValue)) {
            emit(Resource.loading(dbValue))

            when (val response = createCall()) {
                is ApiResponse.ApiSuccessResponse -> {
                    saveCallResult(processResponse(response))
                    emitAll(loadFromDb().map { Resource.success(it) })
                }

                is ApiResponse.ApiEmptyResponse -> {
                    emitAll(loadFromDb().map { Resource.success(it) })
                }

                is ApiResponse.ApiErrorResponse -> {
                    onFetchFailed()
                    emitAll(loadFromDb().map { Resource.error(response.errorMessage, it) })
                }
            }
        } else {
            emitAll(loadFromDb().map { Resource.success(it) })
        }
    }.flowOn(Dispatchers.IO)

    protected open fun onFetchFailed() {}

    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestType>): RequestType =
        response.body

    protected abstract suspend fun saveCallResult(response: RequestType)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): Flow<ResultType>

    protected abstract suspend fun createCall(): ApiResponse<RequestType>
}