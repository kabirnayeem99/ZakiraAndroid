package io.github.kabirnayeem99.zakira.core


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Failed<T> : Resource<T>()
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success(data=$data)"
            is Failed -> "Failed"
            is Loading -> "Loading(data=$data)"
            is Error -> "Error(message=$message, data=$data)"
        }
    }
}
