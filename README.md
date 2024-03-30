# 🎲 Monopoly Simulation Project 🏠

## 📚 Introduction

The Monopoly Simulation Project is a Java-based application designed to simulate the well-known board game, Monopoly. Although Monopoly is not typically recognized for its excitement, it offers substantial educational value, particularly in implementing its rules in a software context. This project enables the exploration of various programming concepts such as threading and networking, making it an excellent tool for educational purposes.

Originating in the early 20th century by Lizzie Magie, Monopoly was designed to demonstrate the issues surrounding land ownership accumulation. For those interested in the history of Monopoly, [this video on the origins of Monopoly](https://www.youtube.com/watch?v=example) is highly recommended.

![Monopoly](https://example.com/monopoly_image.jpg)

## 🎯 Objectives

The primary goal of this project is to create a simulation of the Monopoly game that can be used for playing the game among multiple players via a network, using a command-line interface. The simulation is based on a provided `Simulation` interface, used in integration tests, to model the game accurately.

### Key Features

- **Simulation Interface**: Implementation of the `Simulation` interface to model game actions and outcomes.
- **Dice and Board Movement**: Simulation includes dice rolls for player movement and a representation of the Monopoly board based on a provided CSV file.
- **Property Transactions**: Players can buy properties, pay rent, and manage their money using a BigDecimal representation for accuracy.
- **Prison, Start Square, and Railroads**: Additional game rules including prison mechanics, collecting money on passing start, and different rent calculations based on owned railroads.
- **Network Gameplay**: Enabling multiplayer gameplay through a client-server model, allowing remote play over a network.

## 🚀 Deliverables

This project is divided into several key deliverables:

1. **Dice, Board, and Movement**: Implementing basic game mechanics including player movement and the game board.
2. **Money, Property Purchases, and Rent**: Introducing financial transactions, property ownership, and rent payment.
3. **Prison, Start Square, and Railroads**: Implementing additional game mechanics like prison rules and special property rent calculations.
4. **Construction and Advanced Rent Mechanics**: Allowing property upgrades and implementing comprehensive rent calculations.
5. **Networked Gameplay**: Developing a client-server model for multiplayer gaming.

## 💾 Installation and Running the Project

To install and run this project:

1. Clone the repository to your local machine.
2. Ensure Java (version 11 or newer) is installed on your machine.
3. Navigate to the project's root directory in your terminal.
4. Compile the project using Gradle: `./gradlew build`
5. Run the server application: `java -jar build/libs/server.jar`
6. In a new terminal window, run the client application: `java -jar build/libs/client.jar`

## 📖 Usage

After starting both the server and client applications, players can connect to the game by entering their username when prompted. The game follows the traditional Monopoly rules, with modifications detailed in the project's deliverables section.

## 🤝 How to Contribute

Contributions are welcome! If you're interested in contributing, please:

1. Fork the repository.
2. Create a new branch for your feature (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -am 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## 🙏 Acknowledgments

- Lizzie Magie, for inventing Monopoly and inspiring this project.
- The educational institutions and instructors who have incorporated this project into their teaching.

For more detailed information on the project's structure and how to interact with it, please refer to the project's wiki or issue tracker.
