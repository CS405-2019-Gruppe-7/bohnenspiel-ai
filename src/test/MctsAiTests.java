import ai.AI;
import ai.MCTSAI;
import ai.RandomAI;
import game.GameState;
import org.junit.Ignore;
import org.junit.Test;

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
        ai.getMove(-1);
        GameState gs = new GameState();
        int result = ai.randomPlay(gs);
        System.out.println("Random play result:" + result);
    }

//    @Ignore
    @Test
    public void testVsRandomAI(){
        int[] wins = new int[2];
        System.out.println("Starting test run MCTS vs RandomAI");
        for(int i = 0; i < 10; i++){
            System.out.println("Round "+i+"/10");
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
    }
}
