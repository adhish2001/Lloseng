// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;
  private boolean logoff = false;
  private static String loginId;




  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String host,int port,String loginId, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
    openConnection();
    sendToServer("#login " + loginId);
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message){
    String temp3[] = message.split(" ");
	try {
		if(message.equals("#quit")){
			clientUI.display("Client has quit");
			System.out.println("Closing Connection");
			try{
				quit();
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if(message.equals("#logoff")){
			try{
				clientUI.display("Client has disconnected");
				System.out.println("Closing Connection");
				logoff = true;
				sendToServer("connection closed");
				closeConnection();
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if(temp3[0].equals("#sethost")&& logoff){
			System.out.println("Setting Host");
			try{
				String temp[] = message.split(" ");
				int newhost = Integer.parseInt(temp[1]);
				setPort(newhost);
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if(temp3[0].equals("#login")&& logoff){
			if(temp3[1].equals(loginId)){
				try{
					System.out.println("Connected to server");
					openConnection();
					sendToServer("#login " + loginId);
					logoff = false;
				}catch (Exception e) {
					System.out.println("Requires a loginId");
				}
			}
		}
		else if(temp3[0].equals("#setport")&& logoff){
			System.out.println("The port is ");
			try{
				String temp1[] = message.split(" ");
				int newport = Integer.parseInt(temp1[1]);
				setPort(newport);
				logoff = false;
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if(message.equals("#gethost")){
			try{
				System.out.println("The host is" +getHost());
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if (message.equals("#getport")) {
			try {
				System.out.println("The port is" +getPort());
			}catch (Exception e) {
				System.out.println("ERROR!");
			}
		}
		else if(message.charAt(0)=='#'){
			System.out.println("ERROR! Command invalid");
		}else{
			sendToServer(message);
		}
	}
	catch(IOException e)
	{
		clientUI.display
			("Could not send message to server.Terminating client.");
		quit();
	}
}
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

  protected void connectionClosed() {
    clientUI.display("Connection Closed");
  }
  protected void connectionException(Exception exception) {
    clientUI.display("The connection has terminated");
    System.exit(0);
  }
}
//End of ChatClient class
