package com.pkliang.poi.core.view

sealed class StateData {
    object Loading : StateData()
    object Uninitialized : StateData()
    data class Success(var data: Any) : StateData() { inline fun <reified T> responseTo() = data as T }
    data class Error(val error: Throwable) : StateData() { inline fun <reified T> errorTo() = error as T }
}
