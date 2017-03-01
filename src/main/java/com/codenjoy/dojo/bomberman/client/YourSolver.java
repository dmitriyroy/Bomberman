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

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "dmitriy.roy@ita.biz.ua";

    private Dice dice;
    private Board board;
    private Direction lastDirection = Direction.UP;
    private Direction newDirection = Direction.UP;
    private String returnDirectionString;
    private Collection<Point> destroyWalls;
    private Collection<Point> bombs;
    private Collection<Point> bombermans;
    private Collection<Point> meatChoppers;
    private Point myPosition;
    private Point goal;
    private boolean isGoalExist = true;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        myPosition = board.getBomberman();
        destroyWalls = board.getDestroyWalls();
        bombs = board.getBombs();
        bombermans = board.getOtherBombermans();
        meatChoppers = board.getMeatChoppers();

        if(isGoalExist){
            goal = getGoal(board,myPosition, "DESTROYWALL");
            isGoalExist = true;
        }

        returnDirectionString = stepToGoal(board,myPosition,goal);




        return returnDirectionString;
    }

    public static void main(String[] args) {
//        start(USER_NAME, WebSocketRunner.Host.REMOTE);
        start(USER_NAME, WebSocketRunner.Host.LOCAL);
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
    public boolean isMoveUpDown(Point myPosition){
        return myPosition.getX() % 2 == 1;
    }
    public boolean isMoveLeftRight(Point myPosition){
        return myPosition.getY() % 2 == 1;
    }
    public String stepToGoal(Board board, Point myPosition, Point goal){
        String outDirectionString;
        //   X X X X X X X
        // Y 0 1 2 3 4 5 6
        // Y 1 8 8
        // Y 2 8 +   +   +
        // Y 3     8 8
        // Y 4   + 8 +   +
        // Y 5

        // если цель выше ( getY() > моего.getY() )
        // то идем вверх при условии, что вверх-вниз можно ходить
        if((goal.getY() - myPosition.getY()) < 0){
            // если можем двигаться по вертикали
            outDirectionString = step(board, myPosition, "UP");

            // движемся вниз при условии, что вверх-вниз можно ходить
        }else if((goal.getY() - myPosition.getY()) > 0){
            // если можем двигаться по вертикали
            outDirectionString = step(board, myPosition, "DOWN");

        // находимся на одной линии Y
        // (goal.getY() - myPosition.getY()) == 0
        }else{
            // если можем двигаться по вертикали
            if(isMoveLeftRight(myPosition)){
                if((goal.getX() - myPosition.getX()) < 0){
                    // идем влево
                    outDirectionString = step(board, myPosition, "LEFT");
                }else if((goal.getX() - myPosition.getX()) > 0){
                    // идем вправо
                    outDirectionString = step(board, myPosition, "RIGHT");
                }else{
                    // похоже сюда мы не попадем,
                    // а если попадем, то значит, что мы родились взаперти
                    outDirectionString = Direction.ACT.toString();
                }
            }else if(myPosition.getX() % 2 == 1 ) {
                outDirectionString = step(board, myPosition, "UP");
            }else{
                if((goal.getX() - myPosition.getX()) < 0){
                    // идем влево
                    outDirectionString = step(board, myPosition, "LEFT");
                }else if((goal.getX() - myPosition.getX()) > 0){
                    // идем вправо
                    outDirectionString = step(board, myPosition, "RIGHT");
                }else{
                    // похоже сюда мы не попадем,
                    // а если попадем, то значит, что мы родились взаперти
                    outDirectionString = Direction.ACT.toString();
                }
            }
        }

        return outDirectionString;
    }

    private Direction getDirection(String directionString){
        Direction direction;
        switch(directionString){
            case "UP":
                direction = Direction.UP;
                break;
            case "DOWN":
                direction = Direction.DOWN;
                break;
            case "LEFT":
                direction = Direction.LEFT;
                break;
            case "RIGHT":
                direction = Direction.RIGHT;
                break;
            case "STOP":
                direction = Direction.STOP;
                break;
            default:
                direction = Direction.ACT;
        }
        return direction;
    }

    private Direction reverseDirection(String directionString){
        Direction direction;
        switch(directionString){
            case "UP":
                direction = Direction.DOWN;
                break;
            case "DOWN":
                direction = Direction.UP;
                break;
            case "LEFT":
                direction = Direction.RIGHT;
                break;
            case "RIGHT":
                direction = Direction.LEFT;
                break;
            case "STOP":
                direction = Direction.ACT;
                break;
            default:
                direction = Direction.STOP;
        }
        return direction;
    }

    private String step(Board board, Point myPosition, String direction) {
        String outDirectionString;
        if(isPointEmpty(myPosition,direction.toUpperCase(), board)) {
            newDirection = getDirection(direction.toUpperCase());
            lastDirection = newDirection;
            outDirectionString = newDirection.toString();
        }else if(isPointEmpty(myPosition,reverseDirection(direction.toUpperCase()).toString(), board)){
            newDirection = reverseDirection(direction.toUpperCase());
            lastDirection = newDirection;
            outDirectionString = newDirection.toString();

        }else {
            lastDirection = newDirection.inverted();
            newDirection = Direction.ACT;
            outDirectionString = newDirection.toString() + "," + lastDirection.toString();

        }
        return outDirectionString;
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
            default:
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

    private Point getGoal(Board board, Point myPosition, String typeGoal) {
        Point goal = null;
        int lengthWay = 1000;
        switch (typeGoal.toUpperCase()){
            case "DESTROYWALL":
                for(Point destroyWall: destroyWalls){
                    int tmpLength =Math.abs(myPosition.getX() - destroyWall.getX()) + Math.abs(myPosition.getY() - destroyWall.getY());
                    if(tmpLength < lengthWay){
                        lengthWay = tmpLength;
                        goal = destroyWall;
                    }
                }
                break;
            default:
        }

        return goal;
    }

}
