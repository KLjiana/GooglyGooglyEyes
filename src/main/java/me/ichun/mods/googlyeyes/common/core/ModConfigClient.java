package me.ichun.mods.googlyeyes.common.core;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModConfigClient {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.BooleanValue acidTripEyes = BUILDER
            .define("acidTripEyes", false);
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledGoogly = BUILDER
            .defineList("disabledGoogly", new ArrayList<>(), o -> true);
    public static ForgeConfigSpec.IntValue googlyEyeChance = BUILDER
            .defineInRange("googlyEyeChance", 20, 0, 100);
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> entityOverrideChance = BUILDER
            .defineList("entityOverrideChance", new ArrayList<>(), o -> true);
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> nameOverride = BUILDER
            .defineList("nameOverride", new ArrayList<>(), o -> true);
    public static ForgeConfigSpec.BooleanValue horseEasterEgg = BUILDER
            .define("horseEasterEgg", false);
    public static ForgeConfigSpec.IntValue aggressiveHeadTracking = BUILDER
            .defineInRange("aggressiveHeadTracking", 1, 0, 2);
    public static ForgeConfigSpec SPEC = BUILDER.build();

    public static HashMap<ResourceLocation, Integer> entityOverrideChanceParsed = new HashMap<>();

//    private void onConfigLoad(ModConfigEvent.Loading event) {
//        if (sameConfig(event)) {
//            checkForChanges();
//            onConfigLoaded();
//        }
//    }
//
//    public boolean sameConfig(ModConfigEvent event) {
//        return event.getConfig().getFileName().equals(ModLoadingContext.get().getActiveContainer().getModId() + ".toml");
//    }
//
//    private void onConfigReload(ModConfigEvent.Reloading event) {
//        if (sameConfig(event)) {
//            checkForChanges();
//            onConfigLoaded();
//        }
//    }

    public static void onConfigLoaded() {
        Minecraft.getInstance().execute(ModConfigClient::parseOverrideChance);
    }

    public static void parseOverrideChance() {
        entityOverrideChanceParsed.clear();

        for (String s : entityOverrideChance.get()) {
            String[] split = s.split(",");
            entityOverrideChanceParsed.put(new ResourceLocation(split[0]), Integer.parseInt(split[1]));
        }
    }
}
