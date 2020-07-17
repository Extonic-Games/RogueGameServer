package me.extain.server.packets;

import java.util.ArrayList;
import me.extain.server.objects.Player.Character;

public class LoginSuccessPacket {

    public int id;
    public String username;
    public ArrayList<Character> characters;

}
