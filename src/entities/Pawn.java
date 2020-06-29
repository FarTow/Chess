package entities;

import panels.Board;

import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {
    private boolean movedTwo, enPassantCapturable, promotable;

    public Pawn(boolean isWhite, Square square) {
        super(isWhite, square);
        setImage("pawn");
    }

    public boolean canMove(int newRow, int newColumn, Board board, boolean mouseReleased) {
        return moveableSquares.contains(board.getGrid()[newRow][newColumn]);
    }

    public void update(Board board) {
        moveableSquares = new ArrayList<>();
        Square[][] grid = board.getGrid();

        for (Square[] squareRow : grid) {
            for (Square square : squareRow) {
                int newRow = square.getRow();
                int newColumn = square.getColumn();
                int movementModifier = isWhite ? -1 : 1;

                if (!isJumping(newRow, newColumn, grid) && !(Math.abs(getColumn()-newColumn) > 1)) {
                    if (square.getPiece() == null) { // moving to a spot with no piece
                        if (getColumn() == newColumn) { // moving to a spot in the same column
                            if (firstMove) { // move two on first turn
                                if (getRow() + movementModifier == newRow || getRow() + movementModifier * 2 == newRow) moveableSquares.add(square);
                            } else {
                                if (getRow() + movementModifier == newRow) moveableSquares.add(square);
                            }
                        } else { // only other case is en passant when moving to a spot with no piece
                            if (Math.abs(getRow() - newRow) == 1) {
                                if ((newRow + movementModifier * -1 >= 0 && newRow + movementModifier * -1 <= 7)) {
                                    Square pawnSquare = grid[newRow + movementModifier * -1][newColumn];

                                    if (pawnSquare.getPiece() instanceof Pawn) {
                                        if (((Pawn) pawnSquare.getPiece()).isEnPassantCapturable()) {
                                            moveableSquares.add(square);
                                        }
                                    }
                                }
                            }
                        }
                    } else { // moving to a spot with a piece
                        if (getColumn() != newColumn) { // moving to a spot on a different column
                            if (isWhite != square.getPiece().isWhite()) { // the piece is a different color
                                if (getRow() - newRow == movementModifier * -1) moveableSquares.add(square);
                            }
                        }
                    }
                }

            }
        }

    }

    public boolean isEnPassantCapturable() { return enPassantCapturable; }
    public boolean isPromotable() { return promotable; }
    public char getSymbol() { return isWhite ? '♙' : '♟'; }
}
