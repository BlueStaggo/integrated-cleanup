package io.bluestaggo.integratedcleanup.mixin.server;

import net.minecraft.network.packet.GameEventPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(
		method = "sendWorldInfo",
		at = @At("TAIL")
	)
	public void sendExtraWorldInfo(ServerPlayerEntity player, ServerWorld world, CallbackInfo ci) {
		if (world.isThundering()) {
			player.networkHandler.sendPacket(new GameEventPacket(1, 1));
		}
	}
}
