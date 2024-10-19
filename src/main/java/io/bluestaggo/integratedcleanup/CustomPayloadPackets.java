package io.bluestaggo.integratedcleanup;

import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import java.nio.ByteBuffer;

public final class CustomPayloadPackets {
	private CustomPayloadPackets() {
	}

	public static CustomPayloadS2CPacket createKnockbackYaw(float value) {
		return new CustomPayloadS2CPacket(IntegratedCleanup.CUSTOM_PAYLOAD_KNOCKBACK_YAW,
			ByteBuffer.allocate(4).putFloat(value).array());
	}

	public static float readKnockbackYaw(byte[] data) {
		if (data.length < 4) return 0.0F;
		return ByteBuffer.wrap(data).getFloat();
	}

	public static class VisualEntityEvent {
		public int entityId;
		public byte eventType;
	}

	public static CustomPayloadS2CPacket createVisualEntityEvent(VisualEntityEvent value) {
		return new CustomPayloadS2CPacket(IntegratedCleanup.CUSTOM_PAYLOAD_VISUAL_ENTITY_EVENT,
			ByteBuffer.allocate(5).putInt(value.entityId).put(value.eventType).array());
	}

	public static VisualEntityEvent readVisualEntityEvent(byte[] data) {
		VisualEntityEvent value = new VisualEntityEvent();
		if (data.length < 5) return value;

		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		value.entityId = byteBuffer.getInt();
		value.eventType = byteBuffer.get();
		return value;
	}
}
