package me.cookie.cookiecore.commands

import me.cookie.cookiecore.inDialogue
import me.cookie.cookiecore.message.dialogue.Dialogue
import me.cookie.cookiecore.message.messagequeueing.dialogueQueue
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class SkipDialogue(private val plugin: JavaPlugin): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){
            sender.sendMessage("Only players are allowed to execute this command")
        }
        val player = sender as Player
        if(!player.hasPermission("semicore.dialogue.skip")){
            player.sendMessage(
                MiniMessage.get().parse(
                    plugin.config.getString("no-permission")!!
                )
            )
            return true
        }

        val garbage = mutableListOf<Dialogue>()

        dialogueQueue.forEach dialogueLoop@ { dialogue ->
            if(dialogue.playerToSend.uniqueId == sender.uniqueId){
                dialogue.messages.forEach { message ->
                    dialogue.playerToSend.sendMessage(message.message)
                }
                garbage.add(dialogue)
                return@dialogueLoop
            }
            dialogue.playersToSend.forEach { loopPlayer ->
                if(loopPlayer.uniqueId == player.uniqueId){
                    dialogue.messages.forEach { message ->
                        loopPlayer.sendMessage(message.message)
                    }
                    garbage.add(dialogue)
                    return@dialogueLoop
                }
            }
        }

        dialogueQueue.removeAll(garbage)
        object: BukkitRunnable() { // Prevents the chat messages from buggin out
            override fun run() {
                player.inDialogue = false
            }
        }.runTaskLaterAsynchronously(plugin, 20)

        return true
    }
}