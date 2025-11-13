package net.frozenblock.boww.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.resources.ResourceLocation
import org.lwjgl.glfw.GLFW

class BOWWClient : ClientModInitializer {
    override fun onInitializeClient() {
    }

    companion object {
        @JvmField
        val BINDING_CATEGORY: KeyMapping.Category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("boww", "boww"))

        @JvmField
        val DISABLE_GLIDER: KeyMapping = KeyBindingHelper.registerKeyBinding(KeyMapping("key.boww.disable_glider", GLFW.GLFW_KEY_LEFT_CONTROL, BINDING_CATEGORY))
    }
}
