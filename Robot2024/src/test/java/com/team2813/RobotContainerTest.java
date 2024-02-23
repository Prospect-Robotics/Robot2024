package com.team281;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.team2813.RobotContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RobotContainerTest {
    @Test
    public void constructorDoesNotRaise() {
        new RobotContainer();
    }
}
