package net.frozenblock.boww.mixin;

import net.frozenblock.boww.impl.MovementPlayer;
import net.frozenblock.boww.movement.Movement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends Avatar implements MovementPlayer {

	@Unique
	private static final EntityDataAccessor<CompoundTag> MOVEMENT = MovementPlayer.MOVEMENT_DATA;

	@Unique
	private final Movement movement = new Movement(Player.class.cast(this));

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void defineBOWWSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(MOVEMENT, new CompoundTag());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void readBOWWData(ValueInput valueInput, CallbackInfo ci) {
		valueInput.read("BOWWData", CompoundTag.CODEC).ifPresent(value -> {
			this.movement.load(value);
			Player.class.cast(this).getEntityData().set(MOVEMENT, value, true);
		});
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void addBOWWData(ValueOutput output, CallbackInfo ci) {
		CompoundTag tag = new CompoundTag();
		this.movement.save(tag);
		output.store("BOWWData", CompoundTag.CODEC, tag);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {
		this.movement.useMovement();
	}

	@Override
	public Movement bOWW$getMovement() {
		return this.movement;
	}

	@Override
	public void BOWW$updateSynchedBOWWData() {
		Player.class.cast(this).getEntityData().set(MOVEMENT, this.movement.save(new CompoundTag()), true);
	}
}
