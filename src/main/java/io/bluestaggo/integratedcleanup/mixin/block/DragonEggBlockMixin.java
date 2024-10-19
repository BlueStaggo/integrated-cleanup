package io.bluestaggo.integratedcleanup.mixin.block;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.block.Block;
import net.minecraft.block.DragonEggBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DragonEggBlock.class)
public abstract class DragonEggBlockMixin extends Block {
	protected DragonEggBlockMixin(Material material) {
		super(material);
	}

	@Redirect(
		method = "tryTeleport",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/World;addParticle(Ljava/lang/String;DDDDDD)V"
		)
	)
	public void tryTeleportWithFixedParticles(World world, String particle, double x, double y, double z,
											  double velocityX, double velocityY, double velocityZ) {
		int data = Math.floorMod(MathHelper.floor(x * 256.0), 256)
			| (Math.floorMod(MathHelper.floor(y * 256.0), 256) << 8)
			| (Math.floorMod(MathHelper.floor(z * 256.0), 256) << 16);
		world.doEvent(IntegratedCleanup.WORLD_EVENT_DRAGON_EGG_TELEPORT,
			MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), data);
	}
}
