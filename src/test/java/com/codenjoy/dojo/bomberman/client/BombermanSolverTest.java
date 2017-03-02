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
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class BombermanSolverTest {

    private Dice dice = mock(Dice.class);
    private final YourSolver solver = new YourSolver(dice);

    private void assertA(String board, String direction) {
        assertEquals(direction.toString(), solver.get((Board) new Board().forString(board)));
    }
    private void assertB(String board, Direction direction) {
        assertEquals(direction.toString(), solver.get((Board) new Board().forString(board)));
    }


    @Test
    public void stepUp1() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" + // 0
                        "☼    #☼" + // 1
                        "☼ ☼ ☼ ☼" + // 2
                        "☼  ☺  ☼" + // 3
                        "☼ ☼ ☼ ☼" + // 4
                        "☼     ☼" + // 5
                        "☼☼☼☼☼☼☼",  // 6
                Direction.UP);
    }

    @Test
    public void stepUp2() {
                         // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼    #☼" +
                        "☼ ☼☺☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp3() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼#    ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp4() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼#    ☼" +
                        "☼ ☼☺☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp5() {
        // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼  #  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp6() {
        // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼#☼☺☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp7() {
        // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼☺☼#☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }
///////////////////////////////////////////////////////////////
    @Test
    public void stepDown1() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼#    ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.DOWN);
    }
    @Test
    public void stepDown2() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼☺☼ ☼" +
                        "☼#    ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.DOWN);
    }
    @Test
    public void stepDown3() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼    #☼" +
                        "☼☼☼☼☼☼☼",
                Direction.DOWN);
    }
    @Test
    public void stepDown4() {
                      // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼☺☼ ☼" +
                        "☼    #☼" +
                        "☼☼☼☼☼☼☼",
                Direction.DOWN);
    }
    @Test
    public void stepDown5() {
        // 0123456
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  #  ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.DOWN);
    }
///////////////////////////////////////////////////////////////
    @Test
    public void stepRight1() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼    #☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼ ☺   ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight2() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼    #☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   ☺ ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight3() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼ ☺   ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼    #☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight4() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   ☺ ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼    #☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight5() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺ #☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight6() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   ☺ ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   # ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight7() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼#☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight8() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼#☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }
    ///////////////////////////////////////////////////////////////
    @Test
    public void stepLeft1() {
        assertB("☼☼☼☼☼☼☼" +
                      "☼     ☼" +
                      "☼ ☼ ☼ ☼" +
                      "☼# ☺  ☼" +
                      "☼ ☼ ☼ ☼" +
                      "☼     ☼" +
                      "☼☼☼☼☼☼☼",
                Direction.LEFT);
    }

    @Test
    public void stepLeft2() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼  ☺  ☼" +
                        "☼#☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.LEFT);
    }

    @Test
    public void stepLeft3() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼#☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.LEFT);
    }
///////////////////////////////////////////////////////////////
    @Test
    public void stepAct_Down1() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼#☼" +
                        "☼    ☺☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,DOWN");
    }

    @Test
    public void stepAct_Down2() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼#☼ ☼ ☼" +
                        "☼☺    ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,DOWN");
    }

    @Test
    public void stepAct_Down3() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼ #☺# ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,DOWN");
    }

    @Test
    public void stepAct_Down4() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼   #☺☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,DOWN");
    }

    @Test
    public void stepAct_Down5() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼☺#   ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,DOWN");
    }
///////////////////////////////////////////////////////////////
    @Test
    public void stepAct_Up1() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼    ☺☼" +
                        "☼ ☼ ☼#☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up2() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼☺    ☼" +
                        "☼#☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up3() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼ #☺# ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up4() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼☺#   ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up5() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   #☺☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up6() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼☺#   ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }

    @Test
    public void stepAct_Up7() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   #☺☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,UP");
    }
///////////////////////////////////////////////////////////////

    @Test
    public void stepAct_Right1() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼#☺   ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,RIGHT");
    }
    @Test
    public void stepAct_Right2() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼#☺   ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,RIGHT");
    }
///////////////////////////////////////////////////////////////

    @Test
    public void stepAct_Left1() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼   ☺#☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,LEFT");
    }
    @Test
    public void stepAct_Left2() {
        assertA(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼   ☺#☼" +
                        "☼☼☼☼☼☼☼",
                "ACT,LEFT");
    }
    ////////////////////////////////////////////////////////
//        ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼
//        ☼   #                      #  ##☼
//        ☼ ☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼
//        ☼           #     # #      ##  #☼
//        ☼#☼ ☼ ☼ ☼ ☼#☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
//        ☼#       &   ##4☺   #  ##       ☼
//        ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼

// UP - DOWN - циклится - решено путем введение счетчика ходов без взрывов
// ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼
// ☼                          # ## ☼
// ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼
// ☼     #&     #     #&#      #☺##☼
// ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼#☼ ☼ ☼#☼ ☼#☼ ☼


// LEFT-RIGHT
// My Goal: [28,3]
//☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
//☼       &  &          #     #   ☼
//☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼
//☼                       #   ☺   ☼
//☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼
}
