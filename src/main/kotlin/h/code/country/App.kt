package h.code.country

import spark.Spark

const val webRootPath = "hcc"

fun main() {
    println("now starting...")
    Spark.staticFiles.location("/web")
    Spark.get("/$webRootPath/ping") { _, _ -> "alive" }
}