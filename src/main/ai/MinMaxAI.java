package ai;

import ai.MinMax.MinMaxNode;
import game.GameState;

public class MinMaxAI extends AI
{
    private GameState state = new GameState();
    private int playingAs = -1;
    private MinMaxNode rootNode = new MinMaxNode(null, state);
    private int maxDepth = 8;
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
        if(enemyIndex != -1){
            try{
                rootNode = rootNode.getChildren().stream().filter(c -> c.getState().lastMove == enemyIndex).findFirst().get();
            }catch (Exception e){
                System.out.println("Invalid opponent move:");
                System.out.println("Opponent("+ (rootNode.getState().moveOfPlayer == 0 ? 1 : 0) +") move:" + enemyIndex);
                throw e;
            }
        }
        int maxHeuristic = minimax(0, rootNode, playingAs == this.state.moveOfPlayer, Integer.MIN_VALUE,Integer.MAX_VALUE);
        MinMaxNode nextMove = rootNode.getChildren()
                .stream()
                .filter(
                        c -> c.getValue() == maxHeuristic
                ).findFirst().get();
        rootNode = nextMove;
        return nextMove.getState().lastMove;
    }

    private int minimax(int depth, MinMaxNode node,
                        Boolean maximizingPlayer,
                        int alpha, int beta) {
        // Terminating condition. i.e
        // leaf node is reached
        if (depth == this.maxDepth)
            // get the node heuristic here
            return node.getState().getHeuristicValue();

        if (maximizingPlayer) {
            int best = Integer.MIN_VALUE;
            // Recur for left and
            // right children
            for (MinMaxNode child: node.getChildren()) {
                int val = minimax(depth + 1, child,
                        false, alpha, beta);
                best = Math.max(best, val);
                child.setValue(val);
                alpha = Math.max(alpha, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            node.setValue(best);
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            // Recur for left and
            // right children
            for (MinMaxNode child: node.getChildren()) {

                int val = minimax(depth + 1, child,
                        true, alpha, beta);
                best = Math.min(best, val);
                child.setValue(val);
                beta = Math.min(beta, best);

                // Alpha Beta Pruning
                if (beta <= alpha)
                    break;
            }
            node.setValue(best);
            return best;
        }
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
