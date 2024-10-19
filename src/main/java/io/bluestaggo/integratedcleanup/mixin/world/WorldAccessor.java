package io.bluestaggo.integratedcleanup.mixin.world;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(World.class)
public interface WorldAccessor {
	@Accessor List getEventListeners();
	@Accessor void setThunder(float value);
	@Accessor void setPrevThunder(float value);
}
