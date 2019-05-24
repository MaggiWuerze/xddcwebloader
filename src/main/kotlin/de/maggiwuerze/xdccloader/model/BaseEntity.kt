package de.maggiwuerze.xdccloader.model

import java.time.LocalDateTime

open class BaseEntity (

        val id : Int? = null,
        val dateCreated : LocalDateTime = LocalDateTime.now(),
        val lastUpdated : LocalDateTime = LocalDateTime.now()

)