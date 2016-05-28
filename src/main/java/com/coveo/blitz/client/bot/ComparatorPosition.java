package com.coveo.blitz.client.bot;

import com.coveo.blitz.client.dto.GameState;

import java.util.Comparator;

/**
 * Created by Julien on 16-05-28.
 */
public class ComparatorPosition implements Comparator<GameState.Position> {

    GameState.Hero player;
    Pathfinder pathfinder;

    public ComparatorPosition(GameState.Hero p, Pathfinder p2) {
        player = p;
        pathfinder = p2;
    }

    @Override
    public int compare(GameState.Position o1, GameState.Position o2) {
        if (player.getLife() < 15)
            if (pathfinder.shortestPath(player.getPos(), o1, false).size() < pathfinder.shortestPath(player.getPos(), o2, false).size())
                return -1;
            else
                return 1;
        else if (pathfinder.shortestPath(player.getPos(), o1, false).size() < pathfinder.shortestPath(player.getPos(), o2, true).size())
            return -1;
        else
            return 1;
    }
}
