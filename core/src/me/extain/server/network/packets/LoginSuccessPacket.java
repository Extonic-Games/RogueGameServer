package me.extain.server.network.packets;

import java.util.ArrayList;
import me.extain.server.objects.Player.Character;

public class LoginSuccessPacket {

    public int id;
    public String username;
    public ArrayList<Character> characters;

}
