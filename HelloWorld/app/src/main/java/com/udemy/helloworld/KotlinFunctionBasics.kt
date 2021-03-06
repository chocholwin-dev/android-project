package com.udemy.helloworld

fun main() {
    //Function
    myFunction()
    //argument
    println(addUp(5,3))
    println(avg(3.2,5.3))
    println("************End Function*************")
    myScopeFunction(4)
}

// The syntax of a function - fun stands for function
fun myFunction(){
    // The body of a function
    println("myFunction was called")
}

// Method : a Method is a Function within a class
// This function has two parameters and returns a value of type Int
fun addUp(a: Int, b: Int): Int{
    // the return keyword indicates that this function will return the following value
    // once this function is called and executed
    return (a+b)
}
// Article on Kotlin words https://blog.kotlin-academy.com/kotlin-programmer-dictionary-function-vs-method-vs-procedure-c0216642ee87
// CHALLENGE: create a function that calculates the average and call it
fun avg(a: Double, b: Double): Double {
    return  (a + b)/2
}

// this a is a parameter
fun myScopeFunction(a: Int){
    // a is a variable
    var a = a
    println("a is a $a")
}