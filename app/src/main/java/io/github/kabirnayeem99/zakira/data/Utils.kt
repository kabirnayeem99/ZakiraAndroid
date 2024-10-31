package io.github.kabirnayeem99.zakira.data

import io.github.kabirnayeem99.zakira.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

inline fun <T> asResourceFlow(
    crossinline action: suspend () -> T,
): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    val result = action()
    emit(Resource.Success(result))
}.catch { e ->
    emit(Resource.Error(e.message))
}

inline fun <T> asResourceFlowFromFlow(
    crossinline action: suspend () -> Flow<T>
): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    action().collect { result ->
        emit(Resource.Success(result))
    }
}.catch { e ->
    emit(Resource.Error(e.message))
}