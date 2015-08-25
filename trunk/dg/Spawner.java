package com.tfu.dg;

import com.tfu.dg.framework.DynamicGameObject;

public class Spawner extends DynamicGameObject {
	private float frequency;
	private int roundSpawns;
	private int rounds;
	private int sentObjects;
	private int currentRound;
	private float stateTime;
	private int state;
	private float waitTime;
	private float waitBetweenRounds;
	boolean finished;
	private boolean sendObject;
	
	static int STATE_PRE_START = 0;
	static int STATE_RUNNING = 1;
	static int STATE_ROUND_ENDED = 2;
	static int STATE_FINISHED = 3;
	
	public Spawner (int x, int y) {
		super(x,y,0,0);
		this.frequency = 1.5f;
		this.roundSpawns = 10;
		this.rounds = 5;
		this.stateTime = 0;
		this.sentObjects = 0;
		this.currentRound = 0;
		this.waitTime = 5;
		this.state = STATE_PRE_START;
		this.waitBetweenRounds = 5;
		this.finished = false;
		this.sendObject = false;
	}
	
	public boolean sendObject() {
		return sendObject;
	}
	
	public void objectSent() {
		this.sendObject = false;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		
		if (state == STATE_PRE_START) {
			// waiting to start
			if (stateTime > waitTime) {
				// finished waiting, start the round
				stateTime = 0;
				state = STATE_RUNNING;
			}
		}
		if (state == STATE_RUNNING) {
			if (stateTime >= this.frequency) {
				if (sentObjects < roundSpawns) {
					sendObject = true;
					sentObjects++;
					stateTime = 0;
				} else {
					// last object has been sent
					if (currentRound == rounds) {
						// last object of the last round was sent
						state = STATE_FINISHED;
						stateTime = 0;
					} else {
						// last object of a non final round was sent
						state = STATE_ROUND_ENDED;
						stateTime = 0;
						currentRound++;
					}
				}
			}
		}
		if (state == STATE_ROUND_ENDED) {
			if (stateTime > waitBetweenRounds) {
				sentObjects = 0;
				state = STATE_RUNNING;
				stateTime = 0;
			}
		}
		if (state == STATE_FINISHED) {
			finished = true;
		}
	}
}
