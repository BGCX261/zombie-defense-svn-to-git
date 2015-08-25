package com.tfu.dg.framework;

public class Trixel {
	int[][][] v;
	public static int T_SIZE = 15;
	
	public Trixel () {
		v = new int[T_SIZE][T_SIZE][T_SIZE];
		for (int x = 0; x < T_SIZE; x++) {
			for (int y = 0; y < T_SIZE; y++) {
				for (int z = 0; z < T_SIZE; z++) {
					v[x][y][z] = 0; 
				}
			}
		}
	}
	
	public void loadType(int type) {
		switch(type) {
		case 0:
			loadNeg();
			break;
		case 1:
			loadPos();
			break;
		}
	}

	private void loadPos() {
		
	}

	private void loadNeg() {
		
	}
}
