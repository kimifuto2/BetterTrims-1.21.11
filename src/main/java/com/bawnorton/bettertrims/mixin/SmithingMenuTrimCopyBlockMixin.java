package com.bawnorton.bettertrims.mixin;

import com.bawnorton.bettertrims.BetterTrims;
import com.bawnorton.bettertrims.mixin.accessor.ItemCombinerMenuAccessor;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// When BetterTrims.disallowTrimTemplateCopy is enabled (default off), copying an armour-trim
// smithing template in the smithing table is blocked: the result slot is emptied so no copy can
// be produced. A hardcore-style restriction for players who want the challenge.
@Mixin(SmithingMenu.class)
abstract class SmithingMenuTrimCopyBlockMixin {
	@Inject(method = "createResult", at = @At("RETURN"))
	private void bettertrims$blockTrimTemplateCopy(CallbackInfo ci) {
		if (!BetterTrims.disallowTrimTemplateCopy) return;

		net.minecraft.world.inventory.ResultContainer resultSlots =
				((ItemCombinerMenuAccessor) (Object) this).bettertrims$getResultSlots();
		ItemStack result = resultSlots.getItem(0);
		if (result.isEmpty()) return;
		// Block when the crafted result is an armour-trim smithing template (copy / transform).
		if (result.getItem() instanceof SmithingTemplateItem) {
			resultSlots.setItem(0, ItemStack.EMPTY);
		}
	}
}
