package com.dazkins.triad.networking.packet;

public class Packet004LoginRequestResponse extends Packet {
	private boolean accepted;
	
	private int playerID;

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public int getChosenPlayerID() {
		return playerID;
	}

	public void setPlayerID(int chosenPlayerID) {
		this.playerID = chosenPlayerID;
	}
}