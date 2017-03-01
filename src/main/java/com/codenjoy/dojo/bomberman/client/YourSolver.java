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

import static java.lang.Math.abs;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "dmitriy.roy@ita.biz.ua";

    private Dice dice;
    private Board board;
    private Direction lastDirection = Direction.STOP;
    private Direction newDirection = Direction.STOP;
    private String returnDirectionString;
    private Collection<Point> destroyWalls;
    private Collection<Point> bombs;
    private Collection<Point> bombermans;
    private Collection<Point> meatChoppers;
    private Point myPosition;
    private Point goal;
    private boolean isNeedGoal = true;

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

        if(isNeedGoal){
            goal = getGoal(board,myPosition, "DESTROYWALL");
            isNeedGoal = true;
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
        String outDirectionString = "";

        // есль цель на этом уровне
        if(goal.getY() - myPosition.getY() == 0) {
            if(isMoveLeftRight(myPosition)){
                // если цель слева
                if(goal.getX() - myPosition.getX() < 0){
                    if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = Direction.LEFT.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else{
                        if(isMoveUpDown(myPosition)){
                            if(isPointEmpty(board,myPosition,"UP")){
                                outDirectionString = "ACT," + Direction.UP.toString();
                            }else if(isPointEmpty(board,myPosition,"DOWN")){
                                outDirectionString = "ACT," + Direction.DOWN.toString();
                            }else{
                                // всё занято
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        if(isMoveUpDown(myPosition)){
                            if(isPointEmpty(board,myPosition,"UP")){
                                outDirectionString = "ACT," + Direction.UP.toString();
                            }else if(isPointEmpty(board,myPosition,"DOWN")){
                                outDirectionString = "ACT," + Direction.DOWN.toString();
                            }else{
                                // всё занято
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }
                }
            }else{
                if(isPointEmpty(board,myPosition,"UP")){
                    outDirectionString = Direction.UP.toString();
                }else if(isPointEmpty(board,myPosition,"DOWN")){
                    outDirectionString = "ACT," + Direction.DOWN.toString();
                }else{
                    // всё занято
                    outDirectionString = Direction.ACT.toString();
                }
            }

        // если цель выше
        }else if(goal.getY() - myPosition.getY() < 0){
            if(abs(goal.getY() - myPosition.getY()) == 1){
                // если цель справа
                if(goal.getX() - myPosition.getX() > 0) {
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "RIGHT")) {
                            outDirectionString = Direction.RIGHT.toString();
                        } else if (isPointEmpty(board, myPosition, "LEFT")) {
                            outDirectionString = "ACT," + Direction.LEFT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP")) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN")) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "UP")) {
                            outDirectionString = Direction.UP.toString();
                        } else if (isPointEmpty(board, myPosition, "DOWN")) {
                            outDirectionString = "ACT," + Direction.DOWN.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                // цель слева
                }else if(goal.getX() - myPosition.getX() < 0){
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "LEFT")) {
                            outDirectionString = Direction.LEFT.toString();
                        } else if (isPointEmpty(board, myPosition, "RIGHT")) {
                            outDirectionString = "ACT," + Direction.RIGHT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP")) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN")) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "UP")) {
                            outDirectionString = Direction.UP.toString();
                        } else if (isPointEmpty(board, myPosition, "DOWN")) {
                            outDirectionString = "ACT," + Direction.DOWN.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"DOWN")){
                        outDirectionString = "ACT," + Direction.DOWN.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else if(isMoveUpDown(myPosition)){
                if(isPointEmpty(board,myPosition,"UP")){
                    // если клетка свободная, то идемм верх
                    outDirectionString = Direction.UP.toString();
                }else{
                    // если она занята, то проверяем
                    if(isPointEmpty(board,myPosition,"DOWN")){
                        outDirectionString = "ACT," + Direction.DOWN.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else{
                if(goal.getX() - myPosition.getX() > 0) {
                    if (isPointEmpty(board, myPosition, "RIGHT")) {
                        outDirectionString = Direction.RIGHT.toString();
                    } else if (isPointEmpty(board, myPosition, "LEFT")) {
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }else{
                    if (isPointEmpty(board, myPosition, "LEFT")) {
                        outDirectionString = Direction.LEFT.toString();
                    } else if (isPointEmpty(board, myPosition, "RIGHT")) {
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }
        // если цель ниже
        }else if(goal.getY() - myPosition.getY() > 0){
            if(abs(goal.getY() - myPosition.getY()) == 1){
                // если цель справа
                if(goal.getX() - myPosition.getX() > 0) {
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "RIGHT")) {
                            outDirectionString = Direction.RIGHT.toString();
                        } else if (isPointEmpty(board, myPosition, "LEFT")) {
                            outDirectionString = "ACT," + Direction.LEFT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP")) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN")) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "DOWN")) {
                            outDirectionString = Direction.DOWN.toString();
                        } else if (isPointEmpty(board, myPosition, "UP")) {
                            outDirectionString = "ACT," + Direction.UP.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                // цель слева
                }else if(goal.getX() - myPosition.getX() < 0){
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "LEFT")) {
                            outDirectionString = Direction.LEFT.toString();
                        } else if (isPointEmpty(board, myPosition, "RIGHT")) {
                            outDirectionString = "ACT," + Direction.RIGHT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP")) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN")) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "DOWN")) {
                            outDirectionString = Direction.DOWN.toString();
                        } else if (isPointEmpty(board, myPosition, "UP")) {
                            outDirectionString = "ACT," + Direction.UP.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"UP")){
                        outDirectionString = "ACT," + Direction.UP.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else if(isMoveUpDown(myPosition)){
                if(isPointEmpty(board,myPosition,"DOWN")){
                    // если клетка свободная, то идемм верх
                    outDirectionString = Direction.DOWN.toString();
                }else{
                    // если она занята, то проверяем
                    if(isPointEmpty(board,myPosition,"RIGHT")){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT")){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else if(isPointEmpty(board,myPosition,"UP")){
                        outDirectionString = "ACT," + Direction.UP.toString();
                    }else{
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else{
                if(goal.getX() - myPosition.getX() < 0) {
                    if (isPointEmpty(board, myPosition, "LEFT")) {
                        outDirectionString = Direction.LEFT.toString();
                    } else if (isPointEmpty(board, myPosition, "RIGHT")) {
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }else{
                    if (isPointEmpty(board, myPosition, "RIGHT")) {
                        outDirectionString = Direction.RIGHT.toString();
                    } else if (isPointEmpty(board, myPosition, "LEFT")) {
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }
        }


/*
        // если цель выше ( getY() > моего.getY() )
        // то идем вверх при условии, что вверх-вниз можно ходить
        if((goal.getY() - myPosition.getY()) < 0){
            if((goal.getY() - myPosition.getY()) == 1 || (goal.getY() - myPosition.getY()) == -1){
                if((goal.getX() - myPosition.getX()) > 0) {
                    outDirectionString = step(board, myPosition, "RIGHT");
                }else if((goal.getX() - myPosition.getX()) < 0){
                    outDirectionString = step(board, myPosition, "LEFT");
                }else{
                    // TODO здесь возможно будет ингода стопор (когда будем идти мимо бомбы)
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                    outDirectionString = newDirection.toString() + "," + lastDirection.toString();
                }
            }else {
                // если можем двигаться по вертикали
                outDirectionString = step(board, myPosition, "UP");
            }

            // движемся вниз при условии, что вверх-вниз можно ходить
        }else if((goal.getY() - myPosition.getY()) > 0){
            if((goal.getY() - myPosition.getY()) == 1 || (goal.getY() - myPosition.getY()) == -1){
                if((goal.getX() - myPosition.getX()) > 0) {
                    outDirectionString = step(board, myPosition, "RIGHT");
                }else if((goal.getX() - myPosition.getX()) < 0){
                    outDirectionString = step(board, myPosition, "LEFT");
                }else{
                    // TODO здесь возможно будет ингода стопор (когда будем идти мимо бомбы)
                    lastDirection = newDirection.inverted();
                    newDirection = Direction.ACT;
                    outDirectionString = newDirection.toString() + "," + lastDirection.toString();
                }
            }else {
                // если можем двигаться по вертикали
                outDirectionString = step(board, myPosition, "DOWN");
            }

        // находимся на одной линии Y
        // (goal.getY() - myPosition.getY()) == 0
        }else{
            // если можем двигаться по горизонтали
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
*/
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
        if(isPointEmpty(board,myPosition,direction.toUpperCase())) {
            newDirection = getDirection(direction.toUpperCase());
            lastDirection = newDirection;
            outDirectionString = newDirection.toString();
        }
        else if(isPointEmpty(board,myPosition,reverseDirection(direction.toUpperCase()).toString())){
            newDirection = reverseDirection(direction.toUpperCase());
            lastDirection = newDirection;
            outDirectionString = newDirection.toString();

        }
        else {
            lastDirection = newDirection.inverted();
            newDirection = Direction.ACT;
            outDirectionString = newDirection.toString() + "," + lastDirection.toString();

        }
        return outDirectionString;
    }


    public boolean isPointEmpty(Board board,Point point,String direction){

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
            || point.getX() >= board.size() - 1 //32 //board.size()
            || point.getY() <= 0
//            || point.getY() >= sqrt(board.size())
            || point.getY() >= board.size() - 1 //32 //board.size()
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

        // возвращаем poin на место
        // TODO убрать движение point и потом возвращение на место
        switch(direction){
            case "UP":
                point.move(point.getX(),point.getY()+1);
                break;
            case "DOWN":
                point.move(point.getX(),point.getY()-1);
                break;
            case "LEFT":
                point.move(point.getX()+1,point.getY());
                break;
            case "RIGHT":
                point.move(point.getX()-1,point.getY());
                break;
            default:
        }
        return !isPointDanger;
    }

    private Point getGoal(Board board, Point myPosition, String typeGoal) {
        Point goal = null;
        int lengthWay = 1000;
        switch (typeGoal.toUpperCase()){
            case "DESTROYWALL":
                for(Point destroyWall: destroyWalls){
                    int tmpLength = abs(myPosition.getX() - destroyWall.getX()) + abs(myPosition.getY() - destroyWall.getY());
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
