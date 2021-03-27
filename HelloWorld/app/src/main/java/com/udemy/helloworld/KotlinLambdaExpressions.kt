package com.udemy.helloworld

// Lambda Expression is a function which has no name
// Lambda Expression and Anonymous functions are function literals, i.e. functions that are not declared, but passed immediately as an expression
// Lambda is defined with curly braces {} which takes variables as a parameter (if any) and a body of a function
// The body of a function is written after the variable (if any) followed by -> operator
// Syntax:{variable(s)->body of lambda}

fun main() {
    val sum: (Int, Int) -> Int = {a: Int, b: Int -> a + b}
    println("Sum: " + sum(10, 5))

    // even shorter
    val sum1 = {a:Int, b:Int -> println(10 + 5)}
    sum1(10, 5)
}