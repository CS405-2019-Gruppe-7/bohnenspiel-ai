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

    private int minimax(int depth, int nodeIndex,
                        Boolean maximizingPlayer,
                        int values[], int alpha,
                        int beta) {
        // Terminating condition. i.e
        // leaf node is reached
        if (depth == 3)
            return values[nodeIndex];

        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;

            // Recur for left and
            // right children
            for (int i = 0; i < 2; i++) {
                int val = minimax(depth + 1, nodeIndex * 2 + i,
                        false, values, alpha, beta);
                best = Math.max(best, val);
                alpha = Math.max(alpha, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            // Recur for left and
            // right children
            for (int i = 0; i < 2; i++) {

                int val = minimax(depth + 1, nodeIndex * 2 + i,
                        true, values, alpha, beta);
                best = Math.min(best, val);
                beta = Math.min(beta, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            return best;
        }
    }
}
