package me.extain.server.network.packets;

public class Packet {

    //public HashMap<String, Packet> packets = new HashMap<>();
    public String packetName;
    private int packetID = 0;


    /*public void addPacket(String packetName, Packet packet) {
        packets.put(packetName, packet);
    }

    public Packet getPacket(String packetName) {
        return packets.get(packetName);
    } */

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public String getPacketName() {
        return packetName;
    }

    public void setPacketID(int id) {
        this.packetID = id;
    }
}
