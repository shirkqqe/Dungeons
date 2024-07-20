package ru.shirk.dungeons.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.shirk.dungeons.Dungeons;
import ru.shirk.dungeons.dungeons.Dung;
import ru.shirk.dungeons.loot.Item;

public class Configuration {
    private final JavaPlugin p;
    private FileConfiguration config = null;
    private File file = null;
    private final String name;

    public Configuration(JavaPlugin plugin, String absolutePath) {
        this.p = plugin;
        this.name = absolutePath;
    }

    public void reload() {
        if (this.file == null) {
            this.file = new File(this.p.getDataFolder(), this.name);
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            Dungeons.getConfigurationManager().createFile(this.name);
        }

        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new File(this.name));
        this.config.setDefaults(defConfig);
    }

    public FileConfiguration getFile() {
        if (this.config == null) {
            this.reload();
        }

        return this.config;
    }

    public void saveDefaultConfig() {
        if (this.file == null) {
            this.file = new File(this.name);
        }

        if (!this.file.exists()) {
            this.p.saveResource(this.name, false);
        }

    }

    public void save() {
        if (this.config != null && this.file != null) {
            try {
                this.getFile().save(this.file);
            } catch (IOException e) {
                this.p.getLogger().log(Level.SEVERE, "Could not save config to " + this.file, e);
            }

        }
    }

    public String c(String name) {
        String caption = this.getFile().getString(name);
        if (caption == null) {
            this.p.getLogger().warning("No such language caption found: " + name);
            caption = "&c[No language caption found]";
        }

        return ChatColor.translateAlternateColorCodes('&', caption);
    }

    public int ch(String name) {
        return this.getFile().getInt(name);
    }

    public List<Item> il(String name) {
        return (List<Item>) this.getFile().getList(name);
    }

    public List<String> cl(String name) {
        List<String> captionlist = new ArrayList<>();
        for(String s : captionlist) {
            captionlist.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        if (this.getFile().getStringList(name) == null) {
            this.p.getLogger().warning("No such language caption found: " + name);
            captionlist.add(ChatColor.translateAlternateColorCodes('&', "&c[No language caption found]"));
        }

        return captionlist;
    }

    public List<Material> ml(String name) {
        List<Material> materials = new ArrayList<>();

        for(String materialName : getFile().getStringList(name)) {
            if(Material.getMaterial(materialName) == null) {
                Dungeons.getInstance().getSLF4JLogger().warn("Material {} not found!", materialName);
                continue;
            }
            materials.add(Material.getMaterial(materialName));
        }

        return materials;
    }

    public World wd(String name) {
        return Bukkit.getWorld(this.c(name)) == null ? null : Bukkit.getWorld(this.c(name));
    }

    public List<Dung> dl(String name) {
        return (List<Dung>) this.getFile().getList(name);
    }
}
