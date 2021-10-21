package automnsCLI;

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

public class Worker1 extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("\n New Message From: ( " + msg.getSender().getName() + " ) \n" + msg.getContent());

                    switch (content) {
                        //Agent status
                        case "Check":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("I'm Alive! :)");
                            send(reply);
                            break;
                        //Agent Service Status
                        case "Service Update":
                            Runtime r = Runtime.getRuntime();
                            String cmd = "sudo docker ps";
                            String logContent = "";
                            try {
                                Process proc = r.exec(cmd);
                                BufferedReader stdInput = new BufferedReader(new
                                        InputStreamReader(proc.getInputStream()));
                               File log = new File("log.txt");
                               FileWriter fw = new FileWriter(log);
                               PrintWriter pw = new PrintWriter(fw);
                               String s = null;
                               while ((s = stdInput.readLine()) != null){
                                   pw.println(s);
                               }
                               pw.close();

                               Scanner scan = new Scanner(log);

                               while (scan.hasNextLine()){
                                   logContent = logContent.concat(scan.nextLine() + "\n");
                               }
                                //System.out.println(logContent);

                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            ACLMessage reply2 = msg.createReply();
                            reply2.setPerformative(ACLMessage.INFORM);
                            reply2.setContent(logContent);
                            send(reply2);
                            break;
                        //worker leave swarm and delete images
                        case "Shutdown":
                            Runtime r2 = Runtime.getRuntime();
                            String leave = "sudo docker swarm leave";
                            String erase = "sudo docker rmi $(sudo docker images -q)";
                            try {
                                r2.exec(leave);
                                r2.exec(erase);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
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
