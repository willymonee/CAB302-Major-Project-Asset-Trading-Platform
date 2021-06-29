# CAB302-Major-Project
2021 QUT CAB302 Major Project: Electronic Asset Trading Platform - Group Assessment

**Description**
Created a client-server system for electronic trading of assets within the Client's organisation using Java. The system has organisational units/departments which will be assigned a certain amount of electronic credits which they can use to buy resources from other units. Members part of an organisational unit can place sell offers and buy offers for particular assets on behalf of their organisational unit. IT admins would be responsible for facilitating the user's ability to trade e.g. creating users, organisational units and adding new assets to the system. This was done through a client-server model where the client will request to edit, remove, insert or read information from the database, the server will receive this request and then perform the database queries and return a response back to the client. 

**Features**
1) IT Admin: create users, create assets, create organisational units, edit credits and assets for an organisational unit
2) Members/Traders: change password, list buy/sell offers, remove/edit buy/sell offers, reconcile trade offers, view offers made by the organisational unit, view offers made by other units, view organisational unit trade history
3) Systems Admins: initialise DB, salt and hash passwords, establish client server communication

**Video Demo**
https://youtu.be/R4IdAV5xnHM?t=264

**Process**
1) Gather requirements from project description and user stories
2) Produce a detailed design of the system to meet these requirements e.g. UML diagram, database schema, GUI design and Network Protocol
3) Implement the system using Java
4) Create a comprehensive unit testing suite (Blackbox and Glassbox testing in JUnit5) 
5) Refactor code
6) Create build scripts and continuous integration pipeline that builds the software and runs unit tests

Although the process has been listed in 'steps', as an Agile methodology was taken these steps were repeated through the project. 

**Deployment Instructions**
To deploy the software, copy the ElectronicAssetTrading-Client.jar in a folder with
a copy of the ipconfig.props file, and copy the ElectronicAssetTrading-Server.jar in
a folder with a copy of the db.props and ipconfig.props (exclude the ETP.db SQLite
database file). Set the IP address and port number to your preference, or keep it
HOSTNAME=127.0.0.1, and PORT=10000. Open the ElectronicAssetTrading-Server.jar file
to start the server and create a fresh SQLite database file with a preset IT Admin,
allowing the users to connect and open on the ElectronicAssetTrading-Client.jar.

The preset IT Admin user login is:
Username: itadmin
Password: admin123

To test the unit tests, use the supplied sqlite database file.

To test the Marketplace History Graph, use the following inserts, or your own:

INSERT INTO Marketplace_history VALUES (11, 2, 1, 2, 22.1, 5, "2021-04-25");
INSERT INTO Marketplace_history VALUES (12, 2, 1, 2, 23.5, 5, "2021-05-06");
INSERT INTO Marketplace_history VALUES (13, 2, 1, 2, 25, 5, "2021-05-17");
INSERT INTO Marketplace_history VALUES (14, 2, 1, 2, 33, 5, "2021-05-17");
INSERT INTO Marketplace_history VALUES (15, 2, 1, 2, 20.15, 5, "2021-04-24");
INSERT INTO Marketplace_history VALUES (16, 2, 1, 2, 30, 5, "2021-05-06");

**Group Members**
Daniel Jong: https://github.com/DanDragoneel
David Troung: https://github.com/DavidTruong1609
William Ma: https://github.com/willliamm6984
William Ee: https://github.com/willymonee
