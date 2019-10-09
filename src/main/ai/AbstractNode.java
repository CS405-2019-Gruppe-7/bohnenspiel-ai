package ai;

import game.GameState;

import java.util.ArrayList;
import java.util.List;

public class AbstractNode {
  private GameState state;
  private AbstractNode parent;
  private List<AbstractNode> children = null;

  public AbstractNode(AbstractNode parent, GameState gs){
    this.state = gs;
    this.parent = parent;
  }

  public List<AbstractNode> getChildren() {
    // lazy initialization of children
    if(children == null){
      this.children = new ArrayList<>();
      this.state.getValidMoves().forEach(move -> {
        GameState gs = this.state.copy();
        gs.play(move);
        AbstractNode n = new AbstractNode(this, gs);
        this.children.add(n);
      });
    }
    return children;
  }

  public void setChildren(List<AbstractNode> children){
    this.children = children;
  }

  public GameState getState() {
    return state;
  }

  public AbstractNode getParent() {
    return parent;
  }

  public void setParent(AbstractNode parent) {
    this.parent = parent;
  }

  public boolean isLeaf(){
    return children == null;
  }

}