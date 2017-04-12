package edu.pitt.battleshipgame.common.board;
import edu.pitt.battleshipgame.common.ships.*;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;
import java.io.Serializable;

public class BoardTest {
	
	private Board b;
	LinkedList<Ship> sList;
	
	@Before
	public void setup() {
	   // Check if board is null
		b = new Board("bizz");
		assert(b != null);

	   // Fill board with all 5 ships
		sList = new LinkedList<Ship>();
		sList.add(new Carrier    (new Coordinate("A:1"),new Coordinate("A:5"),b));
		sList.add(new Battleship (new Coordinate("B:1"),new Coordinate("B:4"),b));
		sList.add(new Cruiser    (new Coordinate("C:1"),new Coordinate("C:3"),b));
		sList.add(new Submarine  (new Coordinate("D:1"),new Coordinate("D:3"),b));
		sList.add(new Destroyer  (new Coordinate("E:1"),new Coordinate("E:2"),b));
	}

	@Test
	public void getNameTest() {
		assertEquals(b.getName(),"bizz");	// Make sure board has same name as we initialized it with
	}

	@Test
	public void addShipTest() {
		for(int i = 0; i < 5; i++) 
			assertEquals(b.shipList.get(i),sList.get(i));	// Make sure all ships we added were successfully placed on the board
	}

	@Test
	public void makeMoveTest() {
		boolean [][] moves = new boolean[2][2];
		moves[0][0] = true;	 // Set one spot on fake board to "hit"
		moves[0][1] = false; // Set the other to "miss"

		Coordinate hit = new Coordinate("A:1");
		Coordinate miss = new Coordinate("A:2");

		assertTrue(moves[hit.getRow()][hit.getCol()]);
		assertFalse(moves[miss.getRow()][miss.getCol()]);
	}

	@Test
	public void canShipFitTest() {
		for(int i = 0; i < 5; i++)
			assertFalse(b.canShipFit(sList.get(i)));	// Should be false since ships have already been placed in setup()
	}

	@Test
	public void getsListTest() {
		for(int i = 0; i < 5; i++) 
			assertEquals(b.shipList.get(i),sList.get(i));	// Make sure all the ships we added were also added to sList
	}

	@Test
	public void getShipLocTest() {
		Ship[][] shipLocs = b.getShipLoc();

		for(int i = 0; i < 5; i++)
			for(int j = 0; j < sList.get(i).getLength(); j++)
				assertEquals(shipLocs[i][j].getName(),sList.get(i).getName());
	}

	@Test
	public void areAllShipsSunk() {
	   // Make sure all ships aren't sunk	
		for(int i = 0; i < 5; i++)
			assertFalse(sList.get(i).isSunk());

	   // Now sink all ships
		for(int i = 0; i < 5; i++)
			for(int j = 0; j < sList.get(i).getLength(); j++)
				sList.get(i).registerHit();

	   // Finally, make sure all ships are sunk
		for(int i = 0; i < 5; i++)
			assertTrue(sList.get(i).isSunk());
	}

	@Test
	public void toStringTest() {
		String boardString = "+ABCDEFGHIJ\n
							  1..........\n
							  2..........\n
							  3..........\n
							  4..........\n
							  5..........\n
							  6..........\n
							  7..........\n
							  8..........\n
							  9..........\n
							  1..........\n";
		assertEquals(b.toString(),boardString);
	}
}