package com.bawnorton.bettertrims.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

// ModMenu integration: opens BetterTrimsConfigScreen from the Mods list.
public class BetterTrimsModMenuApi implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return BetterTrimsConfigScreen::new;
	}
}
