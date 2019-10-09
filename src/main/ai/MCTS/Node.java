package ai.MCTS;

import game.GameState;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private GameState state;
    private int visitCount = 0;
    private int winScore = 0;
    private Node parent;
    private List<Node> children = null;

    public Node(Node parent, GameState gs){
        this.state = gs;
        this.parent = parent;
    }

    public List<Node> getChildren() {
        // lazy initialization of children
        if(children == null){
            this.children = new ArrayList<>();
            this.state.getValidMoves().forEach(move -> {
                GameState gs = this.state.copy();
                gs.play(move);
                Node n = new Node(this, gs);
                this.children.add(n);
            });
        }
        return children;
    }

    public void setChildren(List<Node> children){
        this.children = children;
    }

    public GameState getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isLeaf(){
        return children == null;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getWinScore() {
        return winScore;
    }

    public void setWinScore(int winScore) {
        this.winScore = winScore;
    }
}
