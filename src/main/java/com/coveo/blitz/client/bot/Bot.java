package com.coveo.blitz.client.bot;

import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coveo.blitz.client.dto.GameState;
import com.coveo.blitz.client.dto.GameState.Position;

/**
 * Example bot
 */
public class Bot implements SimpleBot {
	private static final Logger logger = LogManager.getLogger(Bot.class);

    private BoardParser parser = new BoardParser();

    @Override
    public BotMove move(GameState gameState) {
    	logger.info(gameState.getHero().getPos().toString());

        List<Tile> tiles = parser.parse(gameState.getGame().getBoard().getTiles());
        List<List<Tile>> map = new ArrayList<List<Tile>>();
        int size = gameState.getGame().getBoard().getSize();
        for (int rowIndex = 0; rowIndex < size; ++rowIndex) {
            List<Tile> row = new ArrayList<Tile>();
            for (int colIndex = 0; colIndex < size; ++colIndex) {
                row.add(tiles.get(rowIndex * size + colIndex));
            }
            map.add(row);
        }

        Pathfinder pathfinder = new Pathfinder(map);
        // TODO implement SkyNet here
        // Example pathfinding:
        // BotMove move = pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(0, 0));

        int x, y; //destination choisi
        x = 100000;
        y = 100000;
        int shortest = 1000000000;
        int tempshortest;

        for (int i = 0; i < size; i++) {
            List<Tile> tilelist = map.get(i);
                for (int j = 0; j < size; j++) {
                    Tile tile = tilelist.get(j);
                    if(tile.getSymbol().equals(Tile.MineNeutral.toString()) ||
                            tile.getSymbol().equals(Tile.MinePlayer2.toString()) ||
                            tile.getSymbol().equals(Tile.MinePlayer3.toString()) ||
                            tile.getSymbol().equals(Tile.MinePlayer4.toString())) {
                            tempshortest = pathfinder.shortestPath(gameState.getHero().getPos(),new Position(i,j)).size();
                        if (tempshortest < shortest) {
                            shortest = tempshortest;
                            x = i;
                            y = j;
                        }
                    }

                }

        }

        return pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(x,y));




        /*int randomNumber = (int)(Math.random() * 5);
        switch(randomNumber) {
            case 1:
            	logger.info("Going north");
                return BotMove.NORTH;
            case 2:
            	logger.info("Going south");
                return BotMove.SOUTH;
            case 3:
            	logger.info("Going east");
                return BotMove.EAST;
            case 4:
            	logger.info("Going west");
                return BotMove.WEST;
            default:
            	logger.info("Going nowhere");
                return BotMove.STAY;
        }*/
    }

    @Override
    public void setup() {
    }

    @Override
    public void shutdown() {
    }
}
