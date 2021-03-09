package com.udemy.helloworld

// The primary constructor needs to have at least one parameter.
// All primary constructor parameters need to be marked as val or var.
// Data classes cannot be abstract, open, sealed or inner.
data class User(val id: Int, var name: String)

fun main() {
    var user1 = User(1, "Mochi")

    val name = user1.name
    println("name $name")
    user1.name = "モチ"

    val user2 = User(1, "モチ")
    println(user1 == user2)

    println("User Details $user1")

    val updatedUser = user1.copy(name = "Mochi Mochi")
    println(user1)
    println(updatedUser)

    println(updatedUser.component1())   // prints 1
    println(updatedUser.component2())   // prints Mochi Mochi

    val(id,name1) = updatedUser
    // is equal to
    //val id = updatedUser.id
    //val name1 = updatedUser.name
    print("id $id, name $name1")
}
