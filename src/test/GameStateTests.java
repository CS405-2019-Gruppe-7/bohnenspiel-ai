import game.GameState;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GameStateTests{

    @Test
    public void prettyStringTest(){
        GameState gs = new GameState();
        assertEquals(
                "===========================\n" +
                "p1 |  6  6  6  6  6  6 | p2\n" +
                " 0 |  6  6  6  6  6  6 |  0\n" +
                "===========================",
                gs.toString());
    }

    @Test
    public void validMoveTest(){
        GameState gs = new GameState();
        gs.play(0);
        gs.play(11);
        assertArrayEquals(new byte[]{ 1, 8, 8, 8, 8, 8, 7, 6, 6, 6, 6, 0 }, gs.board);
    }

    @Test
    public void pointCollection(){
        GameState gs = new GameState();
        gs.board = new byte[] { 5, 3, 3, 5, 3, 3, 7, 6, 6, 6, 6, 0 };
        gs.play(0);
        assertEquals(22, gs.scores[0]);
    }

    @Test
    public void crossTeamPointCollection(){
        GameState gs = new GameState();
        gs.board = new byte[] { 5, 3, 3, 5, 3, 3, 5, 3, 3, 5, 3, 3 };
        gs.play(0);
        gs.play(6);
        assertEquals(22, gs.scores[0]);
        assertEquals(22, gs.scores[1]);
    }

    @Test
    public void crossTeamBoundaryPointCollection(){
        GameState gs = new GameState();
        gs.board = new byte[] { 3, 3, 1, 1, 3, 3, 5, 5, 3, 5, 3, 5 };
        gs.play(2);
        gs.play(10);
        assertEquals(2, gs.scores[0]);
        assertEquals(14, gs.scores[1]);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void invalidMoveDetection(){
        expectedException.expect(RuntimeException.class);
        GameState gs = new GameState();
        gs.play(0);
        gs.play(1);
    }

    @Test
    public void invalidMoveDetectionP2(){
        expectedException.expect(RuntimeException.class);
        GameState gs = new GameState();
        gs.play(0);
        gs.play(10);
        gs.play(10);
    }

    @Test
    public void correctGameOverCollection(){
        GameState gs = new GameState();
        gs.board = new byte[]{ 0, 0, 0, 0, 0, 1, 5, 2, 5, 5, 5, 5 };
        gs.play(6);
        gs.play(8);
        System.out.println(gs.toString());
        assertEquals(6, gs.scores[0]);
        assertEquals(22, gs.scores[1]);
        assertEquals(1, gs.getWinner());
    }

}