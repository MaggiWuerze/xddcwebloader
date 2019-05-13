package de.maggiwuerze.xdccloader

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
open class XdccloaderApplication

fun main(args: Array<String>) {
//    runApplication<XdccloaderApplication>(*args)
    SpringApplication.run(XdccloaderApplication::class.java, *args)

}

