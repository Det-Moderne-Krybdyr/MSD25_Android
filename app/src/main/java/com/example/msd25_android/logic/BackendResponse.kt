package com.example.msd25_android.logic
class BackendResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> create(data: T?, successMsg: String = "OK", errorMsg: String = "ERROR"): BackendResponse<T> {
            val success = (data != null)
            val msg: String = if (success) successMsg else errorMsg
            return BackendResponse(success, msg, data)
        }
    }
}