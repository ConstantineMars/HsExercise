package com.example.hsexercise.photos.state

/**
 * StateData serves for transfer of both state and data through LiveData subscription
 */

class StateData<T>(val data: T? = null, val state: State, val error: Throwable? = null) {

    enum class State {
        LOADING,
        ERROR,
        DONE
    }
}