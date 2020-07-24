package me.extain.server.network.packets;


import me.extain.server.objects.Player.Character;
import me.extain.server.ServerPlayer;

public class JoinPacket {

    public ServerPlayer player;
    public Character selectedChar;

}
