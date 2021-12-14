package me.cookie.semicore.message.dialogue

import me.cookie.semicore.message.messagequeueing.MessageReceiver
import me.cookie.semicore.message.messagequeueing.QueuedMessage
import me.cookie.semicore.message.messagequeueing.dialogueQueue
import org.bukkit.entity.Player

class Dialogue(
    var messages: MutableList<QueuedMessage> = mutableListOf(),
    var receiver: MessageReceiver = MessageReceiver.GLOBAL,
    var whenToSend: Long =  System.currentTimeMillis(),
    var playerToSend: Player = messages[0].playerToSend!!,
    var playersToSend: List<Player> = listOf()
) {
    init {
        when(receiver){
            MessageReceiver.PLAYER -> {
                // PLAYER receiver does not need additional variables, grabs it from the first QueuedMessage since it's
                // required for it to have it
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