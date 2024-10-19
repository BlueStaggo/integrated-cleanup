package io.bluestaggo.integratedcleanup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.resource.Identifier;
import net.minecraft.client.sound.instance.SimpleSoundInstance;
import net.minecraft.locale.I18n;

@Environment(EnvType.CLIENT)
public class EnteringDimensionScreen extends DownloadingTerrainScreen {
	private final String message;
	private final boolean dimensionTransition;

	public EnteringDimensionScreen(ClientPlayNetworkHandler networkHandler, String message) {
		this(networkHandler, message, false);
	}

	public EnteringDimensionScreen(ClientPlayNetworkHandler networkHandler, String message,
								   boolean dimensionTransition) {
		super(networkHandler);
		this.message = message;
		this.dimensionTransition = dimensionTransition;
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		this.drawBackgroundTexture(0);
		this.drawCenteredString(this.textRenderer, this.message, this.width / 2, this.height / 2 - 20, 16777215);
		this.drawCenteredString(this.textRenderer, I18n.translate("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 + 4, 16777215);
	}

	@Override
	public void removed() {
		if (this.dimensionTransition) {
			this.minecraft.getSoundManager().play(SimpleSoundInstance.of(new Identifier("portal.travel"),
				this.minecraft.player.getRandom().nextFloat() * 0.4F + 0.8F));
		}
	}
}
