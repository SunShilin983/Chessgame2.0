package org.ssl.model;

import com.google.gson.*;
import org.ssl.model.figure.Color;
import org.ssl.model.figure.Figure;
import org.ssl.view.ChessBoard;

public class ChessIO {

        public void load(byte[] json, ChessBoard board) {
            JsonObject element = new JsonParser().parse(new String(json)).getAsJsonObject();
            loadFigures(Color.BLACK, element.getAsJsonArray("black"), board);
            //loadFigures(Color.WHITE, element.getAsJsonArray("white"), board);

            board.setCurrenTurn(element.get("current_turn").getAsInt());
            board.set50MoveRuleTurns(element.get("50_move_rule_turns").getAsInt());
            if(element.get("username")!=null)
            board.setUsername(element.get("username").getAsString());
        }

        private void loadFigures(Color color, JsonArray jsonFigures, ChessBoard board) {
            for (JsonElement jsonFigure : jsonFigures) {
                String type = jsonFigure.getAsJsonObject().get("type").getAsString();
                String pos = jsonFigure.getAsJsonObject().get("pos").getAsString();
                int firstTurn = jsonFigure.getAsJsonObject().get("first_turn").getAsInt();
                Figure figure = FigureFactory.createFigure(color, type, pos, firstTurn);
                board.setFigure(figure);
            }
        }

        public byte[] save(ChessBoard board) {
            JsonObject main = new JsonObject();
            main.addProperty("current_turn", board.getCurrentTurn());
            main.addProperty("50_move_rule_turns", board.get50MoveRuleTurns());
            main.addProperty("username", board.getUsername());
            JsonArray black = new JsonArray();
            JsonArray white = new JsonArray();
            for (Figure figure : board.getFigures()) {
                JsonObject jsonFigure = new JsonObject();
                jsonFigure.addProperty("type", figure.getName());
                jsonFigure.addProperty("pos", figure.getPos());
                jsonFigure.addProperty("first_turn", figure.getFirstTurn());
                if (figure.getColor() == Color.BLACK) {
                    black.add(jsonFigure);
                } else {
                    white.add(jsonFigure);
                }
            }
            main.add("black", black);
            main.add("white", white);
            return new GsonBuilder().create().toJson(main).getBytes();
        }

        public byte[] saveResult(GameInfo info){
            JsonObject main = new JsonObject();
            JsonArray gameinfo = new JsonArray();
            JsonObject player = new JsonObject();
            player.addProperty("username",info.getUsername());
            player.addProperty("starttime",info.getStartTime());
            player.addProperty("endtime",info.getEndTime());
            player.addProperty("scores",info.getScores());
            player.addProperty("reslut",info.isResult());
            gameinfo.add(player);
            main.add("playinfo",gameinfo);
            return new GsonBuilder().create().toJson(main).getBytes();
        }


        public String getFileTypeDescription() {
            return "JSON files (*.json)";
        }

        public String getFileExtension() {
            return "json";
        }
    }