package game;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameState {
    public int[] scores = { 0, 0 };
    public byte[] board = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
    public int moveOfPlayer = 0;

    private boolean over = false;

    public void play(int field){
        field = field - 1;
        if(!validMove(field)){
            throw new RuntimeException("Move to field: " + field + " is not valid for player " + moveOfPlayer);
        }
        int startField = field;
        int value = board[field];
        board[field] = 0;
        while(value > 0) {
            field = (++field) % 12;
            board[field]++;
            value--;
        }

        if(board[field] == 2 || board[field] == 4 || board[field] == 6) {
            do {
                scores[startField/6] += board[field];
                board[field] = 0;
                field = (field == 0) ? field = 11 : --field;
            } while(board[field] == 2 || board[field] == 4 || board[field] == 6);
        }
        moveOfPlayer = moveOfPlayer == 0 ? 1 : 0;
        if(isGameOver()){
            over = true;
            int winner = moveOfPlayer == 0 ? 1 : 0;
            for(int i = 0; i < 6; i++){
                scores[winner] += this.board[i+6*winner];
            }
        }
    }

    private boolean validMove(int field){
        return field >= 6*moveOfPlayer && field <= 6*moveOfPlayer + 6 && this.board[field] != 0;
    }

    public GameState copy(){
        GameState gs = new GameState();
        gs.board = this.board.clone();
        gs.scores = this.scores.clone();
        gs.moveOfPlayer = this.moveOfPlayer;
        return gs;
    }

    public boolean isGameOver(){
        if(this.over) return true;
        boolean canMove = false;
        for(int i = 0; i < 6; i++){
            canMove |= this.board[i+6*moveOfPlayer] > 0;
        }
        return !canMove;
    }

    @Override
    public String toString(){
        String fmt = "%2d";
        StringBuilder sb = new StringBuilder();
        String header = IntStream.range(0,27).mapToObj(s -> "=").collect(Collectors.joining(""));
        sb.append(header);
        sb.append("\n");
        sb.append("p1 | ");
        for(int i = 11; i >= 6; i--){
            sb.append(String.format(fmt, this.board[i]));
            sb.append(" ");
        }
        sb.append("| p2\n");
        sb.append(String.format(fmt, this.scores[0]));
        sb.append(" | ");
        for(int i = 0; i < 6; i++){
            sb.append(String.format(fmt, this.board[i]));
            sb.append(" ");
        }
        sb.append("| ");
        sb.append(String.format(fmt, this.scores[1]));
        sb.append("\n");
        sb.append(header);
        return sb.toString();
    }
}