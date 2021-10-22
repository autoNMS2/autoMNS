package automnsCLI.workers;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
//Start of Agent program
public class Worker3 extends Agent
{
    //overriding the agent setup function
    protected void setup()
    {
        //Defining a behaviour
        addBehaviour(new CyclicBehaviour(this)
        {
            //Starting the behaviour
            public void action()
            {
                //receiving a msg
                ACLMessage msg = receive();
                //if msg received print it
                if (msg != null)
                {
                    String content = msg.getContent();
                    System.out.println("\nNew Message From: (" + msg.getSender().getName() + ") \n" + msg.getContent());
                    //Compare msg contents and perform procedure
                    switch (content)
                    {
                        //Agent State
                        case "State Check":
                            //Create a reply and send it
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("I'm Alive! :)");
                            send(reply);
                            break;
                        //Agent Service Status
                        case "Service Update":
                            //Defining a command to execute
                            Runtime r = Runtime.getRuntime();
                            String cmd = "sudo docker ps";
                            String logContent = "";
                            try
                            {
                                //Execute the command
                                Process proc = r.exec(cmd);
                                //read its output
                                BufferedReader stdInput = new BufferedReader(new
                                        InputStreamReader(proc.getInputStream()));
                               //Create a log file
                               File log = new File("log.txt");
                               FileWriter fw = new FileWriter(log);
                               PrintWriter pw = new PrintWriter(fw);
                               String s = null;
                               //Write the output in the log file
                               while ((s = stdInput.readLine()) != null)
                               {
                                   pw.println(s);
                               }
                               //Close file when writing is done
                               pw.close();
                               //Create a scanner to read the log file
                               Scanner scan = new Scanner(log);
                               //read the log file content
                               while (scan.hasNextLine())
                               {
                                   //save the log file content into a variable
                                   logContent = logContent.concat(scan.nextLine() + "\n");
                               }
                            }
                            //Catch thrown errors
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            //Send the log file content in a msg
                            ACLMessage reply2 = msg.createReply();
                            reply2.setPerformative(ACLMessage.INFORM);
                            reply2.setContent(logContent);
                            send(reply2);
                            break;
                        //worker leave swarm and delete images
                        case "Shutdown":
                            //Defining a command to execute
                            Runtime r2 = Runtime.getRuntime();
                            String leave = "sudo docker swarm leave";
                            String erase = "sudo docker rmi $(sudo docker images -q)";
                            try
                            {
                                //Execute the commands
                                r2.exec(leave);
                                r2.exec(erase);
                            }
                            //Catch thrown errors
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            //Send a msg confirming procedure
                            ACLMessage reply4 = msg.createReply();
                            reply4.setPerformative(ACLMessage.INFORM);
                            reply4.setContent("My node left Swarm & Docker images deleted");
                            send(reply4);
                            break;
                    }
                }
            }
        });
    }
}