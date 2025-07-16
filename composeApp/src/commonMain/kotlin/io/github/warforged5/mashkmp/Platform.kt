package io.github.warforged5.mashkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform