package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.entity.living.mob.passive.PassiveEntity;
import net.minecraft.entity.living.mob.passive.animal.AnimalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
	public AnimalEntityMixin(World world) {
		super(world);
	}

	@ModifyConstant(
		method = "breed",
		constant = @Constant(intValue = 7)
	)
	public int removeServerOnlyBreedingHearts(int constant) {
		IntegratedCleanup.sendVisualEntityEvent(this, IntegratedCleanup.VISUAL_ENTITY_EVENT_ANIMAL_BREED);
		return 0;
	}

	@Override
	public void integratedCleanup$doVisualEntityEvent(byte event) {
		if (event == IntegratedCleanup.VISUAL_ENTITY_EVENT_ANIMAL_BREED) {
			for (int i = 0; i < 7; ++i) {
				double vx = this.random.nextGaussian() * 0.02;
				double vy = this.random.nextGaussian() * 0.02;
				double vz = this.random.nextGaussian() * 0.02;
				this.world.addParticle("heart",
					this.x + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width,
					this.y + 0.5 + (double)(this.random.nextFloat() * this.height),
					this.z + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width,
					vx, vy, vz);
			}
		} else {
			super.integratedCleanup$doVisualEntityEvent(event);
		}
	}
}
