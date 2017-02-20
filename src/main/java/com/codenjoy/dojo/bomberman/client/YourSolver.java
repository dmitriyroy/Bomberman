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

import static java.lang.Math.sqrt;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "dmitriy.roy@ita.biz.ua";

    private Dice dice;
    private Board board;
    Direction lastDirection = Direction.UP;
    Direction newDirection = Direction.UP;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        Point myPosition = board.getBomberman();
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

//        String direct = "";
//        direct = Direction.ACT.UP.toString();
//        direct = Direction.ACT.toString();
//        direct = Direction.ACT(2, 0, -1);
        // если Х нечетная, то я могу двигаться только по вертикали
        if(myPosition.getX() % 2 == 1){
            if(lastDirection.toString().equals("UP")){
                if(isPointEmpty(myPosition,"UP", board)) {
                    newDirection = Direction.UP;
                    lastDirection = newDirection;
                }else{
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                }
            }else if(lastDirection.toString().equals("DOWN")){
                if(isPointEmpty(myPosition,"DOWN", board)) {
                    newDirection = Direction.DOWN;
                    lastDirection = newDirection;
                }else {
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                }

            }else{
                newDirection = Direction.UP;
                lastDirection = newDirection;
            }
//            else if(lastDirection.toString().equals("LEFT")){
//
//            }else if(lastDirection.toString().equals("RIGHT")){
//
//            }else{
//                // TODO надо подумать. Здесь мы положили бомбу .ACT
//            }

        // если Х четная, то я могу двигаться только по горизонтали
        }else{
            if(lastDirection.toString().equals("LEFT")){
                if(isPointEmpty(myPosition,"LEFT", board)) {
                    newDirection = Direction.LEFT;
                    lastDirection = newDirection;
                }else{
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                }
            }else if(lastDirection.toString().equals("RIGHT")){
                if(isPointEmpty(myPosition,"RIGHT", board)) {
                    newDirection = Direction.RIGHT;
                    lastDirection = newDirection;
                }else{
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                }

            }else{
                newDirection = Direction.RIGHT;
                lastDirection = newDirection;
            }
        }

        return newDirection.toString();
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

    public boolean isPointEmpty(Point point,String direction,Board board){

        Collection<Point> destroyWals = board.getDestroyWalls();
        Collection<Point> bombs = board.getBombs();
        Collection<Point> bombermans = board.getOtherBombermans();
        Collection<Point> meatChoppers = board.getMeatChoppers();
        boolean isPointDanger = false;
        switch(direction){
            case "UP":
                point.move(point.getX(),point.getY()-1);
                break;
            case "DOWN":
                point.move(point.getX(),point.getY()+1);
                break;
            case "LEFT":
                point.move(point.getX()-1,point.getY());
                break;
            case "RIGHT":
                point.move(point.getX()+1,point.getY());
                break;
        }
        if(point.getX() <= 0
//            || point.getX() >= sqrt(board.size())
            || point.getX() >= 32 //board.size()
            || point.getY() <= 0
//            || point.getY() >= sqrt(board.size())
            || point.getY() >= 32 //board.size()
                // TODO - проверить, возможно, при выполнении этого условия будет ставить бомбы перед перегородками
            || (point.getX() % 2 == 0 && point.getY() % 2 == 0)){
            isPointDanger = true;
        }
        for (Point destroyWal:destroyWals) {
            if(isPointDanger){
                break;
            }
            if(destroyWal.getX() == point.getX() && destroyWal.getY() == point.getY()){
                isPointDanger = true;
                break;
            }
        }
        for (Point bomb:bombs) {
            if(isPointDanger){
                break;
            }
            if(bomb.getX() == point.getX() && bomb.getY() == point.getY()){
                isPointDanger = true;
                break;
            }
        }
        for (Point bomberman:bombermans) {
            if(isPointDanger){
                break;
            }
            if(bomberman.getX() == point.getX() && bomberman.getY() == point.getY()){
                isPointDanger = true;
                break;
            }
        }
        for (Point meatChopper:meatChoppers) {
            if(isPointDanger){
                break;
            }
            if(meatChopper.getX() == point.getX() && meatChopper.getY() == point.getY()){
                isPointDanger = true;
                break;
            }
        }

        return !isPointDanger;
    }

}
