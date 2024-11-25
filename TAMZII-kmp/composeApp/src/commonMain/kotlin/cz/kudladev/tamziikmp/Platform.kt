package cz.kudladev.tamziikmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform