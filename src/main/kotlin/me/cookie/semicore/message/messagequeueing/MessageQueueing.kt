package me.cookie.semicore.message.messagequeueing

import me.cookie.semicore.SemiCore
import me.cookie.semicore.message.dialogue.Dialogue
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

private val messageQueue = mutableListOf<QueuedMessage>()
val dialogueQueue = mutableListOf<Dialogue>()

class MessageQueueing {
    private val plugin = JavaPlugin.getPlugin(SemiCore::class.java)

    private val garbage = mutableSetOf<Any>()

    fun startRunnable(){
        object: BukkitRunnable() {
            override fun run() {


                if(messageQueue.isNotEmpty()) {
                    messageQueue.forEach messageQueueLoop@ { message ->
                        // Check if it's time for a message to be sent
                        if(System.currentTimeMillis() < message.whenToSend) return@messageQueueLoop

                        when(message.receiver){
                            MessageReceiver.GLOBAL -> {
                                Bukkit.broadcast(message.message)
                            }
                            MessageReceiver.PLAYER -> {
                                message.playerToSend!!.sendMessage(message.message)
                            }
                            MessageReceiver.PLAYERS -> {
                                message.playersToSend!!.forEach { player ->
                                    player.sendMessage(message.message)
                                }
                            }
                        }
                        message.isSent = true
                        garbage.add(message)
                    }
                    messageQueue.removeAll(garbage)
                }

                if(dialogueQueue.isNotEmpty()){
                    dialogueQueue.forEach{ dialogue ->
                        dialogue.messages.forEach dialogueMessageLoop@ { message ->
                            if(message.isSent) return@dialogueMessageLoop // That part of the dialogue was already sent
                            if(System.currentTimeMillis() >= message.whenToSend) return@dialogueMessageLoop
                        }
                    }
                }


                garbage.clear()
            }

        }.runTaskTimer(plugin, 0, 10 /* half a second delay, for accuracy */)
    }
}

fun Player.queueMessage(message: Component, seconds: Int){
    messageQueue.add(
        QueuedMessage(
            message = message,
            MessageReceiver.PLAYER,
            whenToSend = System.currentTimeMillis() + (seconds.toLong() * 1000),
            this
        )
    )
}