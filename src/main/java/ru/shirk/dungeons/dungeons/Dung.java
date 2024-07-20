package ru.shirk.dungeons.dungeons;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class Dung implements ConfigurationSerializable {
    @Getter
    private final double x;
    @Getter
    private final double y;
    @Getter
    private final double z;
    @Getter
    private final String world;

    public Dung(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap();
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("z", this.z);
        map.put("world", this.world);
        return map;
    }

    public static Dung deserialize(Map<String, Object> map) {
        return new Dung((Double)map.get("x"), (Double)map.get("y"), (Double)map.get("z"), (String)map.get("world"));
    }
}
