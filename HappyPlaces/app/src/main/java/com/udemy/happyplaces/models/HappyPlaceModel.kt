package com.udemy.happyplaces.models

// (Step 2 : Creating a Data Model Class with all the assumed values that Happy Place details will consists of. Will you this data class in all over the project even when dealing with local SQLite database. Here the latitude and longitude will be used later on.)
// START
/**
 * A Data Model Class for Happy Place details. We will you this data class in all over the project even when
 * dealing with local SQLite database.
 */
data class HappyPlaceModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
)
// END