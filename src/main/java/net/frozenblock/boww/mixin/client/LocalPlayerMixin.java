package net.frozenblock.boww.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.frozenblock.boww.client.BOWWClient;
import net.frozenblock.boww.impl.MovementPlayer;
import net.frozenblock.boww.movement.Movement;
import net.frozenblock.boww.movement.Stamina;
import net.frozenblock.boww.movement.StaminaData;
import net.frozenblock.boww.network.C2SGlidePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements MovementPlayer {

	@Shadow
	public ClientInput input;

	@Shadow
	@Final
	protected Minecraft minecraft;

	public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
		super(clientLevel, gameProfile);
	}

	/// Stops sprinting when the player stops holding the sprint button
	@ModifyReturnValue(method = "shouldStopRunSprinting", at = @At("RETURN"))
	private boolean canStop(boolean original) {
		return original || !this.input.keyPresses.sprint() || this.bOWW$getMovement().getGliding() || this.bOWW$getMovement().getStamina().depleted();
	}

	/// Cant start sprinting mid air
	@ModifyReturnValue(method = "canStartSprinting", at = @At("RETURN"))
	private boolean canStartSprinting(boolean original) {
		return original && (this.onGround() || this.isSwimming()) && !this.bOWW$getMovement().getStamina().depleted();
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Input;jump()Z", ordinal = 2))
	private void checkParaglider(
		CallbackInfo ci,
		@Local Abilities abilities,
		@Local(ordinal = 0) boolean bl,
		@Local(ordinal = 3) boolean bl4,
		@Local(ordinal = 4) LocalBooleanRef bl5
	) {
		Movement movement = this.bOWW$getMovement();
		if (!movement.getHasGlider()) return;

		if (!abilities.flying && !this.minecraft.gameMode.isAlwaysFlying() && !this.onGround() && !this.isSwimming()) {
			if (BOWWClient.DISABLE_GLIDER.consumeClick()) {
				movement.setGliding(false);
				ClientPlayNetworking.send(new C2SGlidePacket(false));
			} else if (!bl && this.input.keyPresses.jump() && !bl4 && !movement.getGliding()) {
				movement.setGliding(true);
				ClientPlayNetworking.send(new C2SGlidePacket(true));

				//noinspection StatementWithEmptyBody
				while (BOWWClient.DISABLE_GLIDER.consumeClick()) {}
			}
		} else {
			if (movement.getGliding()) {
				movement.setGliding(false);
				ClientPlayNetworking.send(new C2SGlidePacket(false));
			}
		}
	}

	@ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSprinting()Z"))
	private boolean isSprinting(boolean original) {
		Movement movement = this.bOWW$getMovement();
		Stamina stamina = movement.getStamina();
		if (stamina.depleted()) {
			StaminaData data = stamina.getData();
			--data.timeout;
			if (data.timeout <= 0) {
				data.timeout = 0;
				data.setStamina(1000.0);
			}
		}
		if (original)
			this.bOWW$getMovement().getStamina().decrease();
		return original;
	}
}
