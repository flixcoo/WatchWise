package com.github.nullsafe.watchwise.core.data.error

import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName(Name.CODE) val code: Int?,
    @SerializedName(Name.ERROR) val error: String?
)