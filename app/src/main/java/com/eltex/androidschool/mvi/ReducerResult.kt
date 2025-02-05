package com.eltex.androidschool.mvi

data class ReducerResult<State, Effect>(
    val newState: State,
    val effects: Set<Effect>,
) {
    constructor(newState: State, action: Effect? = null) : this(newState, setOfNotNull(action))
}
