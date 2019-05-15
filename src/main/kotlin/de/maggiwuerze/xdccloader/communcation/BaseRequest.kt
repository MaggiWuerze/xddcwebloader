package de.maggiwuerze.xdccloader.communcation;

import de.maggiwuerze.xdccloader.model.Channel
import de.maggiwuerze.xdccloader.model.Server
import de.maggiwuerze.xdccloader.model.User
import java.time.LocalDateTime

open class BaseRequest(

        var creationDate: LocalDateTime,

        var server: Server,

        var channel: Channel,

        var user: User

)
