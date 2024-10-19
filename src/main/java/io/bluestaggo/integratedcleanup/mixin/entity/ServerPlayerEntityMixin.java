package io.bluestaggo.integratedcleanup.mixin.entity;

import io.bluestaggo.integratedcleanup.CustomPayloadPackets;
import io.bluestaggo.integratedcleanup.IntegratedCleanup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.network.packet.CustomPayloadPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.ByteBuffer;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	@Shadow public ServerPlayNetworkHandler networkHandler;

	public ServerPlayerEntityMixin(World world, String name) {
		super(world, name);
	}

	@Redirect(
		method = "damage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/entity/living/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
		)
	)
	public boolean damageUpdateKnockbackYaw(PlayerEntity instance, DamageSource source, float amount) {
		boolean damaged = super.damage(source, amount);
		if (damaged) {
			this.networkHandler.sendPacket(CustomPayloadPackets.createKnockbackYaw(this.knockbackVelocity));
		}
		return damaged;
	}
}
