package com.target;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class BarrenLandAnalysisTest {


    @Test
    public void testWhenNoBarrenLand() {
        BarrenLandAnalysis subject = new BarrenLandAnalysis(400, 600, "{}");
        List<Integer> landAreas = subject.findFertileLand();
        int[] expectedResult = new int[]{240000};
        assertThat(landAreas.toArray(), is(expectedResult));

        subject = new BarrenLandAnalysis(400, 600, "");
        landAreas = subject.findFertileLand();
        assertThat(landAreas.toArray(), is(expectedResult));

        subject = new BarrenLandAnalysis(400, 600, null);
        landAreas = subject.findFertileLand();
        assertThat(landAreas.toArray(), is(expectedResult));

    }

    @Test
    public void testWhenNoFertileLand() {
        BarrenLandAnalysis subject = new BarrenLandAnalysis(40, 60, "{”0 0 39 59”}");

        // subject.printGrid();
        List<Integer> landAreas = subject.findFertileLand();

        int[] expectedResult = new int[]{};
        assertThat(landAreas.toArray(), is(expectedResult));


        subject = new BarrenLandAnalysis(400, 600, "{”0 0 399 599”}");
        landAreas = subject.findFertileLand();
        assertThat(landAreas.toArray(), is(expectedResult));

    }


    @Test
    public void testSample1() {
        String input = "”0 292 399 307”";
        BarrenLandAnalysis subject = new BarrenLandAnalysis(400, 600, input);
        System.out.printf("Input String =  %s%n", input);
        List<Integer> landAreas = subject.findFertileLand();
        System.out.print("Fertile land areas =");
        int[] expectedResult = new int[]{116800, 116800};
        System.out.println(landAreas);
        assertThat(landAreas.size(), is(2));
        assertThat(landAreas.toArray(), is(expectedResult));
    }

    @Test
    public void testSample2() {

        String input = "{“48 192 351 207”, “48 392 351 407”, “120 52 135 547”, “260 52 275 547”}";
        BarrenLandAnalysis subject = new BarrenLandAnalysis(400, 600, input);

        System.out.printf("Input String =  %s%n", input);

        List<Integer> landAreas = subject.findFertileLand();
        int[] expectedResult = new int[]{22816, 192608};
        System.out.print("Fertile land areas =");
        System.out.println(landAreas);
        assertThat(landAreas.size(), is(2));
        assertThat(landAreas.toArray(), is(expectedResult));

    }

    @Test
    public void testSmallAreaWithOneBarrenLand() {

        String input = "{“1 1 3 3”}";
        BarrenLandAnalysis subject = new BarrenLandAnalysis(5, 5, input);
        System.out.println("Land Given");
        subject.printGrid();
        List<Integer> landAreas = subject.findFertileLand();
        int[] expectedResult = new int[]{16};
        System.out.println("After finding Fertile Land Area");
        subject.printGrid();

        System.out.println("Fertile Land Areas:" + landAreas);
        assertThat(landAreas.size(), is(1));
        assertThat(landAreas.toArray(), is(expectedResult));
    }

    @Test
    public void testWith2AreasWithSmallArea() {

        String input = "{“0 2 4 3” , “0 6 7 8” }";
        BarrenLandAnalysis subject = new BarrenLandAnalysis(8, 10, input);
        System.out.println("Land Given");
        subject.printGrid();
        System.out.println("After finding Fertile Land Area");
        List<Integer> landAreas = subject.findFertileLand();
        int[] expectedResult = new int[]{8, 38};

        subject.printGrid();
        System.out.print("fertile landAreas = ");
        System.out.println(landAreas);
        assertThat(landAreas.size(), is(2));
        assertThat(landAreas.toArray(), is(expectedResult));
    }

    @Test(expected = AssertionError.class)
    public void testInvalidRectangleThrowsException() {
        String input = "{“0 0 0 1”, “3 4 4 3”}";
        try {
            new BarrenLandAnalysis(5, 5, input);
        } catch (AssertionError ae) {
            String expectedMessage = "Can not form a rectangle with (x=0, y=0), (x=0, y=1)";
            assertThat(ae.getMessage(), is(expectedMessage));
            throw ae;
        }
        fail("AssertionError is not thrown!");
    }

    @Test(expected = AssertionError.class)
    public void testInvalidCoordinatesInRectanglesInput() {
        String input = "{“0 0 5 5”}";
        try {
            new BarrenLandAnalysis(5, 5, input);
        } catch (AssertionError ae) {
            String actualMessage = ae.getMessage();
            String expectedMessage = "(5,5) value should not be greater than (4,4) ";
            assertThat(actualMessage, CoreMatchers.equalTo(expectedMessage));
            throw ae;
        }
        fail("AssertionError is not thrown!");
    }


}


