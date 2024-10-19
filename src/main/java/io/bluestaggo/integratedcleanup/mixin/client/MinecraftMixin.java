package io.bluestaggo.integratedcleanup.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LoadingScreenRenderer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow private IntegratedServer server;

	@Redirect(
		method = "startGame",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/LoadingScreenRenderer;setTask(Ljava/lang/String;)V"
		)
	)
	public void startGameSetLoadingTask(LoadingScreenRenderer loadingScreenRenderer, String task) {
		loadingScreenRenderer.setTask(task);
		if (this.server.getLoadingStage() != null) {
			loadingScreenRenderer.progressStagePercentage(this.server.progress);
		}
	}
}
