package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.entity.ai.goal.AnimalBreedGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.living.mob.passive.animal.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnimalBreedGoal.class)
public abstract class AnimalBreedGoalMixin extends Goal {
	@Shadow private AnimalEntity animal;

	@ModifyConstant(
		method = "breed",
		constant = @Constant(intValue = 7, ordinal = 0)
	)
	public int fixedBreedingHearts(int constant) {
		IntegratedCleanup.sendVisualEntityEvent(this.animal, IntegratedCleanup.VISUAL_ENTITY_EVENT_ANIMAL_BREED);
		return 0;
	}
}
