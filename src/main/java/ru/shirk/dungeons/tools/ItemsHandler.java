package ru.shirk.dungeons.tools;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.shirk.dungeons.Dungeons;

public class ItemsHandler {
    public static boolean giveItem(Player player, int amount) {
        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Dungeons.getConfigurationManager().getConfig("settings.yml").c("item.name"));
        itemMeta.setLore(Dungeons.getConfigurationManager().getConfig("settings.yml").cl("item.lore"));
        itemMeta.getPersistentDataContainer().set(NamespacedKey.minecraft("key"), PersistentDataType.INTEGER, 1);
        itemStack.setItemMeta(itemMeta);
        itemStack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
        itemStack.setAmount(amount);
        if (player.getInventory().getSize() == 36) {
            return false;
        } else {
            player.getInventory().addItem(itemStack);
            return true;
        }
    }
}
