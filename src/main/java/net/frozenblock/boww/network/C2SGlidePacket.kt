package net.frozenblock.boww.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class C2SGlidePacket(val gliding: Boolean) : CustomPacketPayload {
    companion object {
        @JvmField
        val TYPE: CustomPacketPayload.Type<C2SGlidePacket> = CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath("boww", "glide"))

        @JvmField
        val CODEC: StreamCodec<FriendlyByteBuf, C2SGlidePacket> = StreamCodec.ofMember(C2SGlidePacket::write, ::C2SGlidePacket)
    }

    constructor(buf: FriendlyByteBuf) : this(buf.readBoolean())

    fun write(buf: FriendlyByteBuf) {
        buf.writeBoolean(gliding)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
