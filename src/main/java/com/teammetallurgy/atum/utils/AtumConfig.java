package com.teammetallurgy.atum.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class AtumConfig {
    public static Configuration config;
    //Categories
    public static final String WORLDGEN = "world gen";
    public static final String OREGEN = AtumConfig.WORLDGEN + Configuration.CATEGORY_SPLITTER + "ore gen";
    public static final String BIOME = "biome";
    public static final String MOBS = "mobs";
    public static final String ATUM_START = "atum start";
    public static final String MOD_INTEGRATION = "mod integration";
    //Config entries
    public static boolean ALLOW_CREATION;
    public static boolean FOG_ENABLED;
    public static boolean PYRAMID_ENABLED;
    public static boolean START_IN_ATUM;
    public static String ATUM_START_STRUCTURE;
    public static boolean START_IN_ATUM_PORTAL;
    public static int DIMENSION_ID;

    public AtumConfig(File file) {
        AtumConfig.config = new Configuration(file);

        MinecraftForge.EVENT_BUS.register(this);
        syncConfigData();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Constants.MOD_ID)) {
            syncConfigData();
        }
    }

    private void syncConfigData() {
        List<String> propOrder = new ArrayList<>();
        Property prop;

        prop = config.get(CATEGORY_GENERAL, "Atum Portal", true);
        prop.setComment("Can a non-creative user create a portal using the scarab?");
        prop.setLanguageKey("atum.configGui.portalCreation");
        ALLOW_CREATION = prop.getBoolean(true);
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Start in Atum", false);
        prop.setComment("New players should start in Atum?");
        prop.setLanguageKey("atum.configGui.atumStart");
        START_IN_ATUM = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Atum starting structure", "atum:tent_small");
        prop.setComment("Structure that will generate next to the player when starting in Atum (Requires 'Start in Atum' to be enabled). Leave empty for no structure.");
        prop.setLanguageKey("atum.configGui.atumStartStructure");
        ATUM_START_STRUCTURE = prop.getString();
        propOrder.add(prop.getName());

        prop = config.get(ATUM_START, "Create Atum Portal", false);
        prop.setComment("Should a portal back to the Overworld generate, when starting in Atum?");
        prop.setLanguageKey("atum.configGui.atumStartPortal");
        START_IN_ATUM_PORTAL = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = config.get(CATEGORY_GENERAL, "Atum Fog", true);
        prop.setComment("Should clientside fog be rendered?");
        prop.setLanguageKey("atum.configGui.fog").setRequiresMcRestart(true);
        FOG_ENABLED = prop.getBoolean(true);
        propOrder.add(prop.getName());

        prop = config.get(CATEGORY_GENERAL, "Atum Dimension ID", 17);
        prop.setComment("The ID of the Atum Dimension");
        prop.setLanguageKey("atum.configGui.dimensionID").setRequiresMcRestart(true);
        DIMENSION_ID = prop.getInt();
        propOrder.add(prop.getName());

        PYRAMID_ENABLED = config.getBoolean("Should Pyramids generate in Atum?", WORLDGEN, true, "Set to true to enable Pyramids");

        config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

        if (config.hasChanged()) {
            config.save();
        }
    }
}