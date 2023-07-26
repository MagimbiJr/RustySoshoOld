package dev.rustybite.rustysosho.utils

import android.content.Context
import android.content.res.Resources

class ResourceProvider(private val resource: Resources) {
    fun getString(id: Int) = resource.getString(id)
}