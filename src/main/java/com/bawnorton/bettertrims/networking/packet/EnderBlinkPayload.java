package com.bawnorton.bettertrims.networking.packet;

import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

//? if fabric {
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
 //?} else {
/*import net.neoforged.neoforge.network.handling.IPayloadContext;
*///?}

// Client -> server: request to blink (teleport) to a target position, like an ender pearl.
public record EnderBlinkPayload(double x, double y, double z) implements CustomPacketPayload {
	public static final Type<EnderBlinkPayload> TYPE = new Type<>(BetterTrims.rl("ender_blink"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnderBlinkPayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.DOUBLE, EnderBlinkPayload::x,
			ByteBufCodecs.DOUBLE, EnderBlinkPayload::y,
			ByteBufCodecs.DOUBLE, EnderBlinkPayload::z,
			EnderBlinkPayload::new
	);

	public static Vec3 position(EnderBlinkPayload payload) {
		return new Vec3(payload.x(), payload.y(), payload.z());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	//? if fabric {
	public void handle(ServerPlayNetworking.Context context) {
		ServerPlayer player = context.player();
		com.bawnorton.bettertrims.property.ability.misc.EnderBlinkHandler.onBlinkRequest(player, new Vec3(x, y, z));
	}
	//?} else {
	/*public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				com.bawnorton.bettertrims.property.ability.misc.EnderBlinkHandler.onBlinkRequest(player, new Vec3(x, y, z));
			}
		});
	}
	*///?}
}
