package io.bluestaggo.integratedcleanup.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.integrated.IntegratedPlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(IntegratedPlayerManager.class)
public abstract class IntegratedPlayerManagerMixin extends PlayerManager {
	public IntegratedPlayerManagerMixin(MinecraftServer server) {
		super(server);
	}

	@ModifyConstant(
		method = "<init>",
		constant = @Constant(intValue = 10)
	)
	public int increaseViewDistance(int constant) {
		return 12;
	}
}
