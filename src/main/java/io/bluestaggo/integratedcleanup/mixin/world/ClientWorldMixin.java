package io.bluestaggo.integratedcleanup.mixin.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ILogger;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	public ClientWorldMixin(WorldStorage storage, String name, Dimension dimension, WorldSettings settings, Profiler profiler, ILogger logger) {
		super(storage, name, dimension, settings, profiler, logger);
	}

	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	public void tickAmbientDarkness(CallbackInfo ci) {
		int ambientDarkness = this.calculateAmbientDarkness(1.0F);
		if (ambientDarkness != this.ambientDarkness) {
			this.ambientDarkness = ambientDarkness;
		}
	}
}
