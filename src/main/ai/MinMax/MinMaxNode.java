package ai.MinMax;

import game.GameState;

import java.util.ArrayList;
import java.util.List;

public class MinMaxNode {
    private GameState state;
    private int value = 0;
    private MinMaxNode parent;
    private List<MinMaxNode> children = null;

    public MinMaxNode(MinMaxNode parent, GameState gs){
        this.state = gs;
        this.parent = parent;
    }

    public List<MinMaxNode> getChildren() {
        // lazy initialization of children
        if(children == null){
            this.children = new ArrayList<>();
            this.state.getValidMoves().forEach(move -> {
                GameState gs = this.state.copy();
                gs.play(move);
                MinMaxNode n = new MinMaxNode(this, gs);
                this.children.add(n);
            });
        }
        return children;
    }

    public void setChildren(List<MinMaxNode> children){
        this.children = children;
    }

    public GameState getState() {
        return state;
    }

    public MinMaxNode getParent() {
        return parent;
    }

    public void setParent(MinMaxNode parent) {
        this.parent = parent;
    }

    public boolean isLeaf(){
        return children == null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
