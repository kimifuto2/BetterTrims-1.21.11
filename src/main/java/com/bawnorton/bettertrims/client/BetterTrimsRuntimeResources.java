package com.bawnorton.bettertrims.client;

//? if fabric {
import com.bawnorton.bettertrims.BetterTrims;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.Optional;

/**
 * Runtime helper that toggles the built-in {@code bettertrims:trim_effects} datapack and
 * triggers a server-side resource reload so the change takes effect immediately in a
 * singleplayer (or LAN-hosted) world, instead of only on next launch.
 *
 * <p>{@code registerBuiltinResourcePack} only decides the pack's default activation at mod
 * init, so switching the config mid-game does nothing by itself. This helper re-drives the
 * same PackRepository that powers the datapack list and reloads the server registries.</p>
 */
public final class BetterTrimsRuntimeResources {
	private static final String TARGET_TITLE = "trim_effects";

	private BetterTrimsRuntimeResources() {
	}

	/** Applies the trim-effects datapack state and reloads server resources. Call on the render/client thread. */
	public static void apply(boolean enabled) {
		Minecraft minecraft = Minecraft.getInstance();
		if (!minecraft.hasSingleplayerServer()) {
			BetterTrims.LOGGER.debug("No singleplayer server; trim_effects datapack state not applied at runtime.");
			return;
		}

		IntegratedServer server = minecraft.getSingleplayerServer();
		if (server == null || !server.isRunning()) {
			BetterTrims.LOGGER.debug("Integrated server not running; skipping trim_effects runtime apply.");
			return;
		}

		server.execute(() -> {
			PackRepository repo = server.getPackRepository();

			String packId = findTrimEffectsPackId(repo);
			if (packId == null) {
				BetterTrims.LOGGER.warn("Could not find bettertrims:trim_effects datapack; skipping runtime toggle.");
				return;
			}

			if (enabled) {
				repo.addPack(packId);
			} else {
				repo.removePack(packId);
			}

			server.reloadResources(repo.getSelectedIds());
			BetterTrims.LOGGER.info("Trim effects datapack '{}' set to {}; reloading resources.", packId, enabled);
		});
	}

	/**
	 * Locates the built-in trim_effects pack by matching its location id or title.
	 * Uses the pack's true repository id so we don't have to guess the {@code file/} prefix.
	 */
	private static String findTrimEffectsPackId(PackRepository repo) {
		Optional<Pack> match = repo.getAvailablePacks().stream()
				.filter(pack -> {
					String id = pack.getId().toLowerCase();
					String title = pack.getTitle().getString().toLowerCase();
					String description = pack.getDescription().getString().toLowerCase();
					return id.contains(TARGET_TITLE) || title.contains(TARGET_TITLE) || description.contains(TARGET_TITLE);
				})
				.findFirst();
		return match.map(Pack::getId).orElse(null);
	}
}
//?} else if neoforge {
/*public final class BetterTrimsRuntimeResources {
	private BetterTrimsRuntimeResources() {}
	public static void apply(boolean enabled) {}
}
*///?}
