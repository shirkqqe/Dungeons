package ru.shirk.dungeons.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.tools.Utils;

public class InteractListener implements Listener {
    @EventHandler
    public void onInteractDung(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Dungeons.getDungManager().isDungeon(event.getClickedBlock().getLocation())) {
            if (event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("key"), PersistentDataType.INTEGER)) {
                event.getItem().setAmount(event.getItem().getAmount() - 1);
                event.setCancelled(false);
                Bukkit.broadcastMessage(Dungeons.getConfigurationManager().getConfig("lang.yml").c("broadcast.open").replace("%player%", player.getName()));
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
                Utils.playParticle(event.getClickedBlock().getLocation());
                ShulkerBox shulkerBox = (ShulkerBox)event.getClickedBlock().getState();
                Utils.generateLoot(shulkerBox.getInventory());
                Dungeons.getDungManager().removeDung(event.getClickedBlock().getLocation());
            } else {
                event.setCancelled(true);
                player.sendMessage(Dungeons.getConfigurationManager().getConfig("lang.yml").c("feedback.closed"));
            }
        }
    }
}
