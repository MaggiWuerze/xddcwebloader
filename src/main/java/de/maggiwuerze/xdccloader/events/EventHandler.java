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
package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.model.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author Daniel Prange
 */
// tag::code[]
@Component
@RepositoryEventHandler(Download.class)
class EventHandler{

    Logger logger = Logger.getLogger("Class EventHandler");

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    private EntityLinks entityLinks;

    final String MESSAGE_PREFIX = "/topic";

    @HandleAfterCreate
    public void newDownload(Download download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newDownload", getPath(download));
        logger.info("created Download with id: " +  download.getId());

    }

    @HandleAfterDelete
    public void deleteDownload(Download download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteDownload", getPath(download));
    }

    @HandleAfterSave
    public void updateDownload(Download download) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateDownload", getPath(download));
        logger.info("saved Download with id: " +  download.getId());
    }

    @HandleAfterLinkSave
    public void afterLinkSave(Download download){

        logger.info("linkSaved Download with id: " +  download.getId());
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/linkSavedDownload", getPath(download));

    }

    /**
     * Take an [Download] and get the URI using Spring Data REST's [EntityLinks].
     *
     * @param download
     */
    private String getPath(Download download){
        return this.entityLinks.linkForSingleResource(download.getClass(),
                download.getId()).toUri().getPath();
    }

}
// end::code[]
