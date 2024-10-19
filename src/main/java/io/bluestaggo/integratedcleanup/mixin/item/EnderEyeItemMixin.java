package io.bluestaggo.integratedcleanup.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin extends Item {
	protected EnderEyeItemMixin(int id) {
		super(id);
	}

	@ModifyConstant(
		method = "use",
		constant = @Constant(intValue = 16)
	)
	public int fixedSmokeParticles(int constant, @Local(argsOnly = true) World world,
								   @Local(argsOnly = true, ordinal = 0) int x,
								   @Local(argsOnly = true, ordinal = 1) int y,
								   @Local(argsOnly = true, ordinal = 2) int z) {
		world.doEvent(IntegratedCleanup.WORLD_EVENT_ENDER_EYE_INSERT, x, y, z, 0);
		return 0;
	}
}
