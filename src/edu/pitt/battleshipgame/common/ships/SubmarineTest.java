package edu.pitt.battleshipgame.common.ships;

import edu.pitt.battleshipgame.common.board.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SubmarineTest {

	private static Submarine c;

	@Before
	public void setup () 
		{ c = new Submarine(new Coordinate("A:1"),new Coordinate("A:5"),new Board("bizzord")); }

	@Test
	public void getLengthTest()
		{ assertEquals(c.getLength(), 3); }
	
	@Test
	public void maxAllowedTest()
		{ assertEquals(c.maxAllowed(), 1); }
	
	@Test
	public void getNameTest()
		{ assertEquals(c.getName(), "Submarine"); }
	
	@Test
	public void getTypeTest()
		{ assertEquals(c.getType(), Ship.ShipType.SUBMARINE); }

}