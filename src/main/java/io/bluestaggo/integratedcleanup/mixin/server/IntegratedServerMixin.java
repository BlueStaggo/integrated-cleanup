package io.bluestaggo.integratedcleanup.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.net.Proxy;
import java.util.Objects;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
	@Unique private int logTicks = 0;

	public IntegratedServerMixin(File gameDir, Proxy proxy) {
		super(gameDir, proxy);
	}

	@Override
	protected void logProgress(String progressType, int progress) {
		if (!Objects.equals(this.progressType, progressType)) {
			this.logTicks = 10;
		}

		this.logTicks++;
		if (this.logTicks >= 10) {
			this.logTicks = 0;
			super.logProgress(progressType, progress);
		} else {
			this.progressType = progressType;
			this.progress = progress;
		}
	}
}
