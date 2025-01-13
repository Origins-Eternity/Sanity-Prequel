package com.origins_eternity.sanity.content.command;

import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SanityCommand extends CommandBase {
    private static final String name = "sanity";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.sanity.usage";
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add(name);
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            if (sender instanceof EntityPlayerMP) {
                Scanner scanner = new Scanner(args[1]);
                if (scanner.hasNextDouble()) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    ISanity sanity = player.getCapability(Capabilities.SANITY, null);
                    double value = scanner.nextDouble();
                    switch (args[0]) {
                        case "add":
                            sanity.recoverSanity(value);
                            break;
                        case "remove":
                            sanity.consumeSanity(value);
                            break;
                        case "set":
                            sanity.setSanity(value);
                            break;
                    }
                } else {
                    throw new WrongUsageException("commands.sanity.usage");
                }
            }
        } else if (args.length == 3) {
            EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[1]);
            if (player != null) {
                Scanner scanner = new Scanner(args[2]);
                if (scanner.hasNextDouble()) {
                    ISanity sanity = player.getCapability(Capabilities.SANITY, null);
                    double value = scanner.nextDouble();
                    switch (args[0]) {
                        case "add":
                            sanity.recoverSanity(value);
                            break;
                        case "remove":
                            sanity.consumeSanity(value);
                            break;
                        case "set":
                            sanity.setSanity(value);
                            break;
                    }
                } else {
                    throw new WrongUsageException("commands.sanity.usage");
                }
            } else {
                throw new WrongUsageException("commands.sanity.usage");
            }
        } else {
            throw new WrongUsageException("commands.sanity.usage");
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(2, name);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "add", "remove", "set");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("set"))) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return args.length > 1 && index == 1;
    }

    @Override
    public int compareTo(ICommand command) {
        return 0;
    }
}