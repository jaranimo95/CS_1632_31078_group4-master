package edu.pitt.battleshipgame.common;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.battleshipgame.common.board.Board;

public class GameTracker {
    public static final int MAX_PLAYERS = 2;
    private int registeredPlayers = 0;
    private ArrayList<Board> gameBoards;
    private GameState state = GameState.INIT;
    private int playerTurn = 0;
    private boolean quitter = false;
    Object lock;

    public GameState getState(){return state;}
    
    public GameTracker() {
        // Exists to protect this object from direct instantiation
        lock = new Object();
        gameBoards = new ArrayList<Board>(MAX_PLAYERS);
        System.out.println("Server constructed.");
    }

    public int registerPlayer() {
        synchronized(lock) {
            registeredPlayers++;
            gameBoards.add(new Board("Player " + (registeredPlayers - 1) + " board"));
        }
        return registeredPlayers - 1;
    }

    public String exitAfterQuit(){
        String retstr = null;
        switch(state){
            case INIT:{
                if(quitter){
                    retstr  = "Sorry, your opponent quit during the setup phase. Please play again!";
                }
                else{
                    retstr = "Please play again!";
                }
                break;
            }

            case PLAYING:{
                if(quitter){
                    retstr = "Your opponent surrendered the game, you win!";
                }
                else{
                    retstr = "Please play again!";
                }
            }
        }

        quitter = !quitter;
        return retstr;
    }

    public void wait(int playerID) {
        switch (state) {
            case INIT:
            {
                System.out.println("Player " + playerID + " is waiting for other players");
                while(registeredPlayers < MAX_PLAYERS) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                state = GameState.PLAYING;
                if(playerID%2==0) System.out.println("A game has begun");
                break;
            }
            case PLAYING:
            {
                while(playerTurn != playerID) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println(e + " I can't sleep!");
                    }
                }
                break;
            }
            default:
                break;
        }
    }
    
    public List<Board> getBoards() {
        return gameBoards;
    }
    
    public void setBoards(ArrayList<Board> boards) {

        for(int i = 0; i<MAX_PLAYERS; i++){
            if(boards.get(i).getShipList().size() == 5)gameBoards.set(i, boards.get(i));
        }
        playerTurn = (playerTurn + 1) % registeredPlayers;
    }
    
    public boolean isGameOver() {
        System.out.println("Checking if the game is over...");
        for(Board board : gameBoards) {
            if(board.areAllShipsSunk()) {
                return true;
            }
        }
        return false;
    }
}