package io.bluestaggo.integratedcleanup.mixin.world;

import io.bluestaggo.integratedcleanup.CustomPayloadPackets;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.network.packet.GameEventPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	@Shadow @Final private MinecraftServer server;

	public ServerWorldMixin(WorldStorage storage, String name, Dimension dimension, WorldSettings settings, Profiler profiler) {
		super(storage, name, dimension, settings, profiler);
	}

	@Inject(
		method = "changeTime",
		at = @At("TAIL")
	)
	public void changeTimeInstantly(long time, CallbackInfo ci) {
		IntegratedCleanup.syncServerTime(this.server);
	}

	@Inject(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerWorld;wakeSleepingPlayers()V"
		)
	)
	public void changeTimeInstantlyOnSleep(CallbackInfo ci) {
		IntegratedCleanup.syncServerTime(this.server);
	}

	@Redirect(
		method = "tickWeather",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;tickWeather()V"
		)
	)
	public void tickThunder(World instance) {
		boolean wasThundering = this.isThundering();
		super.tickWeather();
		if (wasThundering != this.isThundering()) {
			if (wasThundering) {
				this.server.getPlayerManager().sendPacket(new GameEventPacket(2, 1));
			} else {
				this.server.getPlayerManager().sendPacket(new GameEventPacket(1, 1));
			}
		}
	}
}
