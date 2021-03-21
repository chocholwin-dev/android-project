package com.udemy.helloworld

// open: In Kotlin all classes, functions, and variables are by defaults final,
// and by inheritance property, we cannot inherit the property of final classes,
// final functions, and data members. So we use the open keyword before the class or
// function or variable to make inheritable that.

interface Drivable{
    val maxSpeed: Double
    fun drive(): String
    fun brake(){
        println("The drivable is braking")
    }
}

// super class, parent class, base class
open class InterfaceCar(override val maxSpeed: Double, val name: String, val brand: String): Drivable {
    open var range: Double = 0.0

    fun extendRange(amount: Double){
        if(amount > 0)
            range += amount
    }

    override fun drive(): String {
        TODO("Not yet implemented")
        return "driving the interface drive"
    }
    // shortest version of above drive function
    //override fun drive(): String = "driving the interface drive"

    open fun drive(distance: Double){
        println("Drove for $distance KM")
    }
}

// sub class, child class, derived class of vehicle
class InterfaceElectricCar(maxSpeed: Double, name: String, brand: String, batteryLife: Double)
    : InterfaceCar(maxSpeed, name, brand){

    var chargerType = "Type1"
    override var range = batteryLife * 6

    override fun drive(distance: Double){
        println("Drove for $distance KM on electricity")
    }

    override fun drive(): String{
        return "Drove for $range KM on electricity"
    }

    override fun brake() {
        super.brake()
    }
}

fun main() {
    var audiA3 = InterfaceCar(200.0, "A3", "Audi")
    var teslaS = InterfaceElectricCar(200.0, "S-Model", "Tesla", 85.0)

    teslaS.chargerType = "Type2"
    println("Charger Type "+teslaS.chargerType)
    //teslaS.extendRange(200.0)
    //teslaS.drive()
    teslaS.brake()
    audiA3.brake()

    // Polymorphism
    //audiA3.drive(200.0)
    //teslaS.drive(200.0)
}