package io.bluestaggo.integratedcleanup.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SkullBlock.class)
public abstract class SkullBlockMixin extends BlockWithBlockEntity {
	protected SkullBlockMixin(int i, Material material) {
		super(material);
	}

	@ModifyConstant(
		method = "trySpawn",
		constant = @Constant(intValue = 120, ordinal = 0)
	)
	public int fixedWitherParticlesZ(int constant, @Local(argsOnly = true) World world,
									 @Local(argsOnly = true, ordinal = 0) int x,
									 @Local(argsOnly = true, ordinal = 1) int y,
									 @Local(argsOnly = true, ordinal = 2) int z,
									 @Local(ordinal = 4) int offset) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_SPAWN_IRON_GOLEM, x, y, z + offset + 1, 0);
		return 0;
	}

	@ModifyConstant(
		method = "trySpawn",
		constant = @Constant(intValue = 120, ordinal = 1)
	)
	public int fixedWitherParticlesX(int constant, @Local(argsOnly = true) World world,
									 @Local(argsOnly = true, ordinal = 0) int x,
									 @Local(argsOnly = true, ordinal = 1) int y,
									 @Local(argsOnly = true, ordinal = 2) int z,
									 @Local(ordinal = 4) int offset) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_SPAWN_IRON_GOLEM, x + offset - 1, y, z, 0);
		return 0;
	}
}
