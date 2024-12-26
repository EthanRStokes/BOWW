package net.frozenblock.ml_template;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.NOPLogger;

public class FrozenBlockMLTemplateConstants {
	@ApiStatus.Internal
	public static final String PROJECT_ID = "FrozenBlock Multiloader Template";
	@ApiStatus.Internal
	public static final String MOD_ID = "frozenblock_multiloader_template";
	/**
	 * NeoForge's packet version
	 */
	@ApiStatus.Internal
	public static final String PACKET_VERSION = "1";
	@ApiStatus.Internal
	public static final NOPLogger LOGGER4 = NOPLogger.NOP_LOGGER;
	@ApiStatus.Internal
	public static final int DATA_VERSION = 1;
	/**
	 * Whether the current instance is running in datagen mode.
	 */
	public static final boolean IS_DATAGEN = isDatagen();
	/**
	 * Used for features that may be unstable and crash in public builds.
	 * <p>
	 * It's smart to use this for at least registries.
	 */
	@ApiStatus.Internal
	public static boolean UNSTABLE_LOGGING = PlatformHelper.isDevelopmentEnvironment();
	/**
	 * This is set to true when {@link Bootstrap#bootStrap()} is finished.
	 */
	public static boolean isInitialized;
	public static boolean useNewDripstoneLiquid = false;

	@ApiStatus.Internal
	private static boolean isDatagen() {
		return PlatformHelper.isDatagen();
	}

	public static boolean hasMod(String modID) {
		return PlatformHelper.isModLoaded(modID);
	}

	@ApiStatus.Internal
	@Contract("_ -> new")
	@NotNull
	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(FrozenBlockMLTemplateConstants.MOD_ID, path);
	}

	@ApiStatus.Internal
	@NotNull
	public static String string(String path) {
		return id(path).toString();
	}
}
