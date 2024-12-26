package net.frozenblock.ml_template;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class FrozenBlockMLTemplateClient {

	public static void onInitialize() {

		registerClientEvents();
	}

	private static void registerClientEvents() {

	}
}
