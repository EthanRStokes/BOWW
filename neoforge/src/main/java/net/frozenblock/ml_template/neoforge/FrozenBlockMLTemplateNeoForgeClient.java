package net.frozenblock.ml_template.neoforge;

import net.frozenblock.ml_template.FrozenBlockMLTemplateConstants;
import net.frozenblock.ml_template.FrozenBlockMLTemplateClient;
import net.frozenblock.ml_template.config.frozenlib_config.gui.FrozenLibConfigGui;
import net.frozenblock.ml_template.neoforge.event.impl.FrozenBlockMLTemplateNeoForgeClientEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Mod(value = FrozenBlockMLTemplateConstants.MOD_ID, dist = Dist.CLIENT)
public class FrozenBlockMLTemplateNeoForgeClient {

	public FrozenBlockMLTemplateNeoForgeClient(@NotNull IEventBus eventBus, ModContainer container) {
		NeoForge.EVENT_BUS.register(FrozenBlockMLTemplateNeoForgeClientEvents.class);
		createConfigScreen(container);
		FrozenBlockMLTemplateClient.onInitialize();
	}

	private static void createConfigScreen(ModContainer container) {
		if (FrozenBlockMLTemplateConstants.HAS_CLOTH_CONFIG) {
			container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> FrozenLibConfigGui.createScreen(screen));
		}
	}
}
