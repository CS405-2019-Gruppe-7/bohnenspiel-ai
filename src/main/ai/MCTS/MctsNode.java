package ai.MCTS;

import game.GameState;

import java.util.ArrayList;
import java.util.List;

public class MctsNode {
    private GameState state;
    private int visitCount = 0;
    private int winScore = 0;
    private MctsNode parent;
    private List<MctsNode> children = null;

    public MctsNode(MctsNode parent, GameState gs){
        this.state = gs;
        this.parent = parent;
    }

    public List<MctsNode> getChildren() {
        // lazy initialization of children
        if(children == null){
            this.children = new ArrayList<>();
            this.state.getValidMoves().forEach(move -> {
                GameState gs = this.state.copy();
                gs.play(move);
                MctsNode n = new MctsNode(this, gs);
                this.children.add(n);
            });
        }
        return children;
    }

    public void setChildren(List<MctsNode> children){
        this.children = children;
    }

    public GameState getState() {
        return state;
    }

    public MctsNode getParent() {
        return parent;
    }

    public void setParent(MctsNode parent) {
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
