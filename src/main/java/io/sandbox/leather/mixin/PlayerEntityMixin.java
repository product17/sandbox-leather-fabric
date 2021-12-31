package io.sandbox.leather.mixin;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
  protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Inject(method = "tick", at = @At("HEAD"))
  private void tick(CallbackInfo info) {
    if (!this.world.isClient) {
      if (this.isSneaky()) {
        Iterable<ItemStack> iterable = this.getArmorItems();
        int leatherPieces = 0;
        Iterator<ItemStack> iterator = iterable.iterator();
        
        while (iterator.hasNext()) {
          ItemStack itemStack = iterator.next();
          Item item = itemStack.getItem();
          if (item instanceof ArmorItem && ((ArmorItem)item).getMaterial().equals(ArmorMaterials.LEATHER)) {
            leatherPieces++;
          }
        };

        if (leatherPieces >= 4) {
          this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 6, 1));
        }
      }
    }
  }
}
