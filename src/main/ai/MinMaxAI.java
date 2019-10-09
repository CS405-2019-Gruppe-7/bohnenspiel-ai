package ai;

import ai.MinMax.MinMaxNode;
import game.GameState;

public class MinMaxAI extends AI
{
    private GameState state = new GameState();
    private int playingAs = -1;
    private MinMaxNode rootNode = new MinMaxNode(null, state);

    @Override
    public String getName() {
        return "MinMaxv1";
    }

    @Override
    public int getMove(int enemyIndex) {
        if(playingAs == -1 && enemyIndex == -1){
            playingAs = 0;
        }else if(playingAs == -1){
            playingAs = 1;
        }

        

        return 0;
    }
}
