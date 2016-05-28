package com.coveo.blitz.client.bot;

import java.util.Comparator;
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

    int x = 100000;
    int y = 100000;
    List<Position> nearMines = new ArrayList<Position>();
    int shortest = 1000000000;
    int tempshortest;
    boolean takingBeer = false;

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

        shortest = 1000000000;
        nearMines.clear();

        if (takingBeer) {
            if ((gameState.getHero().getLife() + 3) >= 100) {
                takingBeer = false;
            }

            if (gameState.getHero().getLife() < 15)
                return pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(x, y), false);

            return pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(x, y), true);
        }


        for (int i = 0; i < size; i++) {
            List<Tile> tilelist = map.get(i);
            for (int j = 0; j < size; j++) {
                Tile tile = tilelist.get(j);

                if (gameState.getHero().getId() == 1 && (tile.getSymbol().equals(Tile.MineNeutral.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer2.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer3.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer4.toString())) && gameState.getHero().getLife() > 50) {
                    nearMines.add(new Position(i, j));
                } else if (gameState.getHero().getId() == 2 && (tile.getSymbol().equals(Tile.MineNeutral.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer1.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer3.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer4.toString())) && gameState.getHero().getLife() > 50) {
                    nearMines.add(new Position(i, j));
                } else if (gameState.getHero().getId() == 3 && (tile.getSymbol().equals(Tile.MineNeutral.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer2.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer1.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer4.toString())) && gameState.getHero().getLife() > 50) {
                    nearMines.add(new Position(i, j));
                } else if (gameState.getHero().getId() == 4 && (tile.getSymbol().equals(Tile.MineNeutral.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer2.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer3.toString()) ||
                        tile.getSymbol().equals(Tile.MinePlayer1.toString())) && gameState.getHero().getLife() > 50) {
                    nearMines.add(new Position(i, j));
                } else if (gameState.getHero().getLife() <= 50 && tile.getSymbol().equals(Tile.Tavern.toString())) {
                    if (gameState.getHero().getLife() < 15)
                        tempshortest = pathfinder.shortestPath(gameState.getHero().getPos(), new Position(i, j), false).size();
                    else
                        tempshortest = pathfinder.shortestPath(gameState.getHero().getPos(), new Position(i, j), true).size();
                    if (tempshortest < shortest) {
                        shortest = tempshortest;
                        x = i;
                        y = j;
                    }
                    takingBeer = true;
                }
            }
        }

        if (!takingBeer) {
            nearMines.sort(new ComparatorPosition(gameState.getHero(), pathfinder));

            if (!map.get(nearMines.get(0).getX()).get(nearMines.get(0).getY()).getSymbol().equals(Tile.MineNeutral.toString())) {
                x = nearMines.get(0).getX();
                y = nearMines.get(0).getY();
                System.out.println("HERE1");
            } else if (!map.get(nearMines.get(1).getX()).get(nearMines.get(1).getY()).getSymbol().equals(Tile.MineNeutral.toString())
                    && pathfinder.shortestPath(gameState.getHero().getPos(), nearMines.get(1), true).size() < pathfinder.shortestPath(gameState.getHero().getPos(), nearMines.get(0), true).size() + 5) {
                x = nearMines.get(1).getX();
                y = nearMines.get(1).getY();
                System.out.println("HERE2");
            } else if (!map.get(nearMines.get(2).getX()).get(nearMines.get(2).getY()).getSymbol().equals(Tile.MineNeutral.toString())
                    && pathfinder.shortestPath(gameState.getHero().getPos(), nearMines.get(2), true).size() < pathfinder.shortestPath(gameState.getHero().getPos(), nearMines.get(0), true).size() + 5) {
                x = nearMines.get(2).getX();
                y = nearMines.get(2).getY();
                System.out.println("HERE3");
            } else {
                x = nearMines.get(0).getX();
                y = nearMines.get(0).getY();
                System.out.println("HERE4");
            }

        }

        if (gameState.getHero().getLife() < 15)
            return pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(x, y), false);

        return pathfinder.navigateTowards(gameState.getHero().getPos(), new Position(x, y), true);
    }

    @Override
    public void setup() {
    }

    @Override
    public void shutdown() {
    }
}
