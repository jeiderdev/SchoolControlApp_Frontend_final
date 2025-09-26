package com.example.schoolcontrol.utils
object TokenHolder {
    var tokenType: String? = null
    var accessToken: String? = null

    fun authHeader(): String? {
        return if (!accessToken.isNullOrEmpty()) {
            val type = tokenType ?: "bearer"
            "$type $accessToken"
        } else null
    }
}
