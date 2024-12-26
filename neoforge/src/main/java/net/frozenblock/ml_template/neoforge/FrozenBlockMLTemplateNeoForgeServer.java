package net.frozenblock.ml_template.neoforge;

import net.frozenblock.ml_template.FrozenBlockMLTemplate;
import net.frozenblock.ml_template.FrozenBlockMLTemplateConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod(value = FrozenBlockMLTemplateConstants.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class FrozenBlockMLTemplateNeoForgeServer {
	public FrozenBlockMLTemplateNeoForgeServer(@NotNull IEventBus eventBus, ModContainer container) {
		FrozenBlockMLTemplate.onInitializeServer();
	}
}
