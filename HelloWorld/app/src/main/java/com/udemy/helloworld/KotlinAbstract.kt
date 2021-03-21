package com.udemy.helloworld

// An abstract class cannot be instantiated
// (you cannot create objects of an abstract class).
// However, you can inherit subclasses from an abstract class.
// The members (properties and methods) of an abstract class are non-abstract
// unless you explicitly use the abstract keyword to make them abstract.
abstract class Mammal(private val name: String, private val origin: String,
    private val weight: Double){    //Concrete (Non Abstract) Properties

    // Abstract property (Must be overridden by subclasses)
    abstract var maxSpeed: Double

    // Abstract Methods (Must be implemented by subclasses)
    abstract fun run()
    abstract fun breath()

    // Concrete (Non Abstract) Methods
    fun displayDetails(){
        println("Name: $name, Origin: $origin, Weight: $weight, MaxSpeed: $maxSpeed")
    }
}

class Human(name: String, origin: String, weight: Double,
            override var maxSpeed: Double): Mammal(name, origin, weight){
    override fun run() {
        // Code to run
        println("Runs on two legs")
    }

    override fun breath() {
        // Code to breath
        println("Breath through mouth or nose")
    }
}

fun main() {
    val human = Human("Denis", "Russia", 70.0, 28.0)

    human.run()
    human.breath()
}