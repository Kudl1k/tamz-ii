package cz.kudladev.tamziikmp.weather.network

enum class NetworkError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}


class NetworkException(val error: NetworkError): Exception(
    //message = "An error occured when translating: $error"
)
