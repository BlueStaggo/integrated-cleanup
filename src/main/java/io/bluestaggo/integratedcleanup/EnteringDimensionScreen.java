package io.bluestaggo.integratedcleanup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.handler.ClientNetworkHandler;
import net.minecraft.locale.I18n;

@Environment(EnvType.CLIENT)
public class EnteringDimensionScreen extends DownloadingTerrainScreen {
	private final String message;

	public EnteringDimensionScreen(ClientNetworkHandler networkHandler, String message) {
		super(networkHandler);
		this.message = message;
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		this.drawBackgroundTexture(0);
		this.drawCenteredString(this.textRenderer, this.message, this.width / 2, this.height / 2 - 20, 16777215);
		this.drawCenteredString(this.textRenderer, I18n.translate("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 + 4, 16777215);
	}
}
