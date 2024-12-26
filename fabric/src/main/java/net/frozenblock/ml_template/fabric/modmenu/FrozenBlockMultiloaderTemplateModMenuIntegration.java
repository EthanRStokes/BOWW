package net.frozenblock.ml_template.fabric.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public class FrozenBlockMultiloaderTemplateModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
		if (FrozenLibConstants.HAS_CLOTH_CONFIG) {
			// return FrozenBlockMultiloaderTemplateConfigGui::createScreen;
		}
		return (screen -> null);
	}
}
