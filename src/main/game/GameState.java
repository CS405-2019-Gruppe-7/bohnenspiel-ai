package game;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameState {
    public int[] scores = { 0, 0 };
    public byte[] board = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 };
    public int moveOfPlayer = 0;
    public int lastMove = -1;
    private boolean over = false;
    private static int getHeuristik(int[] state,int[] score){
        int points=0;
        boolean p1=false;
        boolean p2=false;
        for(int i=0;i<=5;i++){
            if(state[i]>0){
                points++;
                p1=true;
            }
            if(state[6+i]>0){
                points--;
                p2=true;
            }
        }
        if(!p1){
            points=0;
            for(int i=0;i<=11;i++){
                points-=state[i];
            }
            if(points+score[1]>score[0]){
                points=-100;
            }
        }
        if(!p2){
            points=0;
            for(int i=0;i<=11;i++){
                points+=state[i];
            }
            if(points+score[0]>state[1]){
                points=100;
            }
        }
        return score[0]-score[1]+points;
    }
    public void play(int field){
        if(!validMove(field)){
            System.out.println("Move not possible:");
            System.out.println(this.toString());
            throw new RuntimeException("Move to field: " + field + " is not valid for player " + moveOfPlayer);
        }
        lastMove = field;
        field = field - 1;
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
                scores[moveOfPlayer] += board[field];
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
                this.board[i+6*winner] = 0;
            }
        }
    }

    // is 1 <= field <= 12 && field isnt empty
    public boolean validMove(int field){
        return field > 6*moveOfPlayer && field <= 6*moveOfPlayer + 6 && this.board[field - 1] != 0;
    }

    public int getWinner(){
        return this.scores[0] > this.scores[1] ? 0 : 1;
    }

    public IntStream getValidMoves(){
        return IntStream.range(1, 13).filter(this::validMove);
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