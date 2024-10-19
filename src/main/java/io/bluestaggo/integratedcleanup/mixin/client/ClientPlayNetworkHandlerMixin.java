package io.bluestaggo.integratedcleanup.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.integratedcleanup.CustomPayloadPackets;
import io.bluestaggo.integratedcleanup.EnteringDimensionScreen;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import io.bluestaggo.integratedcleanup.mixin.world.WorldAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.resource.Identifier;
import net.minecraft.client.sound.instance.SimpleSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameEventS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.world.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Unique private int previousDimension;
	@Shadow private Minecraft minecraft;
	@Shadow private ClientWorld world;

	@Inject(
		method = "handleCustomPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleIntegratedCleanupPayloads(CustomPayloadS2CPacket packet, CallbackInfo ci) {
		if (IntegratedCleanup.CUSTOM_PAYLOAD_KNOCKBACK_YAW.equals(packet.getChannel())) {
			this.minecraft.player.knockbackVelocity = CustomPayloadPackets.readKnockbackYaw(packet.getData());
			ci.cancel();
		} else if (IntegratedCleanup.CUSTOM_PAYLOAD_VISUAL_ENTITY_EVENT.equals(packet.getChannel())) {
			CustomPayloadPackets.VisualEntityEvent visualEntityEvent
				= CustomPayloadPackets.readVisualEntityEvent(packet.getData());

			Entity entity = this.world.getEntity(visualEntityEvent.entityId);
			if (entity != null) {
				entity.integratedCleanup$doVisualEntityEvent(visualEntityEvent.eventType);
			}
			ci.cancel();
		}
	}

	@Redirect(
		method = "handlePlayerMove",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"
		)
	)
	public void handlePlayerMoveLaterStart(Minecraft minecraft, Screen screen) {
	}

	@Inject(
		method = "handlePlayerRespawn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/Minecraft;setWorld(Lnet/minecraft/client/world/ClientWorld;)V"
		)
	)
	public void handleDimensionChangeSound(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
		if (this.minecraft.player.deathTime > 0) {
			return;
		}
		this.previousDimension = this.minecraft.world.dimension.id;
	}

	@Redirect(
		method = "handlePlayerRespawn",
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/client/network/handler/ClientPlayNetworkHandler;)Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen;"
		)
	)
	public DownloadingTerrainScreen handleCustomDimensionLoadingScreen(ClientPlayNetworkHandler clientPlayNetworkHandler,
																	   @Local(argsOnly = true) PlayerRespawnS2CPacket packet) {
		String dimensionName = "Overworld";
		boolean leaving = this.previousDimension != 0;
		switch (leaving ? this.previousDimension : packet.getDimensionId()) {
			case 0:
				break;

			case -1:
				dimensionName = "Nether";
				break;

			case 1:
				dimensionName = "End";
				break;

			default:
				leaving = !leaving;
				break;
		}

		String message = (leaving ? "Leaving the " : "Entering the ") + dimensionName;
		return new EnteringDimensionScreen((ClientPlayNetworkHandler) (Object) this, message, this.minecraft.player.deathTime == 0);
	}

	@Redirect(
		method = "handleGameEvent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/WorldData;setRaining(Z)V"
		)
	)
	public void handleThunderEvent(WorldData instance, boolean raining,
								   @Local(argsOnly = true) GameEventS2CPacket packet) {
		if (packet.getData() == 1) {
			instance.setThundering(raining);
		} else {
			instance.setRaining(raining);
		}
	}

	@Redirect(
		method = "handleGameEvent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/world/ClientWorld;setRain(F)V"
		)
	)
	public void handleThunderEvent(ClientWorld instance, float rain,
								   @Local(argsOnly = true) GameEventS2CPacket packet) {
		if (packet.getData() == 1) {
			((WorldAccessor) this.world).setPrevThunder(rain);
			((WorldAccessor) this.world).setThunder(rain);
		} else {
			instance.setRain(rain);
		}
	}
}
