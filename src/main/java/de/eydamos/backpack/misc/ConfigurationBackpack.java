package de.eydamos.backpack.misc;

import java.io.File;

import de.eydamos.backpack.helper.BackpackHelper;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationBackpack {
    public static Configuration config;

    public static int BACKPACK_SLOTS_S;
    public static int BACKPACK_SLOTS_L;
    public static int MAX_BACKPACK_AMOUNT;
    public static boolean ALTERNATIVE_ENDER_RECIPE;
    public static boolean RENDER_BACKPACK_MODEL;
    public static boolean OPEN_ONLY_PERSONAL_BACKPACK;
    public static boolean AIRSHIP_MOD_COMPATIBILITY;
    public static boolean DISABLE_BACKPACKS;
    public static boolean DISABLE_MEDIUM_BACKPACKS;
    public static boolean DISABLE_BIG_BACKPACKS;
    public static boolean DISABLE_ENDER_BACKPACKS;
    public static boolean DISABLE_WORKBENCH_BACKPACKS;
    public static boolean BIG_BY_UPGRADE_ONLY;
    public static boolean DIRECT_HIGHER_TIER;
    public static boolean DIRECT_HIGHER_MATERIAL;
    public static boolean SKIP_UPGRADE;
    public static String DISALLOW_ITEMS;
    public static String BACKPACKS_S;
    public static String BACKPACKS_M;
    public static String BACKPACKS_L;
    public static String MATERIALS;

    public static void init(File configFile) {
        if(config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    public static void loadConfiguration() {
        ALTERNATIVE_ENDER_RECIPE = config.get(Configuration.CATEGORY_GENERAL, "enderRecipe", false, getEnderRecipeComment()).getBoolean(false);
        // TODO remove config option
        BACKPACK_SLOTS_S = config.get(Configuration.CATEGORY_GENERAL, "backpackSlotsS", 27, getBackpackSlotComment(), 1, 128).getInt();
        if(BACKPACK_SLOTS_S < 1 || BACKPACK_SLOTS_S > 128) {
            BACKPACK_SLOTS_S = 27;
        }
        // TODO remove config option
        BACKPACK_SLOTS_L = config.get(Configuration.CATEGORY_GENERAL, "backpackSlotsL", 54, getBackpackSlotComment(), 1, 128).getInt();
        if(BACKPACK_SLOTS_L < 1 || BACKPACK_SLOTS_L > 128) {
            BACKPACK_SLOTS_L = 54;
        }
        MAX_BACKPACK_AMOUNT = config.get(Configuration.CATEGORY_GENERAL, "maxBackpackAmount", 0, getMaxBackpackAmountComment(), 0, 36).getInt();
        if(MAX_BACKPACK_AMOUNT < 0 || MAX_BACKPACK_AMOUNT > 36) {
            MAX_BACKPACK_AMOUNT = 0;
        }
        RENDER_BACKPACK_MODEL = config.get(Configuration.CATEGORY_GENERAL, "renderBackpackModel", true, getRenderBackpackModelComment()).getBoolean(true);
        OPEN_ONLY_PERSONAL_BACKPACK = config.get(Configuration.CATEGORY_GENERAL, "openOnlyWornBackpacks", false, getOpenOnlyPersonalBackpacksComment()).getBoolean(false);
        AIRSHIP_MOD_COMPATIBILITY = config.get(Configuration.CATEGORY_GENERAL, "airshipModCompatibility", false, getAirshipModCompatibilityComment()).getBoolean(false);

        DISABLE_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableBackpacks", false, getDisableBackpacksComment()).getBoolean(false);
        DISABLE_MEDIUM_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableMediumBackpacks", false, getDisableMediumBackpacksComment()).getBoolean(false);
        DISABLE_BIG_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableBigBackpacks", false, getDisableBigBackpacksComment()).getBoolean(false);
        DISABLE_ENDER_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableEnderBackpack", false, getDisableEnderBackpacksComment()).getBoolean(false);
        DISABLE_WORKBENCH_BACKPACKS = config.get(Configuration.CATEGORY_GENERAL, "disableWorkbenchBackpack", false, getDisableWorkbenchBackpacksComment()).getBoolean(false);

        DIRECT_HIGHER_TIER = config.get(Configuration.CATEGORY_GENERAL, "directHigherTier", false, getDirectHigherTierComment()).getBoolean(false);
        DIRECT_HIGHER_MATERIAL = config.get(Configuration.CATEGORY_GENERAL, "directHigherMaterial", false, getDirectHigherMaterialComment()).getBoolean(false);
        SKIP_UPGRADE = config.get(Configuration.CATEGORY_GENERAL, "skipUpgrade", false, getSkipUpgradeComment()).getBoolean(false);

        DISALLOW_ITEMS = config.get(Configuration.CATEGORY_GENERAL, "disallowItems", "", getDisallowItemsComment()).getString();
        BACKPACKS_S = config.get(Configuration.CATEGORY_GENERAL, "backpacks_small", "18,36").getString();
        BACKPACKS_M = config.get(Configuration.CATEGORY_GENERAL, "backpacks_medium", "36,54").getString();
        BACKPACKS_L = config.get(Configuration.CATEGORY_GENERAL, "backpacks_large", "54").getString();
        MATERIALS = config.get(Configuration.CATEGORY_GENERAL, "backpack_materials", "iron").getString();

        if(config.hasChanged()) {
            config.save();
        }

        BackpackHelper.init();
    }

    private static String getEnderRecipeComment() {
        return "##############\n" + "Recipe to craft ender backpack\n" + "0 ender chest\n" + "1 eye of the ender\n" + "##############";
    }

    private static String getBackpackSlotComment() {
        return "##############\n" + "Number of slots a backpack has\n" + "valid: integers 1-128\n" + "##############";
    }

    private static String getMaxBackpackAmountComment() {
        return "##############\n" + "Number of backpacks a player can have in his inventory\n" + "valid: integers 0-36\n" + "0 = unlimited\n" + "##############";
    }

    private static String getRenderBackpackModelComment() {
        return "##############\n" + "If true the backpack 3D model will be rendered.\n" + "##############";
    }

    private static String getOpenOnlyPersonalBackpacksComment() {
        return "##############\n" + "If true you can only open a backpack that you wear in the extra slot\n" + "##############";
    }

    private static String getAirshipModCompatibilityComment() {
        return "##############\n" + "If true normal backpack requires a chest in the middle\n" + "##############";
    }

    private static String getDisableBackpacksComment() {
        return "##############\n" + "If true small backpacks are not craftable\n" + "##############";
    }

    private static String getDisableMediumBackpacksComment() {
        return "##############\n" + "If true medium backpacks are not craftable\n" + "##############";
    }

    private static String getDisableBigBackpacksComment() {
        return "##############\n" + "If true big backpacks are not craftable\n" + "##############";
    }

    private static String getDisableEnderBackpacksComment() {
        return "##############\n" + "If true ender backpacks are not craftable\n" + "##############";
    }

    private static String getDisableWorkbenchBackpacksComment() {
        return "##############\n" + "If true workbench backpacks are not craftable\n" + "##############";
    }

    private static String getDirectHigherTierComment() {
        return "##############\n" + "If false medium and big backpacks can only be crafted by upgrading the previous size\n" + "##############";
    }

    private static String getDirectHigherMaterialComment() {
        return "##############\n" + "If false backpacks with a higher material can only crafted by upgrading the previous material\n" + "##############";
    }

    private static String getSkipUpgradeComment() {
        return "##############\n" + "If true you can skip a size or material to upgrade a backpack\n" + "##############";
    }

    private static String getDisallowItemsComment() {
        return "##############\n" + "Example:\n" + "disallowItems:1,5:2,ingotSilver\n\n" + "This will disallow stone, birch wood planks and any type of silver ingots in backpacks.\n" + "##############";
    }
}
