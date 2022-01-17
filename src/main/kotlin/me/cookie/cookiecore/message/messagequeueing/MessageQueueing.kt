package me.cookie.cookiecore.message.messagequeueing

import me.cookie.cookiecore.inDialogue
import me.cookie.cookiecore.message.dialogue.Dialogue
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

private val messageQueue = mutableListOf<QueuedMessage>()
val dialogueQueue = mutableListOf<Dialogue>()

class MessageQueueing(private val plugin: JavaPlugin) {
    private val messageGarbage = mutableSetOf<QueuedMessage>()
    private val dialogueMessageGarbage = mutableSetOf<QueuedMessage>()
    private val dialogueGarbage = mutableSetOf<Dialogue>()

    fun startRunnable(){
        object: BukkitRunnable() {
            override fun run() {
                val loopTime = System.currentTimeMillis() // To keep time consistent in between inner loops
                if(messageQueue.isNotEmpty()) {
                    messageQueue.forEach messageQueueLoop@ { message ->
                        // Check if it's time for a message to be sent
                        if(loopTime < message.whenToSend) return@messageQueueLoop

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
                        messageGarbage.add(message)
                    }
                    messageQueue.removeAll(messageGarbage)
                    messageGarbage.clear()
                }

                if(dialogueQueue.isNotEmpty()){
                    dialogueQueue.forEach dialogueLoop@ { dialogue ->
                        if(dialogue.messages.isEmpty()){
                            dialogueGarbage.add(dialogue)

                            if(dialogue.muteForAfter > 0){
                                object: BukkitRunnable(){
                                    override fun run() {
                                        dialogue.cleanup()
                                    }
                                }.runTaskLater(plugin, (dialogue.muteForAfter*20).toLong())
                            }else{
                                dialogue.cleanup()
                            }

                            return@dialogueLoop
                        }
                        dialogue.messages.forEach dialogueMessageLoop@ { message ->
                            if(loopTime < message.whenToSend) return@dialogueMessageLoop
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

                            dialogueMessageGarbage.add(message)
                        }
                        dialogue.messages.removeAll(dialogueMessageGarbage)
                        dialogueMessageGarbage.clear()
                    }
                    dialogueQueue.removeAll(dialogueGarbage)
                    dialogueGarbage.clear()
                }
            }

        }.runTaskTimer(plugin, 0, 20)
    }

    fun Dialogue.cleanup(){
        this.playerToSend.inDialogue = false
        this.playersToSend.forEach { player ->
            player.inDialogue = false
        }
        this.toRun.run()
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