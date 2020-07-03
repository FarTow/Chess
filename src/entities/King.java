package entities;

import panels.Board;
import java.util.ArrayList;

public class King extends Piece {
    private int castled; // 0 -> not castled, 1 -> castled king's side, 2 -> castled queen's side

    public King(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("king");
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();
                int rowDiff = Math.abs(getRow() - newRow);
                int columnDiff = Math.abs(getColumn() - newColumn);

                if (isJumping(newRow, newColumn, grid)) continue;
                if (rowDiff >= 2 || columnDiff >= 3) continue;

                if (columnDiff == 2 && rowDiff == 0) { // if player moves king two spaces horizontally
                    if (firstMove) { // if it's the king's first move
                        Piece rook = getColumn() - newColumn < 0 ? grid[getRow()][7].getPiece() : grid[getRow()][0].getPiece(); // rook on the side player moved

                        if (rook instanceof Rook) { // piece is a rook
                            if (rook.isFirstMove() && rook.isWhite() == isWhite && castled == 0) { // rook's first move
                                moveableSquares.add(square);
                            }
                        }
                    }
                } else {
                    if (rowDiff + columnDiff <= 2 && columnDiff <= 1) {
                        moveableSquares.add(square);
                    }
                }

            }
        }

        if(isWhite == board.getWhiteTurn()) {
            if (castled>0) castled = 0;
        }
    }

    /*
    public boolean castleable(Rook rook, Board board) {
        if (castled == 3) return false;

        Square[][] grid = board.getGrid();
        int dangerSquares = 0;
        boolean castleLeftRook = rook.getColumn() > getColumn();

        // for all the spaces between the king and rook
        for (int currentColumn=getColumn()+(castleLeftRook ? 1 : -1); // start at rook
             (castleLeftRook ? currentColumn<rook.getColumn() : currentColumn>rook.getColumn()); // condition based on if castling king or queens side
             currentColumn += (castleLeftRook ? 1 : -1)) { // increment based on kings or queens side

            //if (!board.mayMove(this, grid[getRow()][currentColumn])) dangerSquares++;
        }

        return dangerSquares == 0;
    }

     */

    public void setCastled(int castled) { this.castled = castled; }

    public int getCastled() { return castled; }
    public char getSymbol() { return isWhite ? '♔' : '♚'; }
}
