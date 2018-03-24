package com.ate.chatOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SpammerCommand implements ICommand {
	private static final ChatStyle red = new ChatStyle().setColor(EnumChatFormatting.RED);
	private static final ChatStyle blue = new ChatStyle().setColor(EnumChatFormatting.AQUA);
	private static final ChatStyle dblue = new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA);
	private static final ChatStyle gray = new ChatStyle().setColor(EnumChatFormatting.GRAY);
	private final List<String> aliases;
	private final SubCommand[] subCommands = {
			new SubCommand() {
				public String getName() {return "help";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					IChatComponent msg = new ChatComponentText("-- "+I18n.format("chatOption.cmd.help.title")+" "+ModMain.NAME+" "+ModMain.VERSION+" --").setChatStyle(red);
					for(SubCommand sc: subCommands)
						msg.appendSibling(new ChatComponentText("\n/"+mainCommand.getName()+" "+sc.getUsage()).setChatStyle(dblue))
						.appendSibling(new ChatComponentText(" : ").setChatStyle(gray))
						.appendSibling(new ChatComponentText(I18n.format("chatOption.cmd."+sc.getName())).setChatStyle(blue));
					sender.addChatMessage(msg);
					return true;
				}
			},
			new SubCommand() {
				public String getName() {return "gui";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
		        	FMLCommonHandler.instance().bus().register(new Object() {
						private int l = 10;
						@SubscribeEvent
						public void ontick(TickEvent ev) {
							l--;
							if(l==0)Minecraft.getMinecraft().displayGuiScreen(new GuiSpammer(null));
							if(l<=0)FMLCommonHandler.instance().bus().unregister(this);
						}
					});
					return true;
				}
			},
			new SubCommand() {
				public String getName() {return "stop";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					ModMain.isEnable = false;
					sender.addChatMessage(new ChatComponentText(I18n.format("chatOption.cmd.stop.msg"))
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
					return true;
				}
			},
			new SubCommand() {
				public String getName() {return "start";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					ModMain.action=0;
					ModMain.tickw=0;
					ModMain.isEnable = true;
					sender.addChatMessage(new ChatComponentText(I18n.format("chatOption.cmd.start.msg"))
							.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
					return true;
				}
			},
			new SubCommand() {
				public String getName() {return "try";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					ModMain.sendMessage(Minecraft.getMinecraft(), "/"+ModMain.spamCommand);
					return true;
				}
			},
			new SubCommand() {
				public String getName() {return "optionslist";}
				public String getUsage() {return this.getName();}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] a,BlockPos p){
					return new ArrayList<String>();
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					IChatComponent msg = new ChatComponentText("-- "+I18n.format("chatOption.to")+" --").setChatStyle(red);
					for (TextOption to: ModMain.textOptions)
						msg.appendSibling(new ChatComponentText("\n- ").setChatStyle(gray))
						.appendSibling(new ChatComponentText("&"+to.getName()).setChatStyle(dblue))
						.appendSibling(new ChatComponentText(" : ").setChatStyle(gray))
						.appendSibling(new ChatComponentText(I18n.format("chatOption.to."+to.getName())).setChatStyle(blue));
					sender.addChatMessage(msg);
					return true;
				}
			},
			new SubCommand() {
				private Value[] values = new Value[] {
					new Value()	{
						public String getName() {return "cmd";}
						public String getValue() {return ModMain.spamCommand;}
						public void setValue(String s) throws Exception {ModMain.spamCommand=s;}
					},
					new Value()	{
						public String getName() {return "showbutton";}
						public String getValue() {return String.valueOf(ModMain.showButton);}
						public void setValue(String s) throws Exception {ModMain.showButton=Boolean.valueOf(s);}
					},
					new Value()	{
						public String getName() {return "renderoverlay";}
						public String getValue() {return String.valueOf(ModMain.renderGameOverlay);}
						public void setValue(String s) throws Exception {ModMain.renderGameOverlay=Boolean.valueOf(s);}
					},
					new Value()	{
						public String getName() {return "stoponclose";}
						public String getValue() {return String.valueOf(ModMain.stopOnClose);}
						public void setValue(String s) throws Exception {ModMain.stopOnClose=Boolean.valueOf(s);}
					},
					new Value()	{
						public String getName() {return "action";}
						public String getValue() {return String.valueOf(ModMain.numberOfAction);}
						public void setValue(String s) throws Exception {ModMain.numberOfAction=Integer.valueOf(s);}
					},
					new Value()	{
						public String getName() {return "tick";}
						public String getValue() {return String.valueOf(ModMain.ticktowait);}
						public void setValue(String s) throws Exception {ModMain.ticktowait=Integer.valueOf(s);}
					}
				};
				private Value getValueByName(String name) {
					for (Value v: values)
						if(v.getName().equalsIgnoreCase(name))return v;
					return null;
				}
				public String getName() {return "set";}
				public String getUsage() {return this.getName()+" <"+I18n.format("chatOption.cmd.set.name")+"> <"+I18n.format("chatOption.cmd.set.value")+">";}
				public List addTabCompletionOptions(ICommand mainCommand, ICommandSender s,String[] args,BlockPos p){
					if(args.length!=1)return new ArrayList<String>();
					List<String> tab = new ArrayList<String>();
					for (Value v: values) tab.add(v.getName());
					return getTabCompletion(tab, args);
				}
				public boolean execute(ICommand mainCommand, ICommandSender sender, String[] args) {
					if(args.length==0) {
						IChatComponent msg = new ChatComponentText("-- "+I18n.format("chatOption.cmd.set.list")+" --")
								.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
						for (Value v: values)
							msg.appendSibling(new ChatComponentText("\n- ").setChatStyle(gray))
								.appendSibling(new ChatComponentText(v.getName()).setChatStyle(dblue))
								.appendSibling(new ChatComponentText(" : ").setChatStyle(gray))
								.appendSibling(new ChatComponentText(I18n.format("chatOption.cmd.set."+v.getName())).setChatStyle(blue));
						sender.addChatMessage(msg.appendSibling(new ChatComponentText("\n/"+mainCommand.getName()+" "+getUsage()).setChatStyle(red)));
					} else {
						Value v;
						if((v=getValueByName(args[0]))!=null) {
							if(args.length>1) {
								try {
									for (int i = 2; i < args.length; i++)
										args[1]+=" "+args[i];
									v.setValue(args[1]);
									sender.addChatMessage(new ChatComponentText(v.getName()+"="+v.getValue())
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
									ModMain.saveConfig();
								} catch (Exception e) {
									sender.addChatMessage(new ChatComponentText(I18n.format("chatOption.cmd.set.error.exception"))
											.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
								}
							} else {
								sender.addChatMessage(new ChatComponentText(v.getName()+"="+v.getValue())
										.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW)));
							}
						} else {
							sender.addChatMessage(new ChatComponentText(I18n.format("chatOption.cmd.set.error", "/"+mainCommand.getName()+" "+getName()))
									.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
						}
					}
					return true;
				}
			}
	};
	public static abstract class Value {
		public abstract String getName();
		public abstract String getValue();
		public abstract void setValue(String s) throws Exception ;
		
	}
	public SpammerCommand() {
		aliases = new ArrayList<String>(); 
        aliases.add(getName());
        aliases.add("spam");
	}
	@Override
	public int compareTo(Object cmd) {
		return this.getName().compareTo(((ICommand) cmd).getName());
	}
	@Override
	public String getName() {
		return "spammer";
	}
	@Override
	public String getCommandUsage(ICommandSender sender) {
		String s = "";
		for (int i = 0; i < subCommands.length; i++) {
			if(i>0)s+="|";
			s+=subCommands[i].getName();
		}
		return getName()+" ["+s+"]";
	}

	@Override
	public List getAliases() {
		return this.aliases;
	}
	public SubCommand getSubCommandByName(String name) {
		for(SubCommand sc: subCommands)
			if(sc.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase()))return sc;
		return null;
	}
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if(args.length==0) args = new String[] {"help"};
		SubCommand sc = getSubCommandByName(args[0]);
		if(sc!=null) {
			String[] SCargs=new String[args.length-1];
			System.arraycopy(args, 1, SCargs, 0, SCargs.length);
			if(!sc.execute(this, sender, SCargs))
				sender.addChatMessage(new ChatComponentText(sc.getUsage())
						.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		} else {
			sender.addChatMessage(new ChatComponentText(I18n.format("chatOption.cmd.error", "\"/"+getName()+" help\""))
					.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
		}
	}
	public List<String> getSubCommandsNames(){
		List<String> ls = new ArrayList<String>();
		for (SubCommand sc: subCommands)
			ls.add(sc.getName());
		ls.sort(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		return ls;
	}
	@Override
	public boolean canCommandSenderUse(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		for (SubCommand sc: subCommands) {
			if(args[0].equals(sc.getName())) {
				String[] SCargs=new String[args.length-1];
				System.arraycopy(args, 1, SCargs, 0, SCargs.length);
				return sc.addTabCompletionOptions(this, sender, SCargs, pos);
			}
		}
		return getTabCompletion(getSubCommandsNames(), args);
	}
	public static List<String> getTabCompletion(List<String> options, String[] args){
		List<String> options_End=new ArrayList<String>();
		if(args.length==0)return options_End;
		String start=args[args.length-1].toLowerCase();
		for (int i = 0; i < options.size(); i++) {
			if(options.get(i).toLowerCase().startsWith(start.toLowerCase()))options_End.add(options.get(i));
		}
		options_End.sort(new Comparator<String>(){
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}}); //sort by name
		return options_End;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
