package io.bluestaggo.integratedcleanup.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin extends HorizontalFacingBlock {
	protected PumpkinBlockMixin(int i, int j, Material material) {
		super(i, j, material);
	}

	@ModifyConstant(
		method = "onAdded",
		constant = @Constant(intValue = 120, ordinal = 0)
	)
	public int fixedSnowGolemParticles(int constant, @Local(argsOnly = true) World world,
								       @Local(argsOnly = true, ordinal = 0) int x,
								       @Local(argsOnly = true, ordinal = 1) int y,
								       @Local(argsOnly = true, ordinal = 2) int z) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_SPAWN_SNOW_GOLEM, x, y, z, 0);
		return 0;
	}

	@ModifyConstant(
		method = "onAdded",
		constant = @Constant(intValue = 120, ordinal = 1)
	)
	public int fixedIronGolemParticles(int constant, @Local(argsOnly = true) World world,
									   @Local(argsOnly = true, ordinal = 0) int x,
									   @Local(argsOnly = true, ordinal = 1) int y,
									   @Local(argsOnly = true, ordinal = 2) int z) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_SPAWN_IRON_GOLEM, x, y, z, 0);
		return 0;
	}
}
