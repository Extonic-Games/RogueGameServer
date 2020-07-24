package me.extain.server.network;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.extain.server.network.command.GiveCommand;
import me.extain.server.objects.Player.Account;
import me.extain.server.RogueGameServer;
import me.extain.server.network.command.BanCommand;
import me.extain.server.network.command.Command;
import me.extain.server.network.command.GodCommand;
import me.extain.server.network.command.KickCommand;
import me.extain.server.network.command.MuteCommand;
import me.extain.server.network.command.RankCommand;
import me.extain.server.network.command.ReloadCommand;
import me.extain.server.network.command.TeleportCommand;
import me.extain.server.network.command.TestCommand;
import me.extain.server.network.packets.MessagePacket;

public class CommandHandler {


    private HashMap<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();

        addCommand(new TestCommand());
        addCommand(new KickCommand());
        addCommand(new MuteCommand());
        addCommand(new GodCommand());
        addCommand(new BanCommand());
        addCommand(new TeleportCommand());
        addCommand(new RankCommand());
        addCommand(new ReloadCommand());
        addCommand(new GiveCommand());
    }

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void processCommand(Connection connection, String message) {
        String[] args = message.split(" ");

         if (args[0].equals("/help") || args[0].equals("/?")) {
            listCommands(connection);
        } else
        if (commands.get(args[0]) != null) {
            if (commands.get(args[0]).getPerm() <= RogueGameServer.getInstance().getAccounts().get(connection.getID()).getRank())
                commands.get(args[0]).init(RogueGameServer.getInstance().getAccounts().get(connection.getID()), args);
            else {
                MessagePacket packet = new MessagePacket();
                packet.username = "[ORANGE]Server[]";
                packet.message = "You do not have permission!";

                RogueGameServer.getInstance().getServer().sendToUDP(connection.getID(), packet);
            }
        }
    }

    public void listCommands(Connection connection) {
        Account account = RogueGameServer.getInstance().getAccounts().get(connection.getID());
        String[] commandList = new String[commands.size()];
        int index = 0;
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            if (entry.getValue().getPerm() <= account.getRank()) {
                commandList[index] = entry.getValue().getName();
                index ++;
            }
        }

        ArrayList<String> availCommands = new ArrayList<>();

        for (String data : commandList) {
            if (data != null) availCommands.add(data);
        }

        MessagePacket packet = new MessagePacket();
        packet.username = "[ORANGE]Server[]";

        packet.message = String.join(", ", availCommands);

        RogueGameServer.getInstance().getServer().sendToUDP(account.getConnectionID(), packet);
    }


}
