package hu.oe.nik.szfmv17t.physics;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;


public class BrakeTest {

	private String mbd = "MAX_BRAKING_DECELERATION";

	@Test
	public void calculateAccelerationReturns0(){
		Brake brakeTest = new Brake();
		double pedalPercentage = 0;
		
		double result = brakeTest.calculateAcceleration(pedalPercentage);
		
		assertEquals(0, result, 0);
	}
	
	
	@Test
	public void calculateAccelerationReturnsMax(){
		Brake brakeTest = new Brake();
		double pedalPercentage = 1;
		Field maxDeceleration = null;
		try {
			maxDeceleration = Brake.class.getDeclaredField(mbd);
			maxDeceleration.setAccessible(true);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double expected = 0.0;
		try {
			expected = maxDeceleration.getDouble(brakeTest);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double result = brakeTest.calculateAcceleration(pedalPercentage);
		
		assertEquals(expected, result, 0);
	}
	
	@Test
	public void calculateAccelerationReturns50Percent(){
		Brake brakeTest = new Brake();
		double pedalPercentage = 0.5;
		Field maxDeceleration = null;
		try {
			maxDeceleration = Brake.class.getDeclaredField(mbd);
			maxDeceleration.setAccessible(true);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double expected = 0.0;
		try {
			expected = maxDeceleration.getDouble(brakeTest) * pedalPercentage;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double result = brakeTest.calculateAcceleration(pedalPercentage);
		
		assertEquals(expected, result, 0);
	}
	
	@Test
	public void calculateAccelerationBiggerReturnsBigger(){
		Brake brakeTest = new Brake();
		double pedalPercentageFirst = 0.8;
		double pedalPercentageSecond = 0.4;
		
		double resultFirst = brakeTest.calculateAcceleration(pedalPercentageFirst);
		double resultSecond = brakeTest.calculateAcceleration(pedalPercentageSecond);
		
		assertTrue(resultFirst < resultSecond); //negatív
	}
}
