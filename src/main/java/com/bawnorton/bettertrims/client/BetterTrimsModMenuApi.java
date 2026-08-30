package com.bawnorton.bettertrims.client;

//? if fabric {
import com.bawnorton.bettertrims.client.BetterTrimsConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.minecraft.client.gui.screens.Screen;

@Entrypoint("modmenu")
public final class BetterTrimsModMenuApi implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return BetterTrimsConfigScreen::new;
	}
}
//?} else if neoforge {
/*public final class BetterTrimsModMenuApi {}
*///?}
