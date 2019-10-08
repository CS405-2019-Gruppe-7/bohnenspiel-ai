package ai;

import game.GameState;

import java.util.Random;

public class RandomAI extends AI {
	GameState gs = new GameState();
	Random rand = new Random();
	
	public int getMove(int enemyIndex) {
		if(enemyIndex != -1){
			gs.play(enemyIndex);
		}
		int[] possibleMoves = gs.getValidMoves().toArray();
		int move = possibleMoves[rand.nextInt(possibleMoves.length)];
		this.gs.play(move);
		return move;
	}

	@Override
	public String getName() {
		return "RANDOM";
	}
	
}
