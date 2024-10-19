package io.bluestaggo.integratedcleanup.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.integratedcleanup.CustomPayloadPackets;
import io.bluestaggo.integratedcleanup.EnteringDimensionScreen;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import io.bluestaggo.integratedcleanup.mixin.world.WorldAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketHandler;
import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.network.packet.GameEventPacket;
import net.minecraft.network.packet.PlayerRespawnPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldData;
import net.minecraft.world.chunk.EmptyChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin extends PacketHandler {
	@Unique private int previousDimension;
	@Shadow private Minecraft minecraft;
	@Shadow private ClientWorld world;

	@Inject(
		method = "handleCustomPayload",
		at = @At("HEAD"),
		cancellable = true
	)
	public void handleIntegratedCleanupPayloads(CustomPayloadPacket packet, CallbackInfo ci) {
		if (IntegratedCleanup.CUSTOM_PAYLOAD_KNOCKBACK_YAW.equals(packet.channel)) {
			this.minecraft.player.knockbackVelocity = CustomPayloadPackets.readKnockbackYaw(packet.data);
			ci.cancel();
		} else if (IntegratedCleanup.CUSTOM_PAYLOAD_VISUAL_ENTITY_EVENT.equals(packet.channel)) {
			CustomPayloadPackets.VisualEntityEvent visualEntityEvent
				= CustomPayloadPackets.readVisualEntityEvent(packet.data);

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
	public void handleDimensionChangeSound(PlayerRespawnPacket packet, CallbackInfo ci) {
		if (this.minecraft.player.deathTime > 0) {
			return;
		}
		this.minecraft.soundSystem.play("portal.travel", 1.0F, this.minecraft.player.getRandom().nextFloat() * 0.4F + 0.8F);
		this.previousDimension = this.minecraft.world.dimension.id;
	}

	@Redirect(
		method = "handlePlayerRespawn",
		at = @At(
			value = "NEW",
			target = "(Lnet/minecraft/client/network/handler/ClientNetworkHandler;)Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen;"
		)
	)
	public DownloadingTerrainScreen handleCustomDimensionLoadingScreen(ClientNetworkHandler clientNetworkHandler,
																	   @Local(argsOnly = true) PlayerRespawnPacket packet) {
		String dimensionName = "Overworld";
		boolean leaving = this.previousDimension != 0;
		switch (leaving ? this.previousDimension : packet.dimensionId) {
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
		return new EnteringDimensionScreen((ClientNetworkHandler) (Object) this, message);
	}

	@Redirect(
		method = "handleGameEvent",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/WorldData;setRaining(Z)V"
		)
	)
	public void handleThunderEvent(WorldData instance, boolean raining,
								   @Local(argsOnly = true) GameEventPacket packet) {
		if (packet.data == 1) {
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
	public void handleThunderEvent(ClientWorld instance, float rain, @Local(argsOnly = true) GameEventPacket packet) {
		if (packet.data == 1) {
			((WorldAccessor) this.world).setPrevThunder(rain);
			((WorldAccessor) this.world).setThunder(rain);
		} else {
			instance.setRain(rain);
		}
	}
}
