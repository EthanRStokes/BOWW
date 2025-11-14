package net.frozenblock.boww

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.frozenblock.boww.impl.MovementPlayer
import net.frozenblock.boww.movement.Movement
import net.frozenblock.boww.network.C2SGlidePacket
import net.minecraft.world.entity.player.Player

class BOWW : ModInitializer {
    override fun onInitialize() {
        println("BOWW Initializing")

        val registry = PayloadTypeRegistry.playC2S()
        registry.register(C2SGlidePacket.TYPE, C2SGlidePacket.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(C2SGlidePacket.TYPE) { packet, ctx ->
            ctx.player().movement.gliding = packet.gliding
        }
    }
}

inline val Player.movement: Movement get() = this.`bOWW$getMovement`()
