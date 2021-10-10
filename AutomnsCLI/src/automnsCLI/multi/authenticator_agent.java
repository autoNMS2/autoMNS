package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;

public class authenticator_agent extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" New Message: " + content
                            + " from: (" + msg.getSender().getName() + ")");
                    switch (content) {
                        case "Check":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("\n" + myAgent.getLocalName() + " Agent is alive!");
                            send(reply);
                            break;
                        case "Service Update":
                            Runtime r = Runtime.getRuntime();
                            String cmd = "sudo docker ps";
                            try {
                                Process proc = r.exec(cmd);
                                BufferedReader stdInput = new BufferedReader(new
                                        InputStreamReader(proc.getInputStream()));

                                BufferedReader stdError = new BufferedReader(new
                                        InputStreamReader(proc.getErrorStream()));

                                // Read the output from the command
                                System.out.println("Here is the standard output of the command:\n");
                                String s = null;
                                while ((s = stdInput.readLine()) != null) {
                                    ACLMessage reply2 = msg.createReply();
                                    reply2.setPerformative(ACLMessage.INFORM);
                                    reply2.setContent("\n" + s);
                                    send(reply2);
                                }
                                // Read any errors from the attempted command
                                System.out.println("Here is the standard error of the command (if any):\n");
                                while ((s = stdError.readLine()) != null) {
                                    System.out.println(s);
                                }
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            break;
                        case "Kill services":
                            System.out.println("Killinggggg services");
                            break;
                    }
                }
            }
        });
    }
}
