package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.extensions.IntegratedCleanupEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements IntegratedCleanupEntity {
	@Shadow public World world;

	@SuppressWarnings("ConstantValue")
	@Inject(
		method = "move",
		at = @At("HEAD"),
		cancellable = true
	)
	@Environment(EnvType.CLIENT)
	private void moveWithMultiplayerRestriction(CallbackInfo ci) {
		if (this.world.isMultiplayer && (Object) this instanceof LivingEntity
			&& !((Object) this instanceof LocalClientPlayerEntity))
			ci.cancel();
	}
}
