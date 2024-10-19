package io.bluestaggo.integratedcleanup;

import io.bluestaggo.integratedcleanup.extensions.IntegratedCleanupWorldEventListener;
import io.bluestaggo.integratedcleanup.mixin.server.MinecraftServerAccessor;
import io.bluestaggo.integratedcleanup.mixin.world.WorldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.WorldTimePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public final class IntegratedCleanup {
	public static final String CUSTOM_PAYLOAD_PREFIX = "ItgClUp|";
	public static final String CUSTOM_PAYLOAD_KNOCKBACK_YAW = CUSTOM_PAYLOAD_PREFIX + "KBYaw";
	public static final String CUSTOM_PAYLOAD_VISUAL_ENTITY_EVENT = CUSTOM_PAYLOAD_PREFIX + "VEEvent";
	public static final String CUSTOM_PAYLOAD_EXT_GAME_EVENT = CUSTOM_PAYLOAD_PREFIX + "ExtGameEvt";

	public static final byte EXT_GAME_EVENT_ENABLE_THUNDER = 1;
	public static final byte EXT_GAME_EVENT_DISABLE_THUNDER = 2;

	public static final byte VISUAL_ENTITY_EVENT_GHAST_PUFF = 1;
	public static final byte VISUAL_ENTITY_EVENT_ANIMAL_BREED = 2;
	public static final byte VISUAL_ENTITY_EVENT_SPAWNER_POOF = 3;

	public static final int WORLD_EVENT_PREFIX = 0x16c00000;
	public static final int WORLD_EVENT_DRAGON_EGG_TELEPORT = WORLD_EVENT_PREFIX | 2000;
	public static final int WORLD_EVENT_REDSTONE_TORCH_BURNOUT = WORLD_EVENT_PREFIX | 2001;
	public static final int WORLD_EVENT_LIQUID_FIZZ = WORLD_EVENT_PREFIX | 2002;
	public static final int WORLD_EVENT_ENDER_EYE_INSERT = WORLD_EVENT_PREFIX | 2003;
	public static final int WORLD_EVENT_SPAWN_SNOW_GOLEM = WORLD_EVENT_PREFIX | 2004;
	public static final int WORLD_EVENT_SPAWN_IRON_GOLEM = WORLD_EVENT_PREFIX | 2005;

	private IntegratedCleanup() {
	}

	public static void syncServerTime(MinecraftServer server) {
		for (int i = 0; i < server.worlds.length; i++) {
			if (i != 0 && !server.isNetherAllowed()) {
				continue;
			}

			ServerWorld world = server.worlds[i];
			((MinecraftServerAccessor) server).getPlayerManager()
				.sendPacket(new WorldTimePacket(world.getTime()), world.dimension.id);
		}
	}

	public static void sendVisualEntityEvent(Entity entity, byte event) {
		for (Object eventListener : ((WorldAccessor) entity.world).getEventListeners()) {
			if (eventListener instanceof IntegratedCleanupWorldEventListener) {
				((IntegratedCleanupWorldEventListener) eventListener)
					.integratedCleanup$doVisualEntityEvent(entity, event);
			}
		}
	}
}
