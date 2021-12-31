package io.sandbox.leather.mixin;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
  @Shadow public abstract Iterable<ItemStack> getArmorItems();

  @Inject(method = "getArmorVisibility", at = @At("RETURN"), cancellable = true)
  private void getArmorVisibility(CallbackInfoReturnable<Float> info) {
    Iterable<ItemStack> iterable = this.getArmorItems();
    int i = 0;
    int j = 0;

    for(Iterator<ItemStack> var4 = iterable.iterator(); var4.hasNext(); ++i) {
      ItemStack itemStack = (ItemStack)var4.next();
      if (!itemStack.isEmpty()) {
        ++j; // add because an item is equipped
        Item armor = itemStack.getItem();
        if (
          armor instanceof ArmorItem &&
          ((ArmorItem)armor).getMaterial().equals(ArmorMaterials.LEATHER)
        ) {
          --j; // remove the count because it's leather
        }
      }
    }

    info.setReturnValue(i > 0 ? (float)j / (float)i : 0.0F);
  }
}
