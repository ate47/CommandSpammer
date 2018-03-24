package com.ate.chatOptions;

import net.minecraft.client.Minecraft;

public abstract class TextOption {
	public abstract String getName();
	public abstract String getReplacement(Minecraft mc);
}
