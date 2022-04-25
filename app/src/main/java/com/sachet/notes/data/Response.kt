package com.sachet.notes.data

import java.lang.Exception

data class Response<T, Boolean, E: Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var exception: E? = null
)
