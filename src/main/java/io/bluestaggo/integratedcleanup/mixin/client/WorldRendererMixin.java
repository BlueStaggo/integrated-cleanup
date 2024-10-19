package io.bluestaggo.integratedcleanup.mixin.client;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import io.bluestaggo.integratedcleanup.extensions.IntegratedCleanupWorldEventListener;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements IntegratedCleanupWorldEventListener {
	@Shadow private ClientWorld world;

	@Inject(
		method = "doEvent",
		at = @At("TAIL")
	)
	public void doIntegrateadCleanupEvent(PlayerEntity source, int type, int x, int y, int z, int data, CallbackInfo ci) {
		double dx, dy, dz;

		switch (type) {
			case IntegratedCleanup.WORLD_EVENT_DRAGON_EGG_TELEPORT:
				dx = x + (data & 255) / 256.0;
				dy = y + ((data >> 8) & 255) / 256.0;
				dz = z + ((data >> 16) & 255) / 256.0;
				this.world.addParticle("portal", dx, dy, dz,
					(this.world.random.nextFloat()) - 0.5F * 0.2F,
					(this.world.random.nextFloat()) - 0.5F * 0.2F,
					(this.world.random.nextFloat()) - 0.5F * 0.2F);
				break;

			case IntegratedCleanup.WORLD_EVENT_REDSTONE_TORCH_BURNOUT:
				for (int i = 0; i < 5; ++i) {
					dx = x + this.world.random.nextDouble() * 0.6 + 0.2;
					dy = y + this.world.random.nextDouble() * 0.6 + 0.2;
					dz = z + this.world.random.nextDouble() * 0.6 + 0.2;
					this.world.addParticle("smoke", dx, dy, dz, 0.0, 0.0, 0.0);
				}
				break;

			case IntegratedCleanup.WORLD_EVENT_LIQUID_FIZZ:
				for (int i = 0; i < 8; ++i) {
					this.world.addParticle("largesmoke",
						(double)x + Math.random(), (double)y + 1.2, (double)z + Math.random(),
						0.0, 0.0, 0.0);
				}
				break;

			case IntegratedCleanup.WORLD_EVENT_ENDER_EYE_INSERT:
				for (int i = 0; i < 16; ++i) {
					dx = x + (5.0 + Math.random() * 6.0) / 16.0;
					dy = y + 0.8125;
					dz = z + (5.0 + Math.random() * 6.0) / 16.0;
					world.addParticle("smoke", dx, dy, dz, 0.0, 0.0, 0.0);
				}
				break;

			case IntegratedCleanup.WORLD_EVENT_SPAWN_SNOW_GOLEM:
				for(int i = 0; i < 120; ++i) {
					world.addParticle("snowshovel",
						x + world.random.nextDouble(),
						(y - 2) + world.random.nextDouble() * 2.5,
						z + world.random.nextDouble(),
						0.0, 0.0, 0.0);
				}
				break;

			case IntegratedCleanup.WORLD_EVENT_SPAWN_IRON_GOLEM:
				for(int i = 0; i < 120; ++i) {
					world.addParticle("snowballpoof",
						x + world.random.nextDouble(),
						(y - 2) + world.random.nextDouble() * 3.9,
						z + world.random.nextDouble(),
						0.0, 0.0, 0.0);
				}
				break;
		}
	}

	@Override
	public void integratedCleanup$doVisualEntityEvent(Entity entity, byte event) {
		entity.integratedCleanup$doVisualEntityEvent(event);
	}
}
