package de.maggiwuerze.xdccloader.communication;

import de.maggiwuerze.xdccloader.model.Channel
import de.maggiwuerze.xdccloader.model.Server
import de.maggiwuerze.xdccloader.model.User
import org.pircbotx.Configuration
import org.pircbotx.PircBotX




class RequestFactory{

    fun debug(debugMessage : String) {
        println("[DEBUG] $debugMessage")
    }

    fun createLoginRequest(server: Server, channel: Channel, user: User){

        val config = Configuration.Builder()
                .setName("MyExampleBot") //Nick of the bot. CHANGE IN YOUR CODE
                .setLogin("PircBotXUser") //Login part of hostmask, eg name:login@host
                .setAutoNickChange(true) //Automatically change nick when the current one is in use
                .addAutoJoinChannel("#pircbotx") //Join #pircbotx channel on connect
                .buildConfiguration() //Create an immutable configuration from this builder

        val myBot = PircBotX(config)

        println("[DEBUG] created login request")
    }

    
}