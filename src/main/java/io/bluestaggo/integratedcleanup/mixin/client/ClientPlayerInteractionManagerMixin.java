package io.bluestaggo.integratedcleanup.mixin.client;

import net.minecraft.client.ClientPlayerInteractionManager;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Shadow private WorldSettings.GameMode gameMode;

	@ModifyConstant(
		method = "hasXpBar",
		constant = @Constant(intValue = 1)
	)
	public int hideCreativeXpBar(int constant) {
		return this.gameMode.isCreative() ? 0 : 1;
	}
}
