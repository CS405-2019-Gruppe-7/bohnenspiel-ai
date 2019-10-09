import ai.AI;
import ai.MCTSAI;
import ai.RandomAI;
import game.GameState;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class MctsAiTests {
    @Ignore
    @Test
    public void testFirstMoveSelection(){
        MCTSAI ai = new MCTSAI();
        int move = ai.getMove(-1);
        System.out.println(move);
    }

    @Ignore
    @Test
    public void testRandomPlay(){
        MCTSAI ai = new MCTSAI();
        ai.setPlayingAs(0);
        GameState gs = new GameState();
        int result = ai.randomPlay(gs);
        System.out.println("Random play result:" + result);
    }

    @Test
    public void lostGame(){
        int playingAs = 1;
        MCTSAI ai = new MCTSAI();
        GameState gs = new GameState();
        gs.board = new byte[]{ 1, 1, 4, 1, 5, 18, 3, 0, 0, 0, 0, 1 };
        gs.scores[0] = 6;
        gs.scores[1] = 32;

        gs.moveOfPlayer = 0;
        gs.play(3);
        gs.play(12);

        ai.setPlayingAs(playingAs);

        int wins[] = new int[2];
        for(int i = 0; i < 10000; i++){
            int result = ai.randomPlay(gs.copy());
            wins[result]++;
        }
        System.out.println(String.format("%d/%d=%f", wins[playingAs], wins[0]+wins[1], wins[playingAs]/(double)(wins[1] + wins[0])));
        assertTrue(wins[0] > wins[1]);
    }

//    @Ignore
    @Test
    public void testVsRandomAI(){
        int[] wins = new int[2];
        System.out.println("Starting test run MCTS vs RandomAI");
        for(int i = 0; i < 100; i++){
            System.out.println("Round "+i+"/100");
            AI[] players = {new MCTSAI(), new RandomAI()};
            GameState gs = new GameState();
            int moveOf = 0;
            int opponentMove = -1;
            while(!gs.isGameOver()){
                int move = players[moveOf].getMove(opponentMove);
//                System.out.println(String.format("-> P%d move: %d\n", moveOf+1, move));
                opponentMove = move;
                gs.play(move);
//                System.out.println(gs.toString());
                moveOf = moveOf == 0 ? 1 : 0;
            }
            int winner = gs.scores[0] > gs.scores[1] ? 0 : 1;
            System.out.println(String.format("Player %d won!", winner));
            wins[winner]++;
        }
        System.out.println(String.format("MCTS vs RandomAI: %d:%d", wins[0], wins[1]));
        assertTrue(wins[0] > 10*wins[1]);
    }

//    @Ignore
    @Test
    public void testVsRandomAIRev(){
        int[] wins = new int[2];
        System.out.println("Starting test run RandomAI vs MCTS");
        for(int i = 0; i < 100; i++){
            System.out.println("Round "+i+"/100");
            AI[] players = {new RandomAI(), new MCTSAI()};
            GameState gs = new GameState();
            int moveOf = 0;
            int opponentMove = -1;
            while(!gs.isGameOver()){
                int move = players[moveOf].getMove(opponentMove);
                opponentMove = move;
                gs.play(move);
                moveOf = moveOf == 0 ? 1 : 0;
            }
            int winner = gs.scores[0] > gs.scores[1] ? 0 : 1;
            System.out.println(String.format("Player %d won!", winner));
            wins[winner]++;
        }
        System.out.println(String.format("RandomAI vs MCTS: %d:%d", wins[0], wins[1]));
    }

}
