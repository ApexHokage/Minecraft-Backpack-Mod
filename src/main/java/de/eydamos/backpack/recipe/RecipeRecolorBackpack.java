package de.eydamos.backpack.recipe;

import de.eydamos.backpack.item.EBackpack;
import de.eydamos.backpack.item.EColor;
import de.eydamos.backpack.item.ESize;
import de.eydamos.backpack.misc.BackpackItems;
import de.eydamos.backpack.misc.Constants;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class RecipeRecolorBackpack extends AbstractRecipe {
    private int dyeCol;
    private int dyeRow;
    private int backpackCol;
    private int backpackRow;
    private boolean dyeFound = false;
    private boolean backpackFound = false;

    public RecipeRecolorBackpack() {
        super(2, 1, EBackpack.SMALL.getItemStack(), Constants.RECIPE_RECOLOR, ECategory.SHAPELESS);
    }

    @Override
    protected boolean checkItemAtPosition(InventoryCrafting craftingGridInventory, int col, int row, int expectedCol, int expectedRow) {
        ItemStack itemStack = craftingGridInventory.getStackInRowAndColumn(col, row);

        if (itemStack == null) {
            return true;
        }

        if (EColor.isColor(itemStack)) {
            if (!dyeFound) {
                dyeCol = col;
                dyeRow = row;
                dyeFound = true;

                return true;
            }
            dyeFound = false;

            return false;
        }

        if (itemStack.getItem() == BackpackItems.backpack) {
            if (!backpackFound) {
                backpackCol = col;
                backpackRow = row;
                backpackFound = true;

                return true;
            }
            backpackFound = false;

            return false;
        }

        return false;
    }

    @Override
    protected boolean allRecipeItemsFulfilled() {
        boolean result = dyeFound && backpackFound;

        dyeFound = false;
        backpackFound = false;

        return result;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting craftingGridInventory) {
        ItemStack backpack = craftingGridInventory.getStackInRowAndColumn(backpackCol, backpackRow).copy();
        EColor color = EColor.getColor(craftingGridInventory.getStackInRowAndColumn(dyeCol, dyeRow));
        ESize size = ESize.getSizeByBackpack(backpack);

        backpack.setItemDamage(size.getDamage() + color.getDamage());

        return backpack;
    }
}
