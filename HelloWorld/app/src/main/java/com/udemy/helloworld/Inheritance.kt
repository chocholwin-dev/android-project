package com.udemy.helloworld

// open: In Kotlin all classes, functions, and variables are by defaults final,
// and by inheritance property, we cannot inherit the property of final classes,
// final functions, and data members. So we use the open keyword before the class or
// function or variable to make inheritable that.

// super class, parent class, base class
open class Car(val name: String, val brand: String) {
    open var range: Double = 0.0

    fun extendRange(amount: Double){
        if(amount > 0)
            range += amount
    }

    open fun drive(distance: Double){
        println("Drove for $distance KM")
    }
}

// sub class, child class, derived class of vehicle
class ElectricCar(name: String, brand: String, batteryLife: Double)
    : Car(name, brand){

    var chargerType = "Type1"
    override var range = batteryLife * 6

    override fun drive(distance: Double){
        println("Drove for $distance KM on electricity")
    }

    fun drive(){
        println("Drove for $range KM on electricity")
    }
}

fun main() {
    var audiA3 = Car("A3", "Audi")
    var teslaS = ElectricCar("S-Model", "Tesla", 85.0)

    teslaS.chargerType = "Type2"
    println("Charger Type "+teslaS.chargerType)
    teslaS.extendRange(200.0)
    teslaS.drive()

    // Polymorphism
    audiA3.drive(200.0)
    teslaS.drive(200.0)
}