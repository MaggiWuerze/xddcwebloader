/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.maggiwuerze.xdccloader.events

import de.maggiwuerze.xdccloader.model.User

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.HandleAfterDelete
import org.springframework.data.rest.core.annotation.HandleAfterSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.hateoas.EntityLinks
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Component
@RepositoryEventHandler(User::class)
class EventHandler

@Autowired
constructor(private val websocket: SimpMessagingTemplate, private val entityLinks: EntityLinks) {

    val MESSAGE_PREFIX : String = "/topic"

    @HandleAfterCreate
    fun newEmployee(user: User) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newEmployee", getPath(user))
    }

    @HandleAfterDelete
    fun deleteEmployee(user : User) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteEmployee", getPath(user))
    }

    @HandleAfterSave
    fun updateEmployee(user : User) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateEmployee", getPath(user))
    }

    /**
     * Take an [Employee] and get the URI using Spring Data REST's [EntityLinks].
     *
     * @param employee
     */
    private fun getPath(user : User): String {
        return this.entityLinks.linkForSingleResource(user.javaClass,
                user.id).toUri().path
    }

}
// end::code[]
