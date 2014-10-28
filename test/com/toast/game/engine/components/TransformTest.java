package com.toast.game.engine.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.toast.game.common.CoordinatesType;
import com.toast.game.engine.property.Transform;

import java.awt.geom.Point2D;

import org.junit.Test;

public class TransformTest
{
   @Test
   public void testSetPosition()
   {
      Transform transform = new Transform();
      
      assertTrue(transform.getPosition().equals(new Point2D.Double(0.0, 0.0)));
      
      transform.setPosition(1.5, 1.5);
      assertTrue(transform.getPosition().getX() == 1.5);
      assertTrue(transform.getPosition().getY() == 1.5);
      
      transform.setPosition(new Point2D.Double(2.5, 2.5));
      assertTrue(transform.getPosition().getX() == 2.5);
      assertTrue(transform.getPosition().getY() == 2.5);
      
      Point2D.Double position = transform.getPosition();
      position.setLocation(0,  0);
      assertFalse(transform.getPosition().getX() == 0);
   }
   
   
   @Test
   public void testSetCoordsType()
   {
      Transform transform = new Transform();
      
      assertTrue(transform.getCoordsType().equals(CoordinatesType.WORLD));
      
      transform.setCoordsType(CoordinatesType.SCREEN);
      assertTrue(transform.getCoordsType() == CoordinatesType.SCREEN);
      
      CoordinatesType coordsType = transform.getCoordsType();
      coordsType = CoordinatesType.OFFSET;
      assertFalse(transform.getCoordsType().equals(coordsType));
   }
   
   
   @Test
   public void testSetScale()
   {
      Transform transform = new Transform();
      
      assertTrue(transform.getScale() == 1.0);
      
      transform.setScale(1.5);
      assertTrue(transform.getScale() == 1.5);
      
      transform.setScale(2.5);
      assertTrue(transform.getScale() == 2.5);
   }
}
