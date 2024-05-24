package com.example.suicidergame.presentation.utils

sealed class UIState<T> {
    class Init<T> : UIState<T>()
    class Error<T>(val error: String) : UIState<T>()
    class Success<T>(val data: T) : UIState<T>()
}
