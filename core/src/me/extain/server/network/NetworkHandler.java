package me.extain.server.network;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Scaling;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.HashMap;

import me.extain.server.ServerPlayer;
import me.extain.server.item.Item;
import me.extain.server.item.WeaponStats;
import me.extain.server.packets.HelloPacket;
import me.extain.server.packets.HelloPacketACK;
import me.extain.server.packets.InventoryUpdatePacket;
import me.extain.server.packets.JoinPacket;
import me.extain.server.packets.LoginSuccessPacket;
import me.extain.server.packets.LoginUserPacket;
import me.extain.server.packets.LootDropPacket;
import me.extain.server.packets.MessagePacket;
import me.extain.server.packets.MovePacket;
import me.extain.server.packets.MovePacketACK;
import me.extain.server.packets.NewCharacterAckPacket;
import me.extain.server.packets.NewCharacterPacket;
import me.extain.server.packets.NewPlayerPacket;
import me.extain.server.packets.Packet;
import me.extain.server.packets.PlayerDisconnected;
import me.extain.server.packets.RequestObjects;
import me.extain.server.packets.SendObjectsPacket;
import me.extain.server.packets.ShootPacket;
import me.extain.server.packets.UpdatePacket;
import me.extain.server.objects.Player.Character;

public class NetworkHandler {

    static public final int port = 5045;

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Packet.class);
        kryo.register(HelloPacket.class);
        kryo.register(HelloPacketACK.class);
        kryo.register(NewPlayerPacket.class);
        kryo.register(SendObjectsPacket.class);
        kryo.register(RequestObjects.class);
        kryo.register(UpdatePacket.class);
        kryo.register(ShootPacket.class);
        kryo.register(ServerPlayer.class);
        kryo.register(JoinPacket.class);
        kryo.register(MovePacket.class);
        kryo.register(MovePacketACK.class);
        kryo.register(PlayerDisconnected.class);
        kryo.register(MessagePacket.class);
        kryo.register(ArrayList.class);
        kryo.register(LootDropPacket.class);
        kryo.register(LoginUserPacket.class);
        kryo.register(LoginSuccessPacket.class);
        kryo.register(Character.class);
        kryo.register(ArrayList.class);
        kryo.register(Item.class);
        kryo.register(NewCharacterPacket.class);
        kryo.register(NewCharacterAckPacket.class);
        kryo.register(Array.class);
        kryo.register(Object[].class);
        kryo.register(InventoryUpdatePacket.class);
        kryo.register(HashMap.class);
        kryo.register(DelayedRemovalArray.class);
        kryo.register(IntArray.class);
        kryo.register(int[].class);
        kryo.register(Color.class);
        kryo.register(Scaling.class);
        kryo.register(Touchable.class);
        kryo.register(WeaponStats.class);
    }

}
