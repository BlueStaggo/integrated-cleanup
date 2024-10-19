package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
	public MobEntityMixin(World world) {
		super(world);
	}

	@Inject(
		method = "doSpawnEffects",
		at = @At("HEAD"),
		cancellable = true
	)
	public void doFixedSpawnEffects(CallbackInfo ci) {
		IntegratedCleanup.sendVisualEntityEvent(this, IntegratedCleanup.VISUAL_ENTITY_EVENT_SPAWNER_POOF);
		ci.cancel();
	}
}
