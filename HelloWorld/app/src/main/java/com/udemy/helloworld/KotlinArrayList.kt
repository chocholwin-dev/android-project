package com.udemy.helloworld

fun main() {
    // Empty ArrayList
    val emptyArrayList = ArrayList<String>()    // Creating an empty arraylist
    emptyArrayList.add("One")   // Adding an object in arraylist
    emptyArrayList.add("Two")
    println(".......print ArrayList......")
    for (i in emptyArrayList){
        println(i)
    }

    // ArrayList Using Collections
    val collectionArrayList: ArrayList<String> = ArrayList<String>(5)
    var list: MutableList<String> = mutableListOf<String>()
    list.add("One")
    list.add("Two")

    collectionArrayList.addAll(list)

    println(".......print ArrayList......")
    for(i in collectionArrayList){
        println(i)
    }

    // Filled elements in ArrayList using Collections
    val itr = collectionArrayList.iterator()

    while (itr.hasNext()){
        println(itr.next())
    }
    println("Size of arraylist = ${collectionArrayList.size}")

    // ArrayList get()
    println("......print collectionArrayList.get(1)......")
    println(collectionArrayList.get(1))

    // ArrayList exercise
    val doubleArrayList = ArrayList<Double>()
    doubleArrayList.add(12.98232)
    doubleArrayList.add(10.22132)
    doubleArrayList.add(3.84343)
    doubleArrayList.add(23.2323)
    doubleArrayList.add(21.3323)

    var total: Double = 0.0
    for(i in doubleArrayList){
        total += i
    }
    var average = total / doubleArrayList.size
    println("Average is $average")
}