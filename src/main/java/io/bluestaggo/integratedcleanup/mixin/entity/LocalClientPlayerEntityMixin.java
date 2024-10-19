package io.bluestaggo.integratedcleanup.mixin.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Session;
import net.minecraft.client.entity.living.player.InputClientPlayerEntity;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalClientPlayerEntity.class)
public abstract class LocalClientPlayerEntityMixin extends InputClientPlayerEntity {
	public LocalClientPlayerEntityMixin(Minecraft minecraft, World world, Session session, int i) {
		super(minecraft, world, session, i);
	}

	@Inject(
		method = "damage",
		at = @At("HEAD")
	)
	public void damageWithFlashingHearts(DamageSource source, int amount, CallbackInfoReturnable<Boolean> cir) {
		this.prevHealth = this.getHealth() + amount;
	}
}
