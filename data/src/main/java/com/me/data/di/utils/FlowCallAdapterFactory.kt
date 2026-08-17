package com.me.data.di.utils

import com.me.data.datasource.remote.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory : CallAdapter.Factory() {
    @Throws(TypeMustBeResourceException::class, ResourceMustBeParameterizedException::class)
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<out Any, out Any>? {
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != ApiResponse::class.java)
            throw TypeMustBeResourceException()
        if (observableType !is ParameterizedType)
            throw ResourceMustBeParameterizedException()
        val bodyType = getParameterUpperBound(0, observableType)
        return FlowCallAdapter(bodyType)
    }
}

