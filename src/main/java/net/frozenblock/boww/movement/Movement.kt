package net.frozenblock.boww.movement

import com.mojang.serialization.Codec
import net.frozenblock.boww.impl.MovementPlayer
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.player.Player
import org.apache.commons.lang3.ObjectUtils
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max

// todo gliding, ascending from FrozenLib wind like TCA fans, stamina
class Movement(val player: Player) {
    var hasGlider = true // TODO add glider special item

    // todo add keybind to stop gliding
    var gliding: Boolean = false
        get() = field && hasGlider
        set(value) {
            if (!hasGlider) return

            if (field != value) {
                field = value
                this.syncData()
            }
        }

    var ascending: Boolean = false
        get() = field && hasGlider
        set(value) {
            if (hasGlider) field = value
        }

    var stamina: Stamina = Stamina(player)

    fun useMovement() {
        if (gliding) {
            player.resetFallDistance()

            val movement = player.deltaMovement
            val yMovement = max(movement.y, -0.05)
            player.setDeltaMovement(movement.x, yMovement, movement.z)

            if (stamina.depleted()) {
                gliding = false
            }
            else stamina.decrease()
        }
        if (player.level().isClientSide) {
            syncData()
        }
    }

    fun load(tag: CompoundTag): CompoundTag {
        hasGlider = tag.getBoolean("hasGlider").getOrNull()!!
        gliding = tag.getBoolean("gliding").getOrNull()!!
        ascending = tag.getBoolean("ascending").getOrNull()!!
        stamina.load(tag.getCompound("stamina").getOrNull()!!)
        return tag
    }

    fun save(tag: CompoundTag): CompoundTag {
        tag.putBoolean("hasGlider", hasGlider)
        tag.putBoolean("gliding", gliding)
        tag.putBoolean("ascending", ascending)
        tag.put("stamina", CompoundTag().also {
            stamina.save(it)
        })
        return tag
    }

    fun syncData() {
        this.player.`BOWW$updateSynchedBOWWData`()
    }
}

// todo
class Stamina {
    val player: Player
    val data: StaminaData

    constructor(player: Player) {
        this.player = player

        if (player.hasAttached(MovementPlayer.STAMINA)) {
            this.data = player.getAttached(MovementPlayer.STAMINA)!!
        } else {
            this.data = StaminaData(1000.0)
            player.setAttached(MovementPlayer.STAMINA, this.data)
        }
    }

    fun decrease() {
        this.data.stamina -= 20

        if (this.depleted()) {
            this.data.stamina = 0.0

            this.data.timeout = 50
        }
    }

    inline fun depleted(): Boolean = data.stamina <= 0.0

    fun load(tag: CompoundTag) {
        this.data.stamina = tag.getDouble("stamina").getOrNull()!!
        this.data.timeout = tag.getShortOr("timeout", 0)
    }

    fun save(tag: CompoundTag) {
        tag.putDouble("stamina", this.data.stamina)
        tag.putString("timeout", this.data.timeout.toString())
    }
}

data class StaminaData(var stamina: Double, @JvmField var timeout: Short = 0)

fun <T> SynchedEntityData.setWithoutLocalUpdate(key: EntityDataAccessor<T>, value: T, force: Boolean = false) {
    val dataItem = this.getItem(key)
    if (force || ObjectUtils.notEqual(value, dataItem.value)) {
        dataItem.value = value
        dataItem.isDirty = true
        this.isDirty = true
    }
}
