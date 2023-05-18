package org.ssl.view;

import org.ssl.controller.ChessGame;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.ssl.model.*;
import org.ssl.model.figure.Color;
import org.ssl.model.figure.Figure;
import org.ssl.model.figure.King;
import org.ssl.model.figure.Knight;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ChessBoard extends GridPane {

    private ChessField[] fields = new ChessField[64];
    private Map<Color, Set<ChessField>> attackedFields = new HashMap<>();
    private int currentTurn = 1;
    private int ruleOf50 = 0;
    private String username;
    private boolean gameState = false;
    private GameInfo gameInfo;
    private ChessIO chessIO;

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public ChessBoard() {
        //resetAttackedFields();
        for (int i = 0; i < 64; i++) {
            int x = getX(i);
            int y = getY(i);
            ChessField field = new ChessField(this, x, y);
            add(field, x, y);
            fields[i] = field;
        }
        //recalculateAttackedFields();
    }

    private void resetAttackedFields() {
        attackedFields.put(Color.BLACK, new HashSet<>());
        attackedFields.put(Color.WHITE, new HashSet<>());
    }

    private int getX(int index) {
        return index % 8;
    }

    private int getY(int index) {
        return (index - getX(index)) / 8;
    }

    public ChessField getField(int x, int y) {
        return x < 0 || x > 7 || y < 0 || y > 7 ? null : fields[y * 8 + x];
    }

    public void setFigure(Figure figure) {
        getField(figure.getX(), figure.getY()).setFigure(figure, true);
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrenTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int get50MoveRuleTurns() {
        return ruleOf50;
    }

    public void set50MoveRuleTurns(int turns) {
        this.ruleOf50 = turns;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGameStartInfo(){
        gameInfo = new GameInfo(this.username,dateTimeFormatter.format(LocalDateTime.now()));
    }

    public void nextTurn() {
        currentTurn++;
        ruleOf50++;
        //recalculateAttackedFields();
        gameStateTest();
    }

    public Color getTurn() {
        return currentTurn % 2 == 0 ? Color.BLACK : Color.WHITE;
    }

    void gameStateTest() {

        for(ChessField field : fields) {
            if((field.getFigure() != null) &&
                    (field.getX() == 6) && (field.getY() == 7)) {
                this.gameState = true;
                saveGameInfo();
                ChessGame.displayStatusText("Congratulations, You win.");

                return;
            }
        }
/*
        King king = getKing();
        if (king != null) {
            if (king.isCheck()) {
                if (king.isCheckMate()) {
                    ChessGame.displayStatusText("Check mate! " + king.getColor().revert().getFancyName() + " wins.");
                    return;
                } else {
                    ChessGame.displayStatusText("Check! " + king.getColor().getFancyName() + " has to defend.");
                    return;
                }
            } else if (king.isStaleMate()) {
                ChessGame.displayStatusText("Stalemate! " + king.getColor().getFancyName() + " can't move.");
                return;
            }
        }*/
//        if (ruleOf50 >= 100) {
//            ChessGame.displayStatusText("50-move-rule applies");
//        }
        ChessGame.displayStatusText("");
    }

/*    public void recalculateAttackedFields() {
        resetAttackedFields();
        Arrays.stream(fields)
                .filter(f -> f.figure != null && f.figure.getColor() == Color.WHITE)
                .forEach(f -> attackedFields.get(Color.WHITE).addAll(f.getFigure().getAccessibleFields()));
        Arrays.stream(fields)
                .filter(f -> f.figure != null && f.figure.getColor() == Color.BLACK)
                .forEach(f -> attackedFields.get(Color.BLACK).addAll(f.getFigure().getAccessibleFields()));
    }*/

    public Set<ChessField> getAllAccessibleFields(Color color) {
        return attackedFields.get(color);
    }

    public King getKing() {
        for (ChessField field : fields) {
            if (field.figure instanceof King) {
                return (King) field.figure;
            }
        }
        return null;
    }

    public Knight getKnight() {
        for (ChessField field : fields) {
            if (field.figure instanceof Knight) {
                return (Knight) field.figure;
            }
        }
        return null;
    }

    public boolean isGameState() {
        return gameState;
    }

    public List<Figure> getFigures() {
        return getFigures(null);
    }

    public List<Figure> getFigures(Color color) {
        List<Figure> figures = new ArrayList<>();
        Arrays.stream(fields)
                .filter(f -> f.figure != null && (color == null || f.figure.getColor() == color))
                .forEach(f -> figures.add(f.figure));
        return figures;
    }

    public void reset(ChessField field) {
        for (ChessField o : fields) {
            if(field.equals(o)) {
                field.setFigure(null, true);
            }
        }
    }

    public void clear() {
        for (ChessField field : fields) {
            field.setFigure(null, true);
        }
        //recalculateAttackedFields();
        currentTurn = 1;
        ruleOf50 = 0;
        ChessGame.displayStatusText("");
    }

    public void setIO(ChessIO io) {
       // this.io.put(io.getFileExtension(), io);
      //  this.io.put("json", io);
        chessIO = io;
    }

    public ChessIO getIO() {
        return chessIO;
    }

    public void loadFromResource(String resource) {
        load(getFileExtension(resource), Helper.loadDataFromResource(resource));
    }

    public void load(File file) {
        load(getFileExtension(file.getName()), Helper.loadDataFromFile(file));
    }

    private String getFileExtension(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private void load(String type, byte[] s) {
        clear();
        chessIO.load(s, this); //io.get(type) 是否得到ChessIO对象？？？
        //recalculateAttackedFields();
        //gameStateTest();
    }

    public void save(File file) {
        byte[] s = chessIO
                .save(this);
        Helper.saveDataToFile(s, file);
    }

    public void saveGameInfo() {
        gameInfo.setEndTime(dateTimeFormatter.format(LocalDateTime.now()));
        gameInfo.setScores(ruleOf50);
        gameInfo.setResult(gameState);

        byte[] s = chessIO.saveResult(gameInfo);
        Helper.saveGameInfoToFile(s);
    }
}
