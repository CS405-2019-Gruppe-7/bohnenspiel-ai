package local;

import ai.AI;
import ai.RandomAI;
import game.GameState;

public class Tester {
    public static void main(String[] args){
        AI[] players = {new RandomAI(), new RandomAI()};
        GameState gs = new GameState();
        int moveOf = 0;
        int opponentMove = -1;
        while(!gs.isGameOver()){
            int move = players[moveOf].getMove(opponentMove);
            System.out.println(String.format("P%d move: %d\n", moveOf+1, move));
            opponentMove = move;
            gs.play(move);
            System.out.println(gs.toString());
        }
        int winner = gs.scores[0] > gs.scores[1] ? 1 : 2;
        System.out.println(String.format("Player %d won!", winner));
    }
}