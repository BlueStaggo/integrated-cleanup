package io.bluestaggo.integratedcleanup.mixin.block;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RedstoneTorchBlock.class)
public abstract class RedstoneTorchBlockMixin extends TorchBlock {
	@Shadow
	protected abstract boolean shouldBurnOut(World world, int x, int y, int z, boolean logToggle);

	protected RedstoneTorchBlockMixin() {
		super();
	}

	@Redirect(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/RedstoneTorchBlock;shouldBurnOut(Lnet/minecraft/world/World;IIIZ)Z",
			ordinal = 0
		)
	)
	public boolean tickBurnOutParticles(RedstoneTorchBlock instance, World world, int x, int y, int z, boolean logToggle) {
		if (this.shouldBurnOut(world, x, y, z, logToggle)) {
			world.playSound(x + 0.5, y + 0.5, z + 0.5, "random.fizz", 0.5F,
				2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
			world.doEvent(IntegratedCleanup.WORLD_EVENT_REDSTONE_TORCH_BURNOUT, x, y, z, 0);
		}
		return false;
	}
}
