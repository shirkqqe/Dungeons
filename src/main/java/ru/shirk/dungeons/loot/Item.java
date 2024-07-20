package ru.shirk.dungeons.loot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.shirk.dungeons.Dungeons;

public class Item implements ConfigurationSerializable {
    @Getter
    private final int chance;
    @Getter
    private final String name;
    @Getter
    private final String material;
    @Getter
    private final List<String> lore;
    @Getter
    private final Map<String, Integer> enchantments;
    @Getter
    private final int amount;

    public Item(int chance, String name, String material, List<String> lore, Map<String, Integer> enchantments, int amount) {
        this.chance = chance;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.enchantments = enchantments;
        this.amount = amount;
    }

    public static Item deserialize(Map<String, Object> map) {
        return new Item((Integer) map.get("chance"), (String) map.get("name"), (String) map.get("material")
                , (List) map.get("lore"), (Map) map.get("enchantments"), (Integer) map.get("amount"));
    }

    public final @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap();
        map.put("name", this.name);
        map.put("material", this.name);
        map.put("lore", this.lore);
        map.put("enchantments", this.enchantments);
        map.put("amount", this.amount);
        map.put("chance", this.chance);
        return map;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.valueOf(this.material));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.setLore(this.lore);
        itemStack.setItemMeta(itemMeta);
        for(Map.Entry<String, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = Enchantment.getByName(entry.getKey());
            if (enchantment == null) {
                Dungeons.getInstance().getSLF4JLogger().warn("{} is not enchantment", entry.getKey());
            } else {
                itemStack.addUnsafeEnchantment(enchantment,entry.getValue());
            }
        }
        itemStack.setAmount(this.amount);
        return itemStack;
    }
}
