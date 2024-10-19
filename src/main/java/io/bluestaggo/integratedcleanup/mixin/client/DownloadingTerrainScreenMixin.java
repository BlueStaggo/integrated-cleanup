package io.bluestaggo.integratedcleanup.mixin.client;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.EmptyChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public abstract class DownloadingTerrainScreenMixin extends Screen {
	@Inject(
		method = "tick",
		at = @At("TAIL")
	)
	public void checkLoaded(CallbackInfo ci) {
		if (this.minecraft.player != null && !(this.minecraft.world.getChunk(
			MathHelper.floor(this.minecraft.player.x), MathHelper.floor(this.minecraft.player.z)) instanceof EmptyChunk)) {
			this.minecraft.openScreen(null);
		}
	}
}
