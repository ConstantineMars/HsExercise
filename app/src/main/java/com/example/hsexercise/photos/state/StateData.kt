package com.example.hsexercise.photos.state

class StateData<T>(val data: T? = null, val state: State, val error: Throwable? = null) {

    enum class State {
        LOADING,
        ERROR,
        SUCCESS
    }
}