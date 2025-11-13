package net.frozenblock.boww.impl;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.frozenblock.boww.movement.Movement;
import net.frozenblock.boww.movement.StaminaData;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface MovementPlayer {
	ResourceLocation COMPOUND_TAG_SERIALIZER = Util.make(() -> {
		EntityDataSerializer<CompoundTag> serializer = EntityDataSerializer.forValueType(ByteBufCodecs.COMPOUND_TAG);
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath("boww", "compound_tag");
		FabricTrackedDataRegistry.register(id, serializer);
		return id;
	});

	AttachmentType<StaminaData> STAMINA = AttachmentRegistry.create(ResourceLocation.fromNamespaceAndPath("boww", "stamina"));

	EntityDataAccessor<CompoundTag> MOVEMENT_DATA = (EntityDataAccessor<CompoundTag>) SynchedEntityData.defineId(Player.class, FabricTrackedDataRegistry.get(COMPOUND_TAG_SERIALIZER));

	Movement bOWW$getMovement();

	void BOWW$updateSynchedBOWWData();
}
