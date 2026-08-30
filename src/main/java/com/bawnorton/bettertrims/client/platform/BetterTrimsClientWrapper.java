package com.bawnorton.bettertrims.client.platform;

import com.bawnorton.bettertrims.client.BetterTrimsClient;
import com.bawnorton.bettertrims.client.tooltip.AbilityTooltipRenderer;

//? if fabric {
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

@Entrypoint
public final class BetterTrimsClientWrapper implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BetterTrimsClient.init();
		ItemTooltipCallback.EVENT.register((stack, context, flag, tooltip) ->
				AbilityTooltipRenderer.appendSimpleDescription(stack, tooltip));
	}
}
//?} else if neoforge {
/*import com.bawnorton.bettertrims.BetterTrims;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = BetterTrims.MOD_ID, dist = Dist.CLIENT)
public final class BetterTrimsClientWrapper {
	public BetterTrimsClientWrapper() {
		BetterTrimsClient.init();
	}
}
*///?}
