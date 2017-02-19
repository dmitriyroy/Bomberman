package com.codenjoy.dojo.bomberman.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Collection;
import java.util.Random;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "dmitriy.roy@ita.biz.ua";

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
//        if(true){
//            return Direction.ACT.toString();
//        }

//        board.
        Point myPosition = board.getBomberman();
        Direction lastDirection = Direction.UP;
        Direction newDirection = Direction.UP;
        Collection<Point> destroyWals = board.getDestroyWalls();
        Collection<Point> bombs = board.getBombs();
        Collection<Point> bombermans = board.getOtherBombermans();
        Collection<Point> meatChppers = board.getMeatChoppers();

        int dx = myPosition.getX() - myPosition.getX();
        //   X X X X X X X
        // Y 0 1 2 3 4 5 6
        // Y 1 8 8
        // Y 2 8 +   +   +
        // Y 3     8 8
        // Y 4   + 8 +   +
        // Y 5


        // если Х нечетная, то я могу двигаться только по вертикали
        if(myPosition.getX() % 2 == 1){
            if(lastDirection.toString().equals("UP")){
                if(myPosition.getY() > 1 && isPointEmpty(Point)) {
                    newDirection = Direction.UP;
                }else{
                    newDirection = Direction.RIGHT;
                }
            }else if(lastDirection.toString().equals("DOWN")){

            }else if(lastDirection.toString().equals("LEFT")){

            }else if(lastDirection.toString().equals("RIGHT")){

            }else{
                // TODO надо подумать. Здесь мы положили бомбу .ACT
            }

        // если Х четная, то я могу двигаться только по горизонтали
        }else{
            newDirection = Direction.ACT;

        }

        lastDirection = newDirection;
        return newDirection.toString();


//        return Direction.ACT.toString();
//        int changeAct = new Random().nextInt();
//        if(changeAct % 5 == 0){
//            return Direction.ACT.toString();
//        }else if(changeAct % 4 == 0){
//            return Direction.LEFT.toString();
//        }else if(changeAct % 3 == 0){
//            return Direction.UP.toString();
//        }else if(changeAct % 2 == 0){
//            return Direction.DOWN.toString();
//        }
//        if(changeAct % 2 == 0){
//            return Direction.DOWN.toString();
//        }
//        return Direction.random().toString();
//        return Direction.random(dice).toString();

//        fdl;fjpdjfwejf
    }

    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new YourSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
