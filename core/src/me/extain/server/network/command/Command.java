package me.extain.server.network.command;

import com.esotericsoftware.kryonet.Connection;

import me.extain.server.Player.Account;

public interface Command {

    public void init(Account account, String[] args);

    public String getName();

    public int getPerm();


}
