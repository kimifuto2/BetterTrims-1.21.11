package com.bawnorton.bettertrims.client;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.configurable.api.ConfigurableApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

// ModMenu config screen for BetterTrims: toggles the trim-effect resource pack, the debug flag,
// and the "disallow armour-trim template copy" hardcore option. Values are written to the
// configurable-backed fields and saved with ConfigurableApi.saveChanges().
public class BetterTrimsConfigScreen extends Screen {
	private final Screen parent;

	public BetterTrimsConfigScreen(Screen parent) {
		super(Component.translatable("bettertrims.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		addRenderableWidget(CycleButton.onOffBuilder(BetterTrims.disallowTrimTemplateCopy)
				.create(10, 60, 200, 20, Component.translatable("bettertrims.config.disallowTrimTemplateCopy"), (button, value) -> {
					BetterTrims.disallowTrimTemplateCopy = value;
					ConfigurableApi.saveChanges();
				}));

		addRenderableWidget(CycleButton.onOffBuilder(BetterTrims.enableTrimEffects)
				.create(10, 90, 200, 20, Component.translatable("bettertrims.config.enableTrimEffects"), (button, value) -> {
					BetterTrims.enableTrimEffects = value;
					ConfigurableApi.saveChanges();
				}));

		addRenderableWidget(CycleButton.onOffBuilder(BetterTrims.debug)
				.create(10, 120, 200, 20, Component.translatable("bettertrims.config.debug"), (button, value) -> {
					BetterTrims.debug = value;
					ConfigurableApi.saveChanges();
				}));

		addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
				Component.translatable("gui.done"),
				button -> onClose()
		).bounds(10, 160, 200, 20).build());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);
		MutableComponent title = Component.translatable("bettertrims.config.title");
		graphics.drawCenteredString(this.font, title, this.width / 2, 20, 0xFFFFFFFF);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
