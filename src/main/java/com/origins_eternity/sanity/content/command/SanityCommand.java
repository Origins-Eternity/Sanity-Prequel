package com.origins_eternity.sanity.content.command;

import com.origins_eternity.sanity.content.capability.Capabilities;
import com.origins_eternity.sanity.content.capability.sanity.ISanity;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
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
        if (args.length == 2 || args.length == 3) {
            EntityPlayerMP player = args.length == 2 ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[1]);
            ISanity sanity = player.getCapability(Capabilities.SANITY, null);
            if (!sanity.getEnable()) {
                throw new CommandException("commands.sanity.disabled");
            } else {
                Scanner scanner = args.length == 2 ? new Scanner(args[1]) : new Scanner(args[2]);
                if (!scanner.hasNextDouble()) {
                    throw new NumberInvalidException("commands.generic.num.invalid", scanner.next());
                } else {
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
                        default:
                            throw new WrongUsageException("commands.sanity.usage");
                    }
                }
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
        } else {
            return args.length == 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}