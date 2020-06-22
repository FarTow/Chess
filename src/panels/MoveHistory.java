package panels;

import entities.Board;
import entities.Pawn;
import entities.Piece;
import entities.Square;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class MoveHistory extends JPanel implements ActionListener {
    private final Board board;
    private final Object[] headers = new Object[] {"Turn", "White", "Black"};

    private final DefaultTableModel moveDisplayModel;
    private final ArrayList<Object[]> allMoveData;

    private boolean whiteTurn;
    private int moveCount;

    public MoveHistory(Board board) {
        setBackground(new Color(194, 194, 194));
        this.board = board;
        whiteTurn = true;
        moveCount = 1;
        allMoveData = new ArrayList<>();

        allMoveData.add(new Object[] {moveCount, "", ""});

        moveDisplayModel = new DefaultTableModel(readableMoveData(), headers) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable moveDisplay = new JTable(moveDisplayModel);
        moveDisplay.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        moveDisplay.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(moveDisplay));
    }

    public Object[][] readableMoveData() {
        Object[][] readableMoveData = new Object[allMoveData.size()][allMoveData.get(0).length];

        for (int i=0; i<allMoveData.size(); i++) {
            Object[] moveDataRow = new Object[readableMoveData[0].length];
            System.arraycopy(allMoveData.get(i), 0, moveDataRow, 0, allMoveData.get(i).length);
            readableMoveData[i] = moveDataRow;
        }

        return readableMoveData;
    }

    public String lastMove(Point oldSquare, Point newSquare, Piece piece, boolean tookPiece) {
        StringBuilder chessNotation = new StringBuilder();
        chessNotation.append(piece.getSymbol());

        if (tookPiece) {
            if (piece instanceof Pawn) chessNotation.append((char) ((char) 97 + oldSquare.y));
            chessNotation.append("×");
        }

        if (!(piece instanceof Pawn)) {
            for (Square[] squareRow : board.getGrid()) {
                for (Square square : squareRow) {
                    if (square.getPiece() != null) { // if the piece isn't null
                        if (square.getPiece().getClass().equals(piece.getClass()) && square.getPiece() != piece) { // if the piece is the "other" piece

                            if (square.getPiece().canMove(newSquare.x, newSquare.y, board, false)) { // if the piece can move to the new square
                                if (square.getPiece().getColumn() == oldSquare.y) { // if they're on the same file
                                    chessNotation.append(4 + (4 - oldSquare.x)); // show the rank of the moved piece
                                } else { // otherwise
                                    chessNotation.append((char) ((char) 97 + oldSquare.y)); // show the file of the moved
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }

        chessNotation.append((char) ((char) 97 + newSquare.y)); // file
        chessNotation.append(4 + (4 - newSquare.x)); // rank

        return chessNotation.toString();
    }

    public void updateAllMoveData() {
        if (whiteTurn != board.getWhiteTurn()) {
            if (board.getWhiteTurn()) {
                allMoveData.get(moveCount-1)[2] = lastMove(board.getOldSquare(), board.getNewSquare(), board.getLastPiece(), board.getTookPiece());

                moveCount++;
                allMoveData.add(new Object[] {moveCount, "", ""});
            } else {
                allMoveData.get(moveCount-1)[1] = lastMove(board.getOldSquare(),board.getNewSquare(), board.getLastPiece(), board.getTookPiece());
            }

            moveDisplayModel.setDataVector(readableMoveData(), headers);
            whiteTurn = board.getWhiteTurn();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
        updateAllMoveData();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
