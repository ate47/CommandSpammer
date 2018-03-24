package com.ate.chatOptions;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public abstract class SubCommand {
	public abstract String getName();
	public abstract String getUsage();
	public abstract boolean execute(ICommand mainCommand, ICommandSender sender, String[] args);
	public abstract List addTabCompletionOptions(ICommand mainCommand, ICommandSender sender, String[] args, BlockPos pos);
}
