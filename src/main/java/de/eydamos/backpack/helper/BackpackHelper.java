package de.eydamos.backpack.helper;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.misc.ConfigurationBackpack;
import de.eydamos.backpack.misc.Constants;
import de.eydamos.backpack.saves.BackpackSave;
import de.eydamos.backpack.util.NBTItemStackUtil;
import de.eydamos.backpack.util.NBTUtil;
import net.minecraft.item.ItemStack;

import java.util.*;

public class BackpackHelper {
    public static class Size {
        public int slots;
        public int columns = 9;
        public int scrollbar = 6;

        public Size(int slotAmount) {
            slots = slotAmount;
        }
    }

    protected List<String> materials;
    protected Map<Integer, HashMap<Integer, Size>> sizes;

    public static void init() {
        Backpack.backpackHelper.clear();

        // materials
        Backpack.backpackHelper.setMaterials(ConfigurationBackpack.MATERIALS.split(","));

        // backpacks
        Backpack.backpackHelper.addSizes(0, ConfigurationBackpack.BACKPACKS_S.split(","));
        Backpack.backpackHelper.addSizes(1, ConfigurationBackpack.BACKPACKS_M.split(","));
        Backpack.backpackHelper.addSizes(2, ConfigurationBackpack.BACKPACKS_L.split(","));

        Backpack.backpackHelper.checkSizes();

        // workbench backpacks
        Backpack.backpackHelper.addMaterial("workbench");
        Backpack.backpackHelper.addSize(0, Backpack.backpackHelper.getMaterialId("workbench"), new Size(9));
        Backpack.backpackHelper.addSize(2, Backpack.backpackHelper.getMaterialId("workbench"), new Size(18));

        // enderbackpack
        Backpack.backpackHelper.addMaterial("ender");
        Backpack.backpackHelper.addSize(0, Backpack.backpackHelper.getMaterialId("ender"), new Size(27));
    }

    protected void setMaterials(String[] materialArray) {
        materials.add("%leather");
        for(String material : materialArray) {
            materials.add(material.trim());
        }
    }

    protected void addMaterial(String material) {
        if(!material.trim().isEmpty()) {
            materials.add(material.trim());
        }
    }

    protected void addSizes(int tier, String[] sizesArr) {
        for(int i = 0; i < sizesArr.length; i++) {
            if(i >= materials.size()) {
                break;
            }

            String[] sizeArr = sizesArr[i].split(":");
            Size size = new Size(Integer.valueOf(sizeArr[0].trim()));

            if(sizeArr.length >= 2 && !sizeArr[1].trim().isEmpty()) {
                size.columns = Integer.valueOf(sizeArr[1].trim());
            }
            if(sizeArr.length == 3 && !sizeArr[2].trim().isEmpty()) {
                size.scrollbar = Integer.valueOf(sizeArr[2].trim());
            }

            HashMap<Integer, Size> tierData = getTierData(tier);
            tierData.put(i, size);
        }
    }

    protected void addSize(int tier, int material, Size size) {
        getTierData(tier).put(material, size);
    }

    protected void checkSizes() {
        for(int tier = 0; tier < sizes.size(); tier++) {
            HashMap<Integer, Size> tierData = getTierData(tier);

            for(int material = 0; material < tierData.size(); material++) {
                // next material must have more slots as current material
                if(tierData.containsKey(material + 1) && tierData.get(material + 1).slots <= tierData.get(material).slots) {
                    tierData.get(material + 1).slots = tierData.get(material).slots + 1;
                }

                if(tier != sizes.size() - 1) {
                    // same material in next tier must have at least the same a mount of slots
                    HashMap<Integer, Size> nextTier = getTierData(tier + 1);
                    if(nextTier.containsKey(material) && nextTier.get(material).slots < tierData.get(material).slots) {
                        nextTier.get(material).slots = tierData.get(material).slots;
                    }
                }
            }
        }
    }

    protected HashMap<Integer, Size> getTierData(int tier) {
        if(null == sizes) {
            init();
        }
        if(!sizes.containsKey(tier)) {
            sizes.put(tier, new HashMap<Integer, Size>());
        }
        return sizes.get(tier);
    }

    protected int getMaterialId(String material) {
        if(null == materials) {
            init();
        }
        return materials.indexOf(material);
    }

    public int getTier(ItemStack itemStack) {
        return itemStack.getItemDamage() / 100 < 3 ? itemStack.getItemDamage() / 100 : 0;
    }

    public int getMeta(ItemStack itemStack) {
        return itemStack.getItemDamage() % 100;
    }

    public Size getSize(ItemStack itemStack) {
        int tier = getTier(itemStack);
        int material;
        BackpackSave backpackSave = new BackpackSave(itemStack);
        if(!backpackSave.getMaterial().isEmpty()) {
            material = getMaterialId(backpackSave.getMaterial());
        } else {
            int meta = getMeta(itemStack);
            if(meta == 99) { // ender
                material = getMaterialId("ender");
            } else if(meta == 17) { // workbench
                material = getMaterialId("workbench");
            } else {
                material = getMaterialId("%leather");
            }
        }
        return getSize(tier, material);
    }

    public Size getSize(int tier, int material) {
        Map<Integer, Size> tierData = getTierData(tier);
        if(tierData.containsKey(material)) {
            return getTierData(tier).get(material);
        } else {
            return new Size(18);
        }
    }

    protected void clear() {
        materials = new ArrayList<String>();
        sizes = new HashMap<Integer, HashMap<Integer, Size>>();
    }
}
