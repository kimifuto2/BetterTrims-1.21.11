package com.bawnorton.bettertrims.property.ability.type.misc;

import com.bawnorton.bettertrims.property.element.TrimElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record DamageImmunityAbility() implements TrimElement {
	public static final DamageImmunityAbility INSTANCE = new DamageImmunityAbility();
	public static final Codec<DamageImmunityAbility> CODEC = MapCodec.unitCodec(INSTANCE);
}
