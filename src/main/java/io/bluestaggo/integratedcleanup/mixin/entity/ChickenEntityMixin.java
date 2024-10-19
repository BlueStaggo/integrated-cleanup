package io.bluestaggo.integratedcleanup.mixin.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.living.mob.passive.animal.AnimalEntity;
import net.minecraft.entity.living.mob.passive.animal.ChickenEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity {
	public ChickenEntityMixin(World world) {
		super(world);
	}

	@Environment(EnvType.CLIENT)
	@Redirect(
		method = "tickAi",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/living/mob/passive/animal/ChickenEntity;onGround:Z"
		)
	)
	public boolean redirectOnGround(ChickenEntity instance) {
		Box collision = instance.shape.copy();
		collision.maxY = collision.minY;
		collision.minY -= 0.04;
		return !instance.world.getBlockCollisions(collision).isEmpty();
	}
}
