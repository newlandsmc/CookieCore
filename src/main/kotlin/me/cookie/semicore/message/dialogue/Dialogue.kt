package me.cookie.semicore.message.dialogue

import me.cookie.semicore.message.messagequeueing.MessageReceiver
import me.cookie.semicore.message.messagequeueing.QueuedMessage
import me.cookie.semicore.message.messagequeueing.dialogueQueue
import org.bukkit.entity.Player

class Dialogue(
    var messages: List<QueuedMessage> = mutableListOf(),
    var receiver: MessageReceiver = MessageReceiver.GLOBAL,
    var whenToSend: Long =  System.currentTimeMillis(),
    var playerToSend: Player? = null,
    var playersToSend: List<Player> = listOf()
) {
    init {
        when(receiver){
            MessageReceiver.PLAYER -> {
                if(playerToSend == null) {
                    throw NullPointerException("You forgot to add a player to QueuedMessage")
                }
            }
            MessageReceiver.PLAYERS -> {
                if(playersToSend.isEmpty()) {
                    throw NullPointerException("Player list is empty, no one to send these messages to")
                }
            }
            else -> {
                // GLOBAL receiver does not need additional variables
            }
        }
    }
}

fun Player.queueDialogue(dialogue: Dialogue) {
    dialogueQueue.add(dialogue)
}