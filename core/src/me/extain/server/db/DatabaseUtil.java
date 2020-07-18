package me.extain.server.db;

import com.badlogic.gdx.math.MathUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Utils.ConsoleLog;
import me.extain.server.objects.Player.Character;
import me.extain.server.item.Item;
import me.extain.server.item.ItemFactory;

public class DatabaseUtil {

    private static final String url = "jdbc:mysql://localhost:3306/roguegame?useSSL=false";
    private static final String user = "root";
    private static final String password = "";
    private static final String sercretKey = "ssshh";

    public static void createUser(String username, String email, String accPassword, int rank) {
        String query = "INSERT INTO users(username, email, password, Rank) VALUES(?, ?, ?, ?)";

        processInsertUser(query, username, email, AES.encrypt(accPassword, sercretKey), rank);
    }

    private static void processInsertUser(String query, Object... params) {
        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, (String) params[0]);
            pst.setString(2, (String) params[1]);
            pst.setString(3, AES.encrypt((String) params[2], sercretKey));
            pst.setInt(4, (Integer) params[3]);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUser(String accName, String accPass) {
        String query = "Select * from users Where username='" + accName + "' and password='" + accPass + "'";

        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            return rs.next();
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
                List<String> equipItems = Arrays.asList(rs.getString("equipItems").split(","));
                List<String> invItems = Arrays.asList(rs.getString("inventoryItems").split(","));

                for (int i = 0; i < equipItems.size(); i++) {
                    if (equipItems.get(i).equals(""))  i++;
                    character.addEquipItem(i, ItemFactory.instantiate().getItem(equipItems.get(i)));
                }

                for (int i = 0; i < invItems.size(); i++) {
                    if (invItems.get(i).equals("")) i++;
                    character.addInventoryItem(i + 10, ItemFactory.instantiate().getItem(invItems.get(i)));
                }



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
        String equipItem = "bow,player-idle";
        String inventoryItem = "stick";

        processCharacterInsert(query, accountID, id, equipItem, inventoryItem);

        Character character = new Character();
        character.setAccountID(accountID);
        character.setId(id);
        List<String> equipItems = Arrays.asList(equipItem.split(","));
        List<String> invItems = Arrays.asList(inventoryItem.split(","));

        for (int i = 0; i < equipItems.size(); i++) {
            character.addEquipItem(i, ItemFactory.instantiate().getItem(equipItems.get(i)));
        }

        for (int i = 0; i < invItems.size(); i++) {
            character.addInventoryItem(i + 10, ItemFactory.instantiate().getItem(invItems.get(i)));
        }

        return character;
    }

    public static void saveCharacter(Character character) {

        if (character == null) return;

        // Create a list of strings for the items.
        ArrayList<String> equipItemList = new ArrayList<>();
        ArrayList<String> invItemList = new ArrayList<>();

        // Loop through the current items the player has and get the name of it. Add it to the list of strings.
        for (Map.Entry<Integer, Item> entry : character.getEquipItems().entrySet()) {
            if (entry.getValue() == null)
                equipItemList.add("");
            else
                equipItemList.add(entry.getValue().getItemTypeID());
        }

        for (Map.Entry<Integer, Item> entry : character.getInventoryItems().entrySet()) {
            if (entry.getValue() == null)
                equipItemList.add("");
            else
                invItemList.add(entry.getValue().getItemTypeID());
        }

        // Place it into a string. Ex: "stick,stick,stick"
        String equipItems = String.join(",", equipItemList);
        String inventoryItems = String.join(",", invItemList);

        // Create a sql query with the strings.
        String query = "UPDATE characters SET equipItems='" + equipItems +"', inventoryItems='" + inventoryItems +"' WHERE id='"+ character.getId() + "'";

        // Process the query
        processQueryUpdate(query);

        // Clear out the string lists.
        equipItemList.clear();
        invItemList.clear();
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


        processQueryUpdate(query);
    }

    public static void mutePlayer(String accName) {
        String query = "UPDATE users SET Muted=true Where username='" + accName + "'";

        processQueryUpdate(query);
    }

    public static void banPlayer(String accName) {
        String query = "UPDATE users SET Banned=true Where username='" + accName + "'";

        processQueryUpdate(query);
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

    private static void processQueryUpdate(String query) {
        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            int log = pst.executeUpdate();

            if (log > 0) {
                ConsoleLog.logSuccess("Query Update was successful for: " + query);
            } else {
                ConsoleLog.logError("Query Update failed for: " + query);
            }
        } catch (Exception e) {
            ConsoleLog.logError("Query failed: " + e);
        }
    }

    /**
     *
     * @param query insert
     * @param params params[0] = Account ID, params[1] = ID, params[2] = equipment items, params[3] = inventory items.
     *
     */
    private static void processCharacterInsert(String query, Object... params) {
        try (Connection con = DriverManager.getConnection(url, user, password); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, (Integer) params[0]);
            pst.setInt(2, (Integer) params[1]);
            pst.setString(3, (String) params[2]);
            pst.setString(4, (String) params[3]);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
