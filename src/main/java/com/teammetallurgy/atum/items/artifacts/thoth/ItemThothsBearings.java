package com.teammetallurgy.atum.items.artifacts.thoth;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemThothsBearings extends ItemMap {

    public ItemThothsBearings() {
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, @Nullable Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            EnumHand hand = player.getHeldItem(EnumHand.OFF_HAND).getItem() == this ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            ItemStack heldStack = player.getHeldItem(hand);
            if (heldStack.getItem() == this && heldStack.getMetadata() == 0) {
                ItemStack map = ItemThothsBearings.setupNewMap(world, entity.posX, entity.posZ, (byte) 4, true, true);
                heldStack.shrink(1);

                if (!player.inventory.addItemStackToInventory(map.copy())) {
                    player.dropItem(map, false);
                }
            }
        }
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    public static ItemStack setupNewMap(World world, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
        ItemStack map = new ItemStack(AtumItems.THOTHS_BEARINGS, 1, world.getUniqueDataId("map"));
        String s = "map_" + map.getMetadata();
        MapData mapdata = new MapData(s);
        world.setData(s, mapdata);
        mapdata.scale = scale;
        mapdata.calculateMapCenter(worldX, worldZ, mapdata.scale);
        mapdata.dimension = world.provider.getDimension();
        mapdata.trackingPosition = trackingPosition;
        mapdata.unlimitedTracking = unlimitedTracking;
        mapdata.markDirty();
        return map;
    }
}