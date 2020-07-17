package me.extain.server.objects.Player;

import java.util.ArrayList;

import me.extain.server.db.DatabaseUtil;

public class Account {

    public int id;
    private int connectionID;
    public String username;
    private String email;
    private String password;
    private int rank = 0;
    public boolean isBanned = false;
    public boolean isMuted = false;

    private ArrayList<Character> characters;
    private Character selectedChar;

    public Account(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;

        getChars();
    }

    private void getChars() {
        this.characters = DatabaseUtil.getCharacters(id);
    }

    public int getRank() {
        return rank;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }

    public void setSelectedChar(Character selectedChar) {
        this.selectedChar = selectedChar;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public Character getSelectedChar() {
        return selectedChar;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public int getConnectionID() {
        return connectionID;
    }
}
