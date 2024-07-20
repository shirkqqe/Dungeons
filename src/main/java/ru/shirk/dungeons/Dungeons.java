package ru.shirk.dungeons;

import java.io.File;
import java.util.Objects;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import ru.shirk.dungeons.commands.Commands;
import ru.shirk.dungeons.commands.GenerateDunges;
import ru.shirk.dungeons.configs.ConfigurationManager;
import ru.shirk.dungeons.dungeons.Dung;
import ru.shirk.dungeons.dungeons.DungManager;
import ru.shirk.dungeons.listeners.InteractListener;
import ru.shirk.dungeons.loot.Item;

public final class Dungeons extends JavaPlugin {
    @Getter
    private static Dungeons instance;
    @Getter
    private static final ConfigurationManager configurationManager = new ConfigurationManager();
    @Getter
    private static final DungManager dungManager = new DungManager();

    public Dungeons() {
    }

    public void onEnable() {
        instance = this;
        ConfigurationSerialization.registerClass(Dung.class);
        ConfigurationSerialization.registerClass(Item.class);
        loadConfigs();
        dungManager.loadDungeons();
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        Objects.requireNonNull(this.getCommand("dungeons")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("generatedungeons")).setExecutor(new GenerateDunges());
        Objects.requireNonNull(this.getCommand("dungeons")).setTabCompleter(new Commands());
    }

    public void onDisable() {
        dungManager.clear();
        instance = null;
    }

    private void loadConfigs() {
        try {
            if (!(new File(this.getDataFolder(), "settings.yml")).exists()) {
                getConfigurationManager().createFile("settings.yml");
            }
            if (!(new File(this.getDataFolder(), "lang.yml")).exists()) {
                getConfigurationManager().createFile("lang.yml");
            }
            if (!(new File(this.getDataFolder(), "dungeons.yml")).exists()) {
                getConfigurationManager().createFile("dungeons.yml");
            }
            if (!(new File(this.getDataFolder(), "loot.yml")).exists()) {
                getConfigurationManager().createFile("loot.yml");
            }
            File dir = new File(this.getDataFolder(),"/schematics");
            if(!dir.exists()) {
                dir.mkdir();
            }
        } catch (Exception e) {
            this.getLogger().warning(e.getMessage());
        }

    }
}
