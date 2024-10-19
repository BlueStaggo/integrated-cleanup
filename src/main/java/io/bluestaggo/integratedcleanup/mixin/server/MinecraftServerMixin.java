package io.bluestaggo.integratedcleanup.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Environment(EnvType.CLIENT)
	@ModifyConstant(
		method = "prepareWorlds",
		constant = @Constant(longValue = 1000L)
	)
	public long prepareWorldsMoreProgress(long constant) {
		if ((Object) this instanceof IntegratedServer) {
			constant /= 10L;
		}
		return constant;
	}
}
