package io.bluestaggo.integratedcleanup.mixin.world;

import io.bluestaggo.integratedcleanup.CustomPayloadPackets;
import io.bluestaggo.integratedcleanup.extensions.IntegratedCleanupWorldEventListener;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerWorldEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorldEventListener.class)
public abstract class ServerWorldEventListenerMixin implements IntegratedCleanupWorldEventListener {
	@Shadow private MinecraftServer server;
	@Shadow private ServerWorld world;

	@Override
	public void integratedCleanup$doVisualEntityEvent(Entity entity, byte event) {
		CustomPayloadPackets.VisualEntityEvent visualEntityEvent = new CustomPayloadPackets.VisualEntityEvent();
		visualEntityEvent.entityId = entity.getNetworkId();
		visualEntityEvent.eventType = event;

		this.server.getPlayerManager().sendPacket(null, entity.x, entity.y, entity.z, 64.0, this.world.dimension.id,
			CustomPayloadPackets.createVisualEntityEvent(visualEntityEvent));
	}
}
