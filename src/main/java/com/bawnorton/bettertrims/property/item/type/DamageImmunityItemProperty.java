package com.bawnorton.bettertrims.property.item.type;

import com.bawnorton.bettertrims.property.element.TrimElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record DamageImmunityItemProperty() implements TrimElement {
	public static final DamageImmunityItemProperty INSTANCE = new DamageImmunityItemProperty();
	public static final Codec<DamageImmunityItemProperty> CODEC = MapCodec.unitCodec(INSTANCE);
}
