package net.sigmarizzler.steamy.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class GauntletMaterial implements ToolMaterial {
    public static final GauntletMaterial INSTANCE = new GauntletMaterial();

    @Override
    public int getDurability() {
        return 1000;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 6.0F;
    }

    @Override
    public float getAttackDamage() {
        return 3.0F;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 22;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
