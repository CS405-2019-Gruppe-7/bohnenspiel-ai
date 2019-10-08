package ai;

import ai.MCTS.Node;
import game.GameState;

import java.util.*;

public class MCTSAI extends AI {

    private Node rootNode;
    private double C = 1.5;
    private Random rng = new Random();
    private int playingAs = -1;

    public MCTSAI(){
        rootNode = new Node(null, new GameState());
    }

    @Override
    public String getName() {
        return "MCTSv1";
    }

    @Override
    public int getMove(int enemyIndex) {
        playingAs = playingAs == -1 && enemyIndex == -1 ? 0 : 1;
        if(enemyIndex != -1){
            try{
                rootNode = rootNode.getChildren().stream().filter(c -> c.getState().lastMove == enemyIndex).findFirst().get();
            }catch (Exception e){
                System.out.println("Invalid opponent move:");
                System.out.println("Opponent("+ (rootNode.getState().moveOfPlayer == 0 ? 1 : 0) +") move:" + enemyIndex);
                throw e;
            }
        }

        for(int i = 0; i < 10000; i++){
            // select leaf node by Upper Confidence Bound
            Node promisingNode = selectPromisingNode(rootNode);

            if(!promisingNode.getState().isGameOver()){
                promisingNode.getChildren();
            }
            Node nodeToExplore = promisingNode;
            if(nodeToExplore.getChildren().size() != 0){
                // select random child of the best UTC node
                nodeToExplore = promisingNode.getChildren().get(rng.nextInt(promisingNode.getChildren().size()));
            }
            int randomPlayResult = randomPlay(nodeToExplore.getState().copy());
            propagateBack(nodeToExplore, randomPlayResult);
        }
        rootNode = getMaxScoringChild(rootNode);
//        System.out.println("After MCTS move: ("+rootNode.getState().lastMove + ")");
//        System.out.println(rootNode.getState().toString());
        return rootNode.getState().lastMove;
    }

    private void propagateBack(Node exploredNode, int result){
        Node n = exploredNode;
        while(n != null){
            n.setVisitCount(n.getVisitCount() + 1);
            n.setWinScore(n.getWinScore() + result);
            n = n.getParent();
        }
    }

    private Node selectPromisingNode(Node from){
        Node n = from;
        while(!n.isLeaf() && n.getChildren().size() > 0){
            n = Collections.max(n.getChildren(), Comparator.comparing(this::getNodeUtc));
        }
        return n;
    }

    public int randomPlay(GameState gs){
        for(int i = 0; i < 1000; i++){
            if(gs.isGameOver()){
                int opponent = playingAs == 1 ? 0 : 1;
                // if this AI wins, return 1, else 0
                return gs.scores[playingAs] > gs.scores[opponent] ? 1 : 0;
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



    private Node getMaxScoringChild(Node n){
        if(n.isLeaf()){
            return n;
        }
        Node best = n.getChildren().get(0);
        for(Node child: n.getChildren()){
            if(
                    best.getWinScore() / (double)best.getVisitCount() <
                    child.getWinScore() / (double)child.getVisitCount()
            ){
                best = child;
            }
        }
//        System.out.println(String.format("The best node has score: %d/%d=%f", best.getWinScore(), best.getVisitCount(), best.getWinScore()/(double)best.getVisitCount()));
        return best;
    }

    private double getNodeUtc(Node n){
        if(n.getVisitCount() == 0){
            return Double.MAX_VALUE;
        }
        return n.getWinScore()/(double)n.getVisitCount() +
                this.C * Math.sqrt(
                        Math.log(n.getParent().getVisitCount()) /
                                (double) n.getVisitCount()
                );
    }
}
