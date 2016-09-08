package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Ольга on 21.07.2016.
 */
public class PointTests {

  @Test
  public void testArea(){
    Point p1=new Point(5,5);
    Point p2=new Point(0,0);
    Assert.assertEquals(p1.distance(p2),7.071067811865475);
  }

}