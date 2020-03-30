package com.paras.contactsmvvm.models

data class BaseModel(
    var isLoading: Boolean = false,

    var error: Throwable? = null,

    var response: Any? = null
)