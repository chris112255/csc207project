package test.entity;

import entity.Nutrients;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NutrientsTest {

    @Test
    void testConstructorAndGetters() {
        Nutrients nutrients = new Nutrients(500, 30.5, 15.2, 10.8, 200.3, 45.7);

        assertEquals(500, nutrients.getCalories());
        assertEquals(30.5, nutrients.getProtein(), 0.001);
        assertEquals(15.2, nutrients.getFat(), 0.001);
        assertEquals(10.8, nutrients.getSugar(), 0.001);
        assertEquals(200.3, nutrients.getSodium(), 0.001);
        assertEquals(45.7, nutrients.getCarbs(), 0.001);
    }

    @Test
    void testRoundingInConstructor() {
        Nutrients nutrients = new Nutrients(500, 30.555, 15.222, 10.888, 200.333, 45.777);

        assertEquals(30.56, nutrients.getProtein(), 0.001);
        assertEquals(15.22, nutrients.getFat(), 0.001);
        assertEquals(10.89, nutrients.getSugar(), 0.001);
        assertEquals(200.33, nutrients.getSodium(), 0.001);
        assertEquals(45.78, nutrients.getCarbs(), 0.001);
    }

    @Test
    void testNullUrl() {
        Nutrients nutrients = new Nutrients(500, 30, 15, 10, 200, 45);
        assertNull(nutrients.getUrl());
    }
}