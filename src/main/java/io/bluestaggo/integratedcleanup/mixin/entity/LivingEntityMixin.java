package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public float headYaw;
	@Unique private float targetHeadYaw;

	public LivingEntityMixin(World world) {
		super(world);
	}

	@Inject(
		method = "setHeadYaw",
		at = @At("HEAD"),
		cancellable = true
	)
	public void setTargetHeadYaw(float headYaw, CallbackInfo ci) {
		this.targetHeadYaw = headYaw;
		ci.cancel();
	}

	@Inject(
		method = "baseTick",
		at = @At("TAIL")
	)
	public void tickHeadYaw(CallbackInfo ci) {
		if (!this.world.isMultiplayer) {
			return;
		}

		float diffHeadYaw = this.targetHeadYaw - this.headYaw;
		if (diffHeadYaw > 180.0f) {
			this.headYaw += 360.0f;
		} else if (diffHeadYaw < -180.0f) {
			this.headYaw -= 360.0f;
		}

		this.headYaw += (this.targetHeadYaw - this.headYaw) * 0.5f;
	}

	@Override
	public void integratedCleanup$doVisualEntityEvent(byte event) {
		if (event == IntegratedCleanup.VISUAL_ENTITY_EVENT_SPAWNER_POOF) {
			for (int i = 0; i < 20; ++i) {
				double offX = this.random.nextGaussian() * 0.02;
				double offY = this.random.nextGaussian() * 0.02;
				double offZ = this.random.nextGaussian() * 0.02;
				double range = 10.0;
				this.world.addParticle("explode",
					this.x + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width - offX * range,
					this.y + (double)(this.random.nextFloat() * this.height) - offY * range,
					this.z + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width - offZ * range,
					offX, offY, offZ);
			}
		} else {
			super.integratedCleanup$doVisualEntityEvent(event);
		}
	}
}
