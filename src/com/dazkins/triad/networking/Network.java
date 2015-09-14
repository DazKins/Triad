package com.dazkins.triad.networking;

import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.networking.client.ChunkData;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
	public static void register (EndPoint endPoint) {
		Kryo k = endPoint.getKryo();
		k.register(Packet000RawMessage.class);
		k.register(Packet001LoginRequest.class);
		k.register(Packet002ChunkDataRequest.class);
		k.register(Packet003ChunkData.class);
		k.register(Packet004LoginRequestResponse.class);
		k.register(Packet005UpdatePlayerPosition.class);
		k.register(Packet006EntityPositionUpdate.class);
		
		
		k.register(Color.class);
		k.register(Color[].class);
		k.register(ChunkCoordinate.class);
		k.register(ChunkData.class);
		k.register(byte[].class);
	}
}