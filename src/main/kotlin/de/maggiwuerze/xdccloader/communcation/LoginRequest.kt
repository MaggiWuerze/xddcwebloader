package de.maggiwuerze.xdccloader.communcation

import de.maggiwuerze.xdccloader.model.Channel
import de.maggiwuerze.xdccloader.model.Server
import de.maggiwuerze.xdccloader.model.User
import java.time.LocalDateTime

class LoginRequest(creationDate: LocalDateTime, server: Server, channel: Channel, user: User) : BaseRequest(creationDate, server, channel, user)
