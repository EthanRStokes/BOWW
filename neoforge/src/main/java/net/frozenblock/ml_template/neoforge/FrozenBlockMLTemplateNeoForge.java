package net.frozenblock.ml_template.neoforge;

import net.frozenblock.ml_template.FrozenBlockMLTemplateConstants;
import net.frozenblock.ml_template.FrozenBlockMLTemplate;
import net.frozenblock.ml_template.neoforge.event.impl.FrozenBlockMLTemplateNeoForgeCommonEvents;
import net.frozenblock.ml_template.neoforge.event.impl.FrozenBlockMLTemplateNeoForgeModEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

@Mod(FrozenBlockMLTemplateConstants.MOD_ID)
public class FrozenBlockMLTemplateNeoForge {

	public FrozenBlockMLTemplateNeoForge(@NotNull IEventBus eventBus, ModContainer container) {
		// NEOFORGE SPECIFIC STUFF
		NeoForge.EVENT_BUS.register(FrozenBlockMLTemplateNeoForgeCommonEvents.class);
		eventBus.register(FrozenBlockMLTemplateNeoForgeModEvents.class);

		// COMMON INIT
		FrozenBlockMLTemplate.onInitialize();
	}
}
