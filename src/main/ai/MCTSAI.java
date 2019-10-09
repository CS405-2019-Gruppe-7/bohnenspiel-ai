package ai;

import ai.MCTS.MctsNode;
import game.GameState;

import java.util.*;

public class MCTSAI extends AI {

    private MctsNode rootNode;
    private double C = 1.5;
    private Random rng = new Random();
    private int playingAs = -1;

    public MCTSAI(){
        rootNode = new MctsNode(null, new GameState());
    }

    @Override
    public String getName() {
        return "MCTSv1";
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
        System.out.println(this.rootNode.getState());

        for(int i = 0; i < 10000; i++){
            // select leaf node by Upper Confidence Bound
            MctsNode promisingNode = selectPromisingNode(rootNode);

            if(!promisingNode.getState().isGameOver()){
                promisingNode.getChildren();
            }
            MctsNode nodeToExplore = promisingNode;
            if(nodeToExplore.getChildren().size() != 0){
                // select random child of the best UTC node
                nodeToExplore = promisingNode.getChildren().get(rng.nextInt(promisingNode.getChildren().size()));
            }
            int randomPlayResult = randomPlay(nodeToExplore.getState().copy());
            int backPropValue = randomPlayResult == playingAs ? 1 : 0;
            propagateBack(nodeToExplore, backPropValue);
        }
        rootNode = getMaxScoringChild(rootNode);
//        System.out.println("After MCTS move: ("+rootNode.getState().lastMove + ")");
//        System.out.println(rootNode.getState().toString());
        System.out.println(this.rootNode.getState());
        return rootNode.getState().lastMove;
    }

    private void propagateBack(MctsNode exploredNode, int result){
        MctsNode n = exploredNode;
        while(n != null){
            n.setVisitCount(n.getVisitCount() + 1);
            n.setWinScore(n.getWinScore() + result);
            n = n.getParent();
        }
    }

    private MctsNode selectPromisingNode(MctsNode from){
        MctsNode n = from;
        while(!n.isLeaf() && n.getChildren().size() > 0){
            n = Collections.max(n.getChildren(), Comparator.comparing(this::getNodeUtc));
        }
        return n;
    }

    public int randomPlay(GameState gs){
        for(int i = 0; i < 1000; i++){
            if(gs.isGameOver()){
                return gs.getWinner();
            }

            int move = rng.nextInt(12) + 1;
            while(!gs.validMove(move)){
                move = rng.nextInt(12) + 1;
            }
            gs.play(move);
        }
        // in case the end of the game is not reached in a 1000 moves
        System.out.println("That is one long game:");
        System.out.println(gs.toString());
        return 0;
    }



    private MctsNode getMaxScoringChild(MctsNode n){
        if(n.isLeaf()){
            return n;
        }
        MctsNode best = n.getChildren().get(0);
        for(MctsNode child: n.getChildren()){
            if(
                    best.getWinScore() / (double)best.getVisitCount() <
                    child.getWinScore() / (double)child.getVisitCount()
            ){
                best = child;
            }
        }
        System.out.println(String.format(
                "The best node has score: %d/%d=%f for p%d",
                best.getWinScore(),
                best.getVisitCount(),
                best.getWinScore()/(double)best.getVisitCount(),
                playingAs + 1
                ));
        System.out.println(best.getState().toString());
        return best;
    }

    private double getNodeUtc(MctsNode n){
        if(n.getVisitCount() == 0){
            return Double.MAX_VALUE;
        }
        return n.getWinScore()/(double)n.getVisitCount() +
                this.C * Math.sqrt(
                        Math.log(n.getParent().getVisitCount()) /
                                (double) n.getVisitCount()
                );
    }

    public void setPlayingAs(int playingAs){
        this.playingAs = playingAs;
    }
}
