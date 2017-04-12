import pexpect

# I wasn't really able to get pexpect working on my computer, so I wrote down what I
# think is legal code that probably won't run, but I figured I'd at least try and get
# something down instead of not handing in anything at all! Hopefully it isn't too
# unclear as to what I was going for, but I'll let the comments explain that instead
# of listing everything up here.

# Will be accessed with for loops in order to automate ship placement / firing phases
# Player will access other player's position array when firing to guaruntee a hit
p1_position = ['A:1','A:5','B:1','B:4','C:1','C:3','D:1','D:3','E:1','E:2']
p2_position = ['F:10','F:6','G:10','G:7','H:10','H:8','I:10','I:8','J:10','J:9']
lengths = [1,2,3,4,5]

# Will help setup and manage an automated game via multithreading
def game_setup():
	# Create a thread to begin running the game
	thread.start_new_thread(spawn, ('java -jar dist/Client/BattleshipServer.jar'), )
	time.sleep(3) # Put main thread down for a nap

	# Now create a new thread for player 1
	thread.start_new_thread(game, (p1_position,p2_position))
	time.sleep(3) # Also put that thread down for a nap down for a nap once we create a thread for player 1
	
	# And create a new thread for player 2
	thread.start_new_thread(game, (p2_position,p1_position))

def game(ship_position,firing_position):
	game = spawn('java -jar dist/Client/BattleshipServer.jar')
	for n in range(0,5):
		if (i == 0)
			game.expect("Please enter a start coordinate to place your Carrier")
			game.sendline(ship_position[i])   # Coordinate at index 0
			game.expect("Please enter an end coordinate to place your Carrier")
			game.sendline(ship_position[i+1]) # Coordinate at index 1
		if (i == 1)
			game.expect("Please enter a start coordinate to place your Battleship")
			game.sendline(ship_position[i+1]) # Coordinate at index 2
			game.expect("Please enter an end coordinate to place your Battleship")
			game.sendline(ship_position[i+2]) # Coordinate at index 3
		if (i == 2)
			game.expect("Please enter a start coordinate to place your Cruiser")
			game.sendline(ship_position[i+2]) # Coordinate at index 4
			game.expect("Please enter an end coordinate to place your Cruiser")
			game.sendline(ship_position[i+3]) # Coordinate at index 5
		if (i == 3)
			game.expect("Please enter a start coordinate to place your Submarine")
			game.sendline(ship_position[i+3]) # Coordinate at index 6
			game.expect("Please enter an end coordinate to place your Submarine")
			game.sendline(ship_position[i+4]) # Coordinate at index 7
		if (i == 4)
			game.expect("Please enter a start coordinate to place your Destroyer")
			game.sendline(ship_position[i+4]) # Coordinate at index 8
			game.expect("Please enter an end coordinate to place your Destroyer")
			game.sendline(ship_position[i+5]) # Coordinate at index 9

	game.expect("The game is starting!")

	# For each type of ship..
	for i in range(5)
		# And then for each space the ship inhabits..
		for j in range(lengths[i])
			game.expect("Where would you like to place your move?")
			game.expect("Used coordinates are [.*]")
			game.expect("Hit coordinates are [.*]")

			game.sendline(str(firing_position[j][0])+(str(firing_position[j][1]+k)))

# And that's about as far as I was able to get. Thanks for bearing with me this shit is awful lol







