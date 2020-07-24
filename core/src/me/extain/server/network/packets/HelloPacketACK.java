package me.extain.server.network.packets;

public class HelloPacketACK extends Packet {

    private String message;

    public HelloPacketACK() {
        this.setPacketID(2);
       // this.addPacket("HelloPacketACK", this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
