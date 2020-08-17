package h.code.country

import spark.Spark

fun main() {
    println("now starting...")
    Spark.get("/ping") { request, response -> "alive" }
}