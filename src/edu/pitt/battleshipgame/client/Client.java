package edu.pitt.battleshipgame.client;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import edu.pitt.battleshipgame.common.board.*;
import edu.pitt.battleshipgame.common.ships.*;
import edu.pitt.battleshipgame.common.GameTracker;
import static java.lang.Math.abs;

public class Client {

    public static ClientWrapper gi;
    public static int myPlayerID;
    public static ArrayList<Board> gameBoards;
    public static Scanner scan = new Scanner(System.in);
    public static boolean started = false;

    public static void main(String[] args) {
        gi = new ClientWrapper();
        myPlayerID = gi.registerPlayer();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!gi.isGameOver()) {
                        handleQuit();
                    }
                } catch (Exception e) {
                    System.out.println("The server crashed! Please try again shortly");
                }
            }
        });

        System.out.println("You have registered as Player " + myPlayerID);
        System.out.println("Please wait for other players to join");
        gi.wait(myPlayerID);
        System.out.println("Both Players have joined, starting the game.");
        gameBoards = gi.getBoards();
        placeShips(gameBoards.get(myPlayerID));
        System.out.println("Your board:");
        System.out.println(gameBoards.get(myPlayerID).toString(true));
        gi.setBoards(gameBoards);
        gameLoop();
    }

    public static void placeShips(Board board) {
        System.out.println("Your Board:");

        //Removed this string just to avoid having the board double post
        //System.out.println(board.toString(true));
        for (Ship.ShipType type : Ship.ShipType.values()) {
            if (type != Ship.ShipType.NONE) {
                boolean valid1 = false;
                boolean valid2 = false;
                boolean correctSpaces = false;
                boolean noOverlap = false;
                String coord1 = null;
                String coord2 = null;
                Coordinate start = new Coordinate();
                Coordinate end = new Coordinate();
                while (!correctSpaces || !noOverlap) {
                    while (!(valid1 && valid2)) {
                        System.out.println(gameBoards.get(myPlayerID).toString(true));
                        System.out.println("Please enter a start coordinate to place your " + ShipFactory.getNameFromType(type));
                        coord1 = scan.nextLine();
                        valid1 = start.setCoordinates(coord1);

                        if (valid1) {
                            System.out.println("Please enter an end coordinate to place your " + ShipFactory.getNameFromType(type));
                            coord2 = scan.nextLine();
                            valid2 = end.setCoordinates(coord2);
                        }
                    }
                    while (!valid2) {
                        System.out.println("Please enter an end coordinate to place your " + ShipFactory.getNameFromType(type));
                        coord2 = scan.nextLine();
                        valid2 = end.setCoordinates(coord2);
                    }
                    correctSpaces = start.checkShipPlacement(ShipFactory.getNameFromType(type), coord1, coord2);
                    //makes sures ship doesn't overlap another ship during placement
                    noOverlap = checkOverlap(start, end);

                    if (!correctSpaces || !noOverlap) {
                        valid1 = false;
                        valid2 = false;
                    }
                }
                System.out.println(type + " placed at " + coord1 + " to " + coord2);
                // We don't need to track a reference to the ship since it will be
                // on the board.
                ShipFactory.newShipFromType(type, start, end, board);
            }
        }
    }

    private static boolean checkOverlap(Coordinate start, Coordinate end) {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();
        final int BOARD_DIM = 10;
        int shipCoord[];
        int diff;
        boolean noOverlap = true;
        boolean beginning = false;
        boolean middle = false;
        boolean last = false;

        Ship[][] theShips = gameBoards.get(myPlayerID).getShipLoc();

        //Check whether it's horizontal or vertical
        //If success, horizontal
        if (startRow == endRow) {
            diff = abs(endCol - startCol);

            //Creates and enumerates an array of coordinate values
            shipCoord = new int[diff];
            for (int i = 0; i < diff; i++) {
                shipCoord[i] = startCol + i;
            }
            //Scans through board to find where ships are placed and 
            //checks if the new ship will overlap or not. If so, noOverlap = false
            for (int row = 0; row < BOARD_DIM; row++) {
                for (int col = 0; col < BOARD_DIM; col++) {
                    //Checks if a ship exists at each spot
                    if (theShips[col][row] != null) {
                        //Iterates through shipCoord's array to check if coordinates are equal
                        for (int x = 0; x < diff; x++) {
                            if (startRow == row) {
                                if (shipCoord[x] == col) {
                                    noOverlap = false;

                                    //Checks where the ship overlaps
                                    if (x == 0) {
                                        beginning = true;
                                    } else if (x == diff - 1) {
                                        last = true;
                                    } else {
                                        middle = true;
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } //if success, vertical
        else if (startCol == endCol) {
            diff = abs(endRow - startRow);

            //Creates and enumerates an array of coordinate values
            shipCoord = new int[diff];
            for (int i = 0; i < diff; i++) {
                shipCoord[i] = startRow + i;
            }

            //Scans through board to find where ships are placed and 
            //checks if the new ship will overlap or not. If so, noOverlap = false
            for (int row = 0; row < BOARD_DIM; row++) {
                for (int col = 0; col < BOARD_DIM; col++) {
                    //Checks if a ship exists at each spot
                    if (theShips[col][row] != null) {
                        //Iterates through shipCoord's array to check if coordinates are equal
                        for (int x = 0; x < diff; x++) {
                            if (startCol == col) {
                                if (shipCoord[x] == row) {
                                    noOverlap = false;

                                    //Checks where the ship overlaps
                                    if (x == 0) {
                                        beginning = true;
                                    } else if (x == diff - 1) {
                                        last = true;
                                    } else {
                                        middle = true;
                                    }

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("It's diagonal. This is a bug.");
        }

        if (!noOverlap) {
            System.out.println("Error: There is an overlap of ships.");
            if (beginning) {
                System.out.println("Overlap at the beginning of the ship.");
            }
            if (middle) {
                System.out.println("Overlap somewhere in the middle of ship.");
            }
            if (last) {
                System.out.println("Overlap at the end of the ship.");
            }
            System.out.println("Your ship may not be placed here. Please try again.");
        }

        return noOverlap;
    }

    public static void gameLoop() {
        boolean setCoord = false;
        ArrayList usedCoordinates = new ArrayList(100);
        ArrayList hitCoordinates = new ArrayList(17);
        
        System.out.println("The game is starting!");
        do {
            // Wait for our turn
            gi.wait(myPlayerID);

            // Get the updated boards
            gameBoards = gi.getBoards();
            System.out.println("Where would you like to place your move?");
            System.out.println("Used coordinates are: " + usedCoordinates.toString());
            System.out.println("Hit coordinates are: " + hitCoordinates.toString());
            String theMove = scan.nextLine();
            String[] coordinates = theMove.split(":");
            Coordinate move = new Coordinate();
            setCoord = move.setCoordinates(theMove);
            if (setCoord) {
                int coord2 = Integer.parseInt(coordinates[1]);
                String finCoord = coordinates[0].toUpperCase() + String.valueOf(coord2);
                setCoord = checkDuplicateCoords(usedCoordinates, finCoord);
            }

            //Makes sure the attack input is valid
            while (!setCoord) {
                System.out.println("Invalid coordinate. Please try again.");
                System.out.println("Where would you like to place your move?");
                System.out.println("Used coordinates are: " + usedCoordinates.toString());
                System.out.println("Hit coordinates are: " + hitCoordinates.toString());

                theMove = scan.nextLine();
                setCoord = move.setCoordinates(theMove);
                if (setCoord) {
                    coordinates = theMove.split(":");
                    int coord2 = Integer.parseInt(coordinates[1]);
                    String finCoord = coordinates[0].toUpperCase() + String.valueOf(coord2);
                    setCoord = checkDuplicateCoords(usedCoordinates, finCoord);
                }
            }

            Ship ship = gameBoards.get((myPlayerID + 1) % GameTracker.MAX_PLAYERS).makeMove(move);
            if (ship == null) {
                System.out.println("Miss");
            } else if (ship.isSunk()) {
                int coord2 = Integer.parseInt(coordinates[1]);
                String finCoord = coordinates[0].toUpperCase() + String.valueOf(coord2);
                hitCoordinates.add(finCoord);
                System.out.println("You sunk " + ship.getName());
            } else {
                int coord2 = Integer.parseInt(coordinates[1]);
                String finCoord = coordinates[0].toUpperCase() + String.valueOf(coord2);
                hitCoordinates.add(finCoord);
                System.out.println("Hit");
            }
            // Send the updated boards.
            gi.setBoards(gameBoards);

            //Uncomment this once we get the board to properly 
            //update each time to let the user know what they just fired
            //System.out.println(gameBoards.get(myPlayerID).toString(true));
        } while (!gi.isGameOver());

        Board otherPersonBoard = gi.getBoards().get((myPlayerID + 1) % GameTracker.MAX_PLAYERS);
        Board myBoard = gi.getBoards().get((myPlayerID) % GameTracker.MAX_PLAYERS);

        if (otherPersonBoard.areAllShipsSunk()) {
            System.out.println("You won!");
        } else if (myBoard.areAllShipsSunk()) {
            System.out.println("You lost...");
        }
        System.out.println("The Game is Over!");
    }

    /**
     * Helper method that checks if coordinates have been used already.
     *
     * @param array ArrayList of coordinates used
     * @param finCoord Inputted coordinate to check
     * @return True if good coordinate, false if it already exits
     */
    private static boolean checkDuplicateCoords(ArrayList array, String finCoord) {
        if (array.contains(finCoord)) {
            System.out.println("This coordinate has already been fired upon.");
            return false;
        } else {
            array.add(finCoord);
        }
        return true;
    }

    public static void handleQuit() {
        System.out.println(gi.serverInterface.exitAfterQuit());
    }
}
