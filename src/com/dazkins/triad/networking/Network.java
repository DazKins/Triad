package com.dazkins.triad.networking;

import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;
import com.dazkins.triad.networking.packet.Packet009EntityRemoved;
import com.dazkins.triad.networking.packet.Packet010PlayerNameSet;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.Packet015SingleLightValueChunkUpdate;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network
{
	public static void register(EndPoint endPoint)
	{
		Kryo k = endPoint.getKryo();
		
		k.register(Packet000RawMessage.class);
		k.register(Packet001LoginRequest.class);
		k.register(Packet002ChunkDataRequest.class);
		k.register(Packet003ChunkData.class);
		k.register(Packet004LoginRequestResponse.class);
		k.register(Packet005UpdatePlayerPosition.class);
		k.register(Packet006EntityPositionUpdate.class);
		k.register(Packet007EntityAnimationStart.class);
		k.register(Packet008CameraStateUpdate.class);
		k.register(Packet009EntityRemoved.class);
		k.register(Packet010PlayerNameSet.class);
		k.register(Packet011PlayerVelocity.class);
		k.register(Packet012Inventory.class);
		k.register(Packet013InteractCommand.class);
		k.register(Packet014InteractionUpdate.class);
		k.register(Packet015SingleLightValueChunkUpdate.class);

		k.register(String.class);
		k.register(int[].class);
		k.register(byte[].class);
	}
}