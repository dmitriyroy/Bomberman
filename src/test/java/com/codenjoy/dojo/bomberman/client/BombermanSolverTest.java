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

    private void assertB(String board, Direction direction) {
        assertEquals(direction.toString(), solver.get((Board) new Board().forString(board)));
    }

    @Test
    public void stepDown1() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                      "☼☺        # # ☼" +
                      "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                      "☼##           ☼" +
                      "☼ ☼ ☼#☼ ☼ ☼ ☼ ☼" +
                      "☼   #    # #  ☼" +
                      "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                      "☼             ☼" +
                      "☼#☼ ☼ ☼#☼ ☼ ☼#☼" +
                      "☼  #  #       ☼" +
                      "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                      "☼ ##      #   ☼" +
                      "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                      "☼ #   #  &    ☼" +
                      "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼",
                Direction.DOWN);
    }

    @Test
    public void stepUp1() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                       "☼         # # ☼" +
                       "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                       "☼##           ☼" +
                       "☼ ☼ ☼#☼ ☼ ☼ ☼ ☼" +
                       "☼☺  #    # #  ☼" +
                       "☼ ☼ ☼ ☼#☼ ☼ ☼ ☼" +
                       "☼             ☼" +
                       "☼#☼ ☼ ☼#☼ ☼ ☼#☼" +
                       "☼  #  #       ☼" +
                       "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                       "☼ ##      #   ☼" +
                       "☼ ☼ ☼ ☼ ☼ ☼ ☼#☼" +
                       "☼ #   #  &    ☼" +
                       "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp2() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼☺☼#☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }

    @Test
    public void stepUp3() {
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
    public void stepUp4() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼#☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼  ☺  ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.UP);
    }


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
    public void stepRight1() {
        assertB("☼☼☼☼☼☼☼" +
                      "☼     ☼" +
                      "☼ ☼ ☼ ☼" +
                      "☼  ☺ #☼" +
                      "☼ ☼ ☼ ☼" +
                      "☼     ☼" +
                      "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

    @Test
    public void stepRight2() {
        assertB(  "☼☼☼☼☼☼☼" +
                        "☼  ☺  ☼" +
                        "☼ ☼ ☼#☼" +
                        "☼     ☼" +
                        "☼ ☼ ☼ ☼" +
                        "☼     ☼" +
                        "☼☼☼☼☼☼☼",
                Direction.RIGHT);
    }

}
