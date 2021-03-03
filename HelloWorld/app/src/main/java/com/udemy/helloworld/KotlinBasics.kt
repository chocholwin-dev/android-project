package com.udemy.helloworld

fun main(){
    // TODO: Add new functionality
    //immutable variable
    val myName : String = "Kyaw Zin"
    //mutable variable
    var myName1 : String = "Cho Lwin"
    val long : Long = 39_812_309_487_120_300
    val intValue : Short = 23_12

    val myStr : String = "Hello World!"
    val myLastChar : Char = myStr[myStr.length - 1]
    val myFirstChar : Char = myStr[0]
    println("My First Character "+myFirstChar)
    println("My Last Character "+myLastChar)
    myName1 = "Cho Cho"
    println("Immutable variable : "+myName)
    println("Mutable variable : "+myName1)

    // Arithmetic operator (+, -, %, /, *)
    var result = 5 + 3
    val a = 5
    val b = 3
    val c = 4.0
    result /= 2
    var resultDouble : Double
    resultDouble = a / c
    println("Result $result")
    println("Result Double $resultDouble")

    // Comparison Operator (==, !=, <=, >=)
    val isEqual = 3!=2
    println("isEqual is $isEqual")
    println("isLowerEqual3 ${-4<=3}")
    println("is5isGreaterEqual5 ${5>=5}")

    //Assignment Operators (+=, -+, /=, *=)
    var myNum = 5
    myNum += 3
    myNum /= 2
    myNum *= 3
    println("My Num $myNum")

    //Increment & Decrement Operators (++, --)
    myNum++
    println("My Num $myNum")
    println("My Num ${myNum++}")
    println("My Num ${++myNum}")
    println("My Num ${--myNum}")
}