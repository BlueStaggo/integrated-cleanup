package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.living.mob.FlyingEntity;
import net.minecraft.entity.living.mob.GhastEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity {
	@Shadow public int attackCooldown;
	@Shadow public int shootingCooldown;
	@Unique private boolean wasFiring;

	public GhastEntityMixin(World world) {
		super(world);
	}

	@Override
	public void integratedCleanup$doVisualEntityEvent(byte event) {
		if (event == IntegratedCleanup.VISUAL_ENTITY_EVENT_GHAST_PUFF) {
			this.shootingCooldown = 0;
		} else {
			super.integratedCleanup$doVisualEntityEvent(event);
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void tickAi() {
		super.tickAi();

		if (!this.world.isMultiplayer) {
			return;
		}

		this.attackCooldown = this.shootingCooldown;
		if (this.shootingCooldown >= 0) {
			this.shootingCooldown++;
			if (this.shootingCooldown > 20) {
				this.shootingCooldown = -40;
			}
		}

		boolean firing = this.dataTracker.getByte(16) != 0;
		if (firing && !this.wasFiring && this.shootingCooldown < 0) {
			this.shootingCooldown = 10;
		}
		this.wasFiring = firing;
	}

	@Inject(
		method = "tickDespawn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/mob/GhastEntity;canSee(Lnet/minecraft/entity/Entity;)Z",
			shift = At.Shift.AFTER
		)
	)
	private void tickDespawnDoShootEvent(CallbackInfo ci) {
		if (this.shootingCooldown == 0) {
			IntegratedCleanup.sendVisualEntityEvent(this, IntegratedCleanup.VISUAL_ENTITY_EVENT_GHAST_PUFF);
		}
	}
}
