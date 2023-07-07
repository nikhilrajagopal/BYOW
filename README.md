# BYOW
This is an ASCII game implementation in Java. The game allows players to explore a randomly generated world using ASCII characters. It features rooms, corridors, and a player character that can navigate through the world.

## Features

- Randomly generated world: The game generates a random world consisting of rooms and corridors each time it is run. This ensures a unique gameplay experience with every playthrough.

- Room and corridor generation: The world is populated with rooms and corridors. Rooms are rectangular areas with a specified width and height, while corridors connect the rooms.

- Player navigation: Players can control a character using keyboard inputs to move around the world, explore rooms, and traverse the corridors.

## Getting Started

To run the game, follow these steps:

1. Clone the repository to your local machine.

2. Open the project in your preferred Java development environment.

3. Compile and run the `Main` class located in the `byow.Core` package.

4. If you want to play with keyboard inputs, simply run the program without any command-line arguments. Use the arrow keys to navigate the world.

5. If you want to provide a specific world layout, you can pass a seed string as a command-line argument. For example, `java byow.Core.Main -s "randomseed"` will generate the world using the specified seed.

## Dependencies

This project has no external dependencies. It is implemented purely in Java using standard libraries.

## Code Structure

The project consists of the following main classes:

- `Main`: The main entry point of the program. It parses the command-line inputs and initializes the game engine accordingly.

- `Engine`: The game engine that handles the game logic and interaction with the user. It generates the world, processes user inputs, and updates the game state accordingly.

- `Position`: A utility class representing a position in the world grid.

- `Room`: A class representing a room in the game world. It stores information such as position, dimensions, connectivity, and center coordinates.
