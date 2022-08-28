# ChatApp_Clype
This is a personal porject for Advanced Programming Concepts in Java:

> The application name **Clype** is a portmanteau combining `Clarkson` and `Skype`


**Clype** is a **multi-user chat app**, allowing multiple computers to connect to the same chat room, exchanging messages, **encrypted** text files, and images 

To run the chat room, one has to run the [Server.jar](Server.jar) first, which will open a window displaying information about the connected clients

Then, others can run the [Main.jar](Main.jar) to connect to the chat room using optional hostname and port number.

The server window and the chat room are both designed by **JavaFx** 

## Connecting Instructions
1. Run the [Server.jar](Server.jar) and enter a port number to start the server; 
> Press `Default` button to use the default port number is `7000` 
2. Run [Main.jar](Main.jar) with a username, a hostname and a port number to connect to the server
> Press `Default` button to use the default user name is `Anon`, host name is `localhost`, and port number is `7000`
3. There are three buttons in the chat room:
  * `Leave`: Leave the chat room and close the connection                                                    
  * `Share Picture` : Display a picture to all the users in the chat room
  >  Clype supports **png, jpg, and gif** file formats
  * `Share Text File` : Display a text to all the users in the chat room
4. Close the window will also safely close the connection between the server and the client  
> There are also buttons in the chat room and the server window for closing the connection






## Details

### [src/data](src/data) contains all necessary files for users and servers to exchange data, text messages or images:

* [ClypeData.java](src/data/ClypeData.java) objects implements **[Vigenere cipher](https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher)** inside `encrypt` and `decrypt` functions for text files

### [src/main](src/main) contains all necessary files for networking between the clients and  server:
* [ClypeServer.java](src/main/ClypeServer.java) implements `ClypeServer` object that is used in [Server.jar](Server.jar) to handle all the underlying interactions with the server
* [ServerSideClientIO.java](src/main/ServerSideClientIO.java) utilizes **multi-threading** in Java to implement fucntions for sening data to and receivimg data from client
* [ClypeClient.java](src/main/ClypeClient.java) implements `ClypeClient` object that is used in [Main.jar](main.jar) to handle all the underlying interactions with the server


### [src/application](src/application) contains all GUIs developed by **JavaFx** for the application:
* [Main.java](src/application/Main.java) designs the chat room interface and handle events triggered in the chat room
* [Server.java](src/application/Server.java) designs the server window interface that displays all the connected clients' information

> [pic_1.png](pic_1.png), [pic_2.jpg](pic_2.jpg), [pic_3.gif](pic_3.gif) can be used to test picture share

> [document.txt](document.txt), [test.txt](test.txt) can be used to test text file share




