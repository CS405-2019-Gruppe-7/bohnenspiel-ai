package local;

import ai.AI;
import ai.MCTSAI;
import ai.RandomAI;
import game.GameState;

public class Tester {
    public static void main(String[] args){
        AI[] players = {new MCTSAI(), new RandomAI()};
        GameState gs = new GameState();
        int moveOf = 0;
        int opponentMove = -1;
        while(!gs.isGameOver()){
            int move = players[moveOf].getMove(opponentMove);
            System.out.println(String.format("-> P%d move: %d\n", moveOf+1, move));
            opponentMove = move;
            gs.play(move);
            System.out.println(gs.toString());
            moveOf = moveOf == 0 ? 1 : 0;
        }
        int winner = gs.scores[0] > gs.scores[1] ? 1 : 2;
        System.out.println(String.format("Player %d won!", winner));
    }
}