package me.cookie.cookiecore.message.dialogue

import me.cookie.cookiecore.inDialogue
import me.cookie.cookiecore.message.messagequeueing.MessageReceiver
import me.cookie.cookiecore.message.messagequeueing.QueuedMessage
import me.cookie.cookiecore.message.messagequeueing.dialogueQueue
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class Dialogue(
    var messages: MutableList<QueuedMessage> = mutableListOf(),
    private val receiver: MessageReceiver = MessageReceiver.GLOBAL,
    var whenToSend: Long =  System.currentTimeMillis(),
    var playerToSend: Player = messages[0].playerToSend!!,
    var playersToSend: List<Player> = listOf(),
    val muteForAfter: Int = 0,
    val toRun: Runnable = Runnable { },
) {

    init {
        for(message in messages){
            val component = Component.text("[DIALOGUE]").append(message.message)
            message.message = component
        }
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
    this.inDialogue = true
}