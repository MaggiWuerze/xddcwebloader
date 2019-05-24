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

import de.maggiwuerze.xdccloader.model.Download
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.HandleAfterDelete
import org.springframework.data.rest.core.annotation.HandleAfterSave
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.hateoas.EntityLinks
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

/**
 * @author Daniel Prange
 */
// tag::code[]
@Component
@RepositoryEventHandler(Download::class)
class EventHandler
@Autowired
constructor(private val websocket: SimpMessagingTemplate, private val entityLinks: EntityLinks) {

    val MESSAGE_PREFIX: String = "/topic"

    @HandleAfterCreate
    fun newDownload(download: Download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newDownload", getPath(download))

        println("created Download with id: " +  download.id)

    }

    @HandleAfterDelete
    fun deleteDownload(download: Download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteDownload", getPath(download))
    }

    @HandleAfterSave
    fun updateDownload(download: Download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateDownload", getPath(download))
        println("saved Download with id: " +  download.id)
    }

    /**
     * Take an [Download] and get the URI using Spring Data REST's [EntityLinks].
     *
     * @param Download
     */
    private fun getPath(download: Download): String {
        return this.entityLinks.linkForSingleResource(download.javaClass,
                download.id).toUri().path
    }

}
// end::code[]
