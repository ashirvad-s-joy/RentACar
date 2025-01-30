package com.inte.assignment.data

class User {
    var name: String? = null
    var speed: Int = 0

    constructor()

    constructor(name: String?, age: Int) {
        this.name = name
        this.speed = age
    }
}
