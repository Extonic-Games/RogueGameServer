package me.extain.server.network.command;

import me.extain.server.objects.Player.Account;

public interface Command {

    public void init(Account account, String[] args);

    public String getName();

    public int getPerm();


}
