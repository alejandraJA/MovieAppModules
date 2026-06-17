package com.me.domain

data class Resource<out T>(
    val status: Constants.StatusResponse,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> =
            Resource(Constants.StatusResponse.SUCCESS, data, null)

        fun <T> error(msg: String, data: T?): Resource<T> =
            Resource(Constants.StatusResponse.ERROR, data, msg)

        fun <T> loading(data: T?): Resource<T> =
            Resource(Constants.StatusResponse.LOADING, data, null)

        fun <T> error(msg: String): Resource<T> =
            Resource(Constants.StatusResponse.ERROR, null, msg)

    }
}