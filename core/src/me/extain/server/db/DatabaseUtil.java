package me.extain.server.db;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import Utils.ConsoleLog;
import me.extain.server.Player.Character;
import me.extain.server.item.ItemFactory;

public class DatabaseUtil {

    private static final String url = "jdbc:mysql://localhost:3306/roguegame?useSSL=false";
    private static final String user = "root";
    private static final String password = "";
    private static final String sercretKey = "ssshh";

    public static void createUser(String username, String email, String accPassword, int rank) {
        String query = "INSERT INTO users(username, email, password, Rank) VALUES(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, AES.encrypt(accPassword, sercretKey));
            pst.setInt(4, rank);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUser(String accName, String accPass) {
        String query = "Select * from users Where username='" + accName + "' and password='" + accPass + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getUserID(String accName, String accPass) {
        String query = "Select * from users Where username='" + accName + "' and password='" + accPass + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static ArrayList<Character> getCharacters(int id) {
        String query = "Select * from characters Where accountID='" + id + "'";

        ArrayList<Character> characters = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Character character = new Character();
                character.setAccountID(rs.getInt("accountID"));
                character.setId(rs.getInt("id"));
                character.equipItems.addAll(Arrays.asList(rs.getString("equipItems").split(",")));
                character.inventoryItems.addAll(Arrays.asList(rs.getString("inventoryItems").split(",")));

                characters.add(character);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return characters;
    }

    public static Character createNewCharacter(int accountID) {
        String query = "INSERT INTO characters(accountID, id, equipItems, inventoryItems) VALUES(?, ?, ?, ?)";

        int id = MathUtils.random(0, 10000);
        String equipItem = "stick,player-idle";
        String inventoryItem = "stick,player-idle";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, accountID);
            pst.setInt(2, id);
            pst.setString(3, equipItem);
            pst.setString(4, inventoryItem);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Character character = new Character();
        character.setAccountID(accountID);
        character.setId(id);
        character.equipItems.addAll(Arrays.asList(equipItem.split(",")));
        character.inventoryItems.addAll(Arrays.asList(inventoryItem.split(",")));

        return character;
    }

    public static void saveCharacter(Character character) {

        if (character == null) return;

        String equipItems = String.join(",", character.getEquipItems());
        String inventoryItems = String.join(",", character.getInventoryItems());
        String query = "UPDATE characters SET equipItems='" + equipItems +"', inventoryItems='" + inventoryItems +"' WHERE id='"+ character.getId() + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.executeUpdate();
            System.out.println("Saved character: " + character.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRank(String accName, String accPass) {
        String query = "Select * from users Where username='" + accName + "' and password='" + accPass + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("Rank");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void rankPlayer(String accName, int rank) {
        String query = "UPDATE users SET Rank='" + rank + "' Where username='" + accName + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.executeUpdate();
        } catch (Exception e) {
            ConsoleLog.logError(e.toString());
        }
    }

    public static void mutePlayer(String accName) {
        String query = "UPDATE users SET Muted=true Where username='" + accName + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.executeUpdate();
        } catch (Exception e) {
            ConsoleLog.logError(e.toString());
        }
    }

    public static void banPlayer(String accName) {
        String query = "UPDATE users SET Banned=true Where username='" + accName + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.executeUpdate();
        } catch (Exception e) {
            ConsoleLog.logError(e.toString());
        }
    }

    public static boolean isMuted(String accName) {
        String query = "Select * from users Where username='" + accName + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("Muted");
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isBanned(String accName) {
        String query = "Select * from users Where username='" + accName + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getBoolean("Banned");
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



}
