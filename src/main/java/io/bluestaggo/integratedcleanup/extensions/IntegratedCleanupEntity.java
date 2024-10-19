package io.bluestaggo.integratedcleanup.extensions;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IntegratedCleanupEntity {
	@Environment(EnvType.CLIENT)
	default void integratedCleanup$doVisualEntityEvent(byte event) { }
}
