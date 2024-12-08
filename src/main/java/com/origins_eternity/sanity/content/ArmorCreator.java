package com.origins_eternity.sanity.content;

import com.origins_eternity.sanity.Sanity;
import com.origins_eternity.sanity.config.Configuration;
import com.origins_eternity.sanity.content.armor.Armors;
import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.origins_eternity.sanity.content.tab.CreativeTab.SANITY;
import static com.origins_eternity.sanity.utils.Utils.isDangerous;
import static com.origins_eternity.sanity.utils.Utils.isWet;

public class ArmorCreator extends ItemArmor
{
    public ArmorCreator(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot, String name, int maxdamage)
    {
        super(material, renderIndex, equipmentSlot);
        setTranslationKey(Sanity.MOD_ID + "." + name.toLowerCase());
        setRegistryName(name.toLowerCase());
        setMaxDamage(maxdamage);
        setNoRepair();
        setCreativeTab(SANITY);

        Armors.ARMORS.add(this);
    }

    static int counter;

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (counter >= 10) {
            counter++;
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (player.inventory.armorItemInSlot(3).getItem().equals(this)) {
                sanity.recoverSanity(Configuration.garland);
                if (isWet(player) || isDangerous(player)) {
                    sanity.setWet(sanity.getWet() + 10);
                }
                if (sanity.getWet() > 60) {
                    stack.damageItem(1, player);
                    sanity.setWet(0);
                }
                super.onArmorTick(world, player, stack);
                counter = 0;
            }
        }
    }
}