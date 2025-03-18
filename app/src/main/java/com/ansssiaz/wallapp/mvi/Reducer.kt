package com.ansssiaz.wallapp.mvi

interface Reducer <State, Effect, Message> {
    fun reduce(old: State, message: Message): ReducerResult<State, Effect>
}