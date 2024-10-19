package io.bluestaggo.integratedcleanup.extensions;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldEventListener;

public interface IntegratedCleanupWorldEventListener extends WorldEventListener {
	default void integratedCleanup$doVisualEntityEvent(Entity entity, byte event) { }
}
