package edu.pitt.battleshipgame.common.ships;

import edu.pitt.battleshipgame.common.board.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CarrierTest {

	private static Carrier c;

	@Before
	public void setup () 
		{ c = new Carrier(new Coordinate("A:1"),new Coordinate("A:5"),new Board("bizzord")); }

	@Test
	public void getLengthTest()
		{ assertEquals(c.getLength(), 5); }
	
	@Test
	public void maxAllowedTest()
		{ assertEquals(c.maxAllowed(), 1); }
	
	@Test
	public void getNameTest()
		{ assertEquals(c.getName(), "Carrier"); }
	
	@Test
	public void getTypeTest()
		{ assertEquals(c.getType(), Ship.ShipType.CARRIER); }

}