package com.bawnorton.bettertrims.client;

//? if fabric {
import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.configurable.api.ConfigurableApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BetterTrimsConfigScreen extends Screen {
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;

	public BetterTrimsConfigScreen(Screen parent) {
		super(Component.translatable("bettertrims.config.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int centerX = this.width / 2;
		int top = this.height / 2 - 40;

		addRenderableWidget(CycleButton.<Boolean>onOffBuilder(BetterTrims.enableTrimEffects)
				.create(centerX - BUTTON_WIDTH / 2, top, BUTTON_WIDTH, BUTTON_HEIGHT,
						Component.translatable("bettertrims.config.enableTrimEffects"),
						(button, value) -> {
							BetterTrims.enableTrimEffects = value;
							ConfigurableApi.saveChanges();
							BetterTrimsRuntimeResources.apply(value);
							BetterTrims.LOGGER.debug("enableTrimEffects set to {}", value);
						}));

		addRenderableWidget(CycleButton.<Boolean>onOffBuilder(BetterTrims.debug)
				.create(centerX - BUTTON_WIDTH / 2, top + BUTTON_HEIGHT + 4, BUTTON_WIDTH, BUTTON_HEIGHT,
						Component.translatable("bettertrims.config.debug"),
						(button, value) -> {
							BetterTrims.debug = value;
							ConfigurableApi.saveChanges();
							BetterTrims.LOGGER.debug("debug set to {}", value);
						}));

		addRenderableWidget(CycleButton.<Boolean>onOffBuilder(BetterTrims.disallowTrimTemplateCopy)
				.create(centerX - BUTTON_WIDTH / 2, top + 2 * (BUTTON_HEIGHT + 4), BUTTON_WIDTH, BUTTON_HEIGHT,
						Component.translatable("bettertrims.config.disallowTrimTemplateCopy"),
						(button, value) -> {
							BetterTrims.disallowTrimTemplateCopy = value;
							ConfigurableApi.saveChanges();
							BetterTrims.LOGGER.debug("disallowTrimTemplateCopy set to {}", value);
						}));

		addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
				.bounds(centerX - BUTTON_WIDTH / 2, this.height - 28, BUTTON_WIDTH, BUTTON_HEIGHT)
				.build());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		super.render(guiGraphics, mouseX, mouseY, delta);
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}
}
//?} else if neoforge {
/*public final class BetterTrimsConfigScreen {}
*///?}
