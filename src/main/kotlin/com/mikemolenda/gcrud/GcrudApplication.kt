package com.mikemolenda.gcrud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GcrudApplication

fun main(args: Array<String>) {
    runApplication<GcrudApplication>(*args)
}
