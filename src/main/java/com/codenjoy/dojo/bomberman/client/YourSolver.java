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
/**
 * Version by Dmitriy Roy dmitriy.roy@ita.biz.ua
 */

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "dmitriy.roy@ita.biz.ua";
    private static final int MAX_COUNT_WITHOUT_BOMB = 25;

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
    private int currentCountWithoutBomb = 0;

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
//            goal.move(1, 31);
            isNeedGoal = false;
        }
        System.out.println("--->>>My Goal: " + goal.toString());
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
        String tmpOutDireсtion = "";
        String outDirectionString = stepNormal(board, goal, myPosition);

        // если поставили бомбу, то нужна новая цель
        // иначе будет бежать на свою бомбу
        if(outDirectionString.indexOf("ACT") >= 0){
            tmpOutDireсtion += "ACT,";
            isNeedGoal = true;
            currentCountWithoutBomb = 0;
        }else{
            if(isNearOtherGoal(board, myPosition)){
                outDirectionString += "ACT," + outDirectionString;
            }
            currentCountWithoutBomb++;
        }
        // если зациклились, то подрываем себя
        if(currentCountWithoutBomb > MAX_COUNT_WITHOUT_BOMB){
            currentCountWithoutBomb = 0;
            return Direction.ACT.toString();
        }

        // если новую позицию заденет бомба,
        // то надо искать свободное поле
        outDirectionString = correctDirectionForBlast(board, myPosition, tmpOutDireсtion, outDirectionString);

        return outDirectionString;
    }

    private boolean isNearOtherGoal(Board board, Point myPosition) {
        if(!isPointEmpty(board,myPosition,"UP", false)) {
            return true;
        }
        if(!isPointEmpty(board,myPosition,"RIGHT", false)) {
            return true;
        }
        if(!isPointEmpty(board,myPosition,"DOWN", false)) {
            return true;
        }
        if(!isPointEmpty(board,myPosition,"LEFT", false)) {
            return true;
        }
        return false;
    }

    private String correctDirectionForBlast(Board board, Point myPosition, String tmpOutDirection, String outDirectionString) {
        String clearDirection = outDirectionString.replaceAll("ACT","").replaceAll(",","").replaceAll(" ","");
        if(clearDirection.length() > 0 && !clearDirection.equals("STOP")){
            String whereIsBomb = whereIsTheBomb(board,myPosition,outDirectionString);
            if(!whereIsBomb.equals("NOTHING")){
                switch(whereIsBomb){
                    case "UP":
                        if(isPointEmpty(board,myPosition,"RIGHT",true)
                                && !isExplodeOnPoint(board,myPosition,"RIGHT")){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)
                                && !isExplodeOnPoint(board,myPosition,"LEFT")){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)
                                && !isExplodeOnPoint(board,myPosition,"DOWN")){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else{
                            // nothing TODO ? - попадаем ли сюда?
                            System.out.println("--->>>Попали в TODO." + "correctDirectionForBlast.case." + "UP");
                            outDirectionString = Direction.ACT.toString();
                        }
                        break;
                    case "DOWN":
                        if(isPointEmpty(board,myPosition,"RIGHT",true)
                                && !isExplodeOnPoint(board,myPosition,"RIGHT")){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)
                                && !isExplodeOnPoint(board,myPosition,"LEFT")){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else if(isPointEmpty(board,myPosition,"UP",true)
                                && !isExplodeOnPoint(board,myPosition,"UP")){
                            outDirectionString = tmpOutDirection + "UP";
                        }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else if(isPointEmpty(board,myPosition,"UP",true)){
                            outDirectionString = tmpOutDirection + "UP";
                        }else{
                            // nothing TODO ? - попадаем ли сюда?
                            System.out.println("--->>>Попали в TODO." + "correctDirectionForBlast.case." + "DOWN");
                            outDirectionString = Direction.ACT.toString();
                        }
                        break;
                    case "RIGHT":
                        if(isPointEmpty(board,myPosition,"UP",true)
                                && !isExplodeOnPoint(board,myPosition,"UP")){
                            outDirectionString = tmpOutDirection + "UP";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)
                                && !isExplodeOnPoint(board,myPosition,"DOWN")){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)
                                && !isExplodeOnPoint(board,myPosition,"LEFT")){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else if(isPointEmpty(board,myPosition,"UP",true)){
                            outDirectionString = tmpOutDirection + "UP";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                            outDirectionString = tmpOutDirection + "LEFT";
                        }else{
                            // nothing TODO ? - попадаем ли сюда?
                            System.out.println("--->>>Попали в TODO." + "correctDirectionForBlast.case." + "RIGHT");
                            outDirectionString = Direction.ACT.toString();
                        }
                        break;
                    case "LEFT":
                        if(isPointEmpty(board,myPosition,"UP",true)
                                && !isExplodeOnPoint(board,myPosition,"UP")){
                            outDirectionString = tmpOutDirection + "UP";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)
                                && !isExplodeOnPoint(board,myPosition,"DOWN")){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else if(isPointEmpty(board,myPosition,"RIGHT",true)
                                && !isExplodeOnPoint(board,myPosition,"RIGHT")){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else if(isPointEmpty(board,myPosition,"UP",true)){
                            outDirectionString = tmpOutDirection + "UP";
                        }else if(isPointEmpty(board,myPosition,"DOWN",true)){
                            outDirectionString = tmpOutDirection + "DOWN";
                        }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                            outDirectionString = tmpOutDirection + "RIGHT";
                        }else{
                            // nothing TODO ? - попадаем ли сюда?
                            System.out.println("--->>>Попали в TODO." + "correctDirectionForBlast.case." + "LEFT");
                            outDirectionString = Direction.ACT.toString();
                        }
                        break;
                    default:
                }
            }
        }
        return outDirectionString;
    }

    private String whereIsTheBomb(Board board, Point position, String directionString) {
        String outDirection = "";
        int newPosX =  position.getX();
        int newPosY =  position.getY();
        switch(directionString){
            case "UP":
                newPosY--;
                break;
            case "DOWN":
                newPosY++;
                break;
            case "RIGHT":
                newPosX++;
                break;
            case "LEFT":
                newPosX--;
                break;
        }
        if(!isExplodeOnPoint(board,position,directionString)){
            outDirection = "NOTHING";
        }else{
            Collection<Point> bombs = board.getBombs();
            Collection<Point> nearestBomb = new ArrayList<>();
            for (Point bomb:bombs) {
                if(bomb.getX() == newPosX && abs(bomb.getY() - newPosY) <= 4){
                    nearestBomb.add(bomb);
                }
                if(bomb.getY() == newPosY && abs(bomb.getX() - newPosX) <= 4){
                    nearestBomb.add(bomb);
                }
            }
            for (Point bomb:nearestBomb) {
                int minLen = board.size();
                int tmpLen = board.size();
                String minDirect = "";
                if(bomb.getX() == newPosX){
                    tmpLen = bomb.getY() - newPosY;
                    if(tmpLen < 0 && abs(tmpLen) < minLen){
                        minLen = tmpLen;
                        outDirection = "UP";
                    }
                    if(tmpLen > 0 && abs(tmpLen) < minLen){
                        minLen = tmpLen;
                        outDirection = "DOWN";
                    }
                }
                if(bomb.getY() == newPosY){
                    tmpLen = bomb.getX() - newPosX;
                    if(tmpLen < 0 && abs(tmpLen) < minLen){
                        minLen = tmpLen;
                        outDirection = "LEFT";
                    }
                    if(tmpLen > 0 && abs(tmpLen) < minLen){
                        minLen = tmpLen;
                        outDirection = "RIGHT";
                    }
                }
            }
        }
        return outDirection;
    }

    private boolean isExplodeOnPoint(Board board, Point position, String directionString) {
        Collection<Point> bombs = board.getBombs();
        int newPosX =  position.getX();
        int newPosY =  position.getY();
        switch(directionString){
            case "UP":
                newPosY--;
                break;
            case "DOWN":
                newPosY++;
                break;
            case "RIGHT":
                newPosX++;
                break;
            case "LEFT":
                newPosX--;
                break;
        }
        for (Point bomb:bombs) {
            if(bomb.getX() == newPosX && abs(bomb.getY() - newPosY) <= 4){
                return true;
            }
            if(bomb.getY() == newPosY && abs(bomb.getX() - newPosX) <= 4){
                return true;
            }
        }
        return false;
    }

    private String stepNormal(Board board, Point goal, Point myPosition) {
        String outDirectionString = "";

        // есль цель на этом уровне
        if(goal.getY() - myPosition.getY() == 0) {
            if(isMoveLeftRight(myPosition)){
                // если цель слева
                if(goal.getX() - myPosition.getX() < 0){
                    if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = Direction.LEFT.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else{
                        if(isMoveUpDown(myPosition)){
                            if(isPointEmpty(board,myPosition,"UP",true)){
                                outDirectionString = "ACT," + Direction.UP.toString();
                            }else if(isPointEmpty(board,myPosition,"DOWN",true)){
                                outDirectionString = "ACT," + Direction.DOWN.toString();
                            }else{
                                // всё занято
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        if(isMoveUpDown(myPosition)){
                            if(isPointEmpty(board,myPosition,"UP",true)){
                                outDirectionString = "ACT," + Direction.UP.toString();
                            }else if(isPointEmpty(board,myPosition,"DOWN",true)){
                                outDirectionString = "ACT," + Direction.DOWN.toString();
                            }else{
                                // всё занято
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }
                }
            }else{
                if(isPointEmpty(board,myPosition,"UP",true)){
                    outDirectionString = Direction.UP.toString();
                }else if(isPointEmpty(board,myPosition,"DOWN",true)){
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
                        if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                            outDirectionString = Direction.RIGHT.toString();
                        } else if (isPointEmpty(board, myPosition, "LEFT",true)) {
                            outDirectionString = "ACT," + Direction.LEFT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP",true)) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "UP",true)) {
                            outDirectionString = Direction.UP.toString();
                        } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                            outDirectionString = "ACT," + Direction.DOWN.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                // цель слева
                }else if(goal.getX() - myPosition.getX() < 0){
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "LEFT",true)) {
                            outDirectionString = Direction.LEFT.toString();
                        } else if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                            outDirectionString = "ACT," + Direction.RIGHT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP",true)) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "UP",true)) {
                            outDirectionString = Direction.UP.toString();
                        } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                            outDirectionString = "ACT," + Direction.DOWN.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"DOWN",true)){
                        outDirectionString = "ACT," + Direction.DOWN.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else if(isMoveUpDown(myPosition)){
                if(isPointEmpty(board,myPosition,"UP",true)){
                    // если клетка свободная, то идемм верх
                    outDirectionString = Direction.UP.toString();
                }else{
                    // если она занята, то проверяем
                    if(isPointEmpty(board,myPosition,"DOWN",true)){
                        outDirectionString = "ACT," + Direction.DOWN.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else{
                if(goal.getX() - myPosition.getX() > 0) {
                    if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                        outDirectionString = Direction.RIGHT.toString();
                    } else if (isPointEmpty(board, myPosition, "LEFT",true)) {
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }else{
                    if (isPointEmpty(board, myPosition, "LEFT",true)) {
                        outDirectionString = Direction.LEFT.toString();
                    } else if (isPointEmpty(board, myPosition, "RIGHT",true)) {
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
                        if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                            outDirectionString = Direction.RIGHT.toString();
                        } else if (isPointEmpty(board, myPosition, "LEFT",true)) {
                            outDirectionString = "ACT," + Direction.LEFT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP",true)) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "DOWN",true)) {
                            outDirectionString = Direction.DOWN.toString();
                        } else if (isPointEmpty(board, myPosition, "UP",true)) {
                            outDirectionString = "ACT," + Direction.UP.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                // цель слева
                }else if(goal.getX() - myPosition.getX() < 0){
                    if(isMoveLeftRight(myPosition)) {
                        if (isPointEmpty(board, myPosition, "LEFT",true)) {
                            outDirectionString = Direction.LEFT.toString();
                        } else if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                            outDirectionString = "ACT," + Direction.RIGHT.toString();
                        } else {
                            if (isMoveUpDown(myPosition)) {
                                if (isPointEmpty(board, myPosition, "UP",true)) {
                                    outDirectionString = Direction.UP.toString();
                                } else if (isPointEmpty(board, myPosition, "DOWN",true)) {
                                    outDirectionString = "ACT," + Direction.DOWN.toString();
                                } else {
                                    outDirectionString = Direction.ACT.toString();
                                }
                            } else {
                                outDirectionString = Direction.ACT.toString();
                            }
                        }
                    }else{
                        if (isPointEmpty(board, myPosition, "DOWN",true)) {
                            outDirectionString = Direction.DOWN.toString();
                        } else if (isPointEmpty(board, myPosition, "UP",true)) {
                            outDirectionString = "ACT," + Direction.UP.toString();
                        } else {
                            outDirectionString = Direction.ACT.toString();
                        }
                    }
                }else{
                    if(isPointEmpty(board,myPosition,"UP",true)){
                        outDirectionString = "ACT," + Direction.UP.toString();
                    }else if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else{
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else if(isMoveUpDown(myPosition)){
                if(isPointEmpty(board,myPosition,"DOWN",true)){
                    // если клетка свободная, то идемм верх
                    outDirectionString = Direction.DOWN.toString();
                }else{
                    // если она занята, то проверяем
                    if(isPointEmpty(board,myPosition,"RIGHT",true)){
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    }else if(isPointEmpty(board,myPosition,"LEFT",true)){
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    }else if(isPointEmpty(board,myPosition,"UP",true)){
                        outDirectionString = "ACT," + Direction.UP.toString();
                    }else{
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }
            }else{
                if(goal.getX() - myPosition.getX() < 0) {
                    if (isPointEmpty(board, myPosition, "LEFT",true)) {
                        outDirectionString = Direction.LEFT.toString();
                    } else if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                        outDirectionString = "ACT," + Direction.RIGHT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
                }else{
                    if (isPointEmpty(board, myPosition, "RIGHT",true)) {
                        outDirectionString = Direction.RIGHT.toString();
                    } else if (isPointEmpty(board, myPosition, "LEFT",true)) {
                        outDirectionString = "ACT," + Direction.LEFT.toString();
                    } else {
                        // всё занято
                        outDirectionString = Direction.ACT.toString();
                    }
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
        if(isPointEmpty(board,myPosition,direction.toUpperCase(),true)) {
            newDirection = getDirection(direction.toUpperCase());
            lastDirection = newDirection;
            outDirectionString = newDirection.toString();
        }
        else if(isPointEmpty(board,myPosition,reverseDirection(direction.toUpperCase()).toString(),true)){
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

    public boolean isPointEmpty(Board board,Point point,String direction, boolean withWall){

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
        if(withWall) {
            if (point.getX() <= 0
//            || point.getX() >= sqrt(board.size())
                    || point.getX() >= board.size() - 1 //32 //board.size()
                    || point.getY() <= 0
//            || point.getY() >= sqrt(board.size())
                    || point.getY() >= board.size() - 1 //32 //board.size()
                    // TODO - проверить, возможно, при выполнении этого условия будет ставить бомбы перед перегородками
                    || (point.getX() % 2 == 0 && point.getY() % 2 == 0)) {
                isPointDanger = true;
            }
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
        int minLenWay = 4;
        switch (typeGoal.toUpperCase()){
            case "DESTROYWALL":
                for(Point destroyWall: destroyWalls){
                    int lengthToGoal = abs(myPosition.getX() - destroyWall.getX()) + abs(myPosition.getY() - destroyWall.getY());
                    if(lengthToGoal < lengthWay
                            && lengthToGoal >= minLenWay
//                                && destroyWall.getX() != myPosition.getX()
//                                    && destroyWall.getY() != myPosition.getY()
                            ){
                        lengthWay = lengthToGoal;
                        goal = destroyWall;
                    }
                }
                break;
            default:
        }

        return goal;
    }

}
