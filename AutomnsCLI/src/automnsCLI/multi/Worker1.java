package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.Arrays;

public class Worker1 extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" New Message: " + content
                            + " from: (" + msg.getSender().getName() + ")");
                    Runtime r = Runtime.getRuntime();
                    switch (content) {
                        //Agent status
                        case "Check":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("\n" + myAgent.getLocalName() + " Agent is alive!");
                            send(reply);
                            break;
                        //Agent Service Status
                        case "Service Update":

                            String cmd = "sudo docker ps";
                            String s;
                            try {
                                Process proc = r.exec(cmd);
                                BufferedReader stdInput = new BufferedReader(new
                                        InputStreamReader(proc.getInputStream()));

                                while ((s = stdInput.readLine()) != null) {
                                    String[] ps = s.split("\\s");
                                    s = Arrays.toString(ps);
                                }
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            ACLMessage reply2 = msg.createReply();
                            reply2.setPerformative(ACLMessage.INFORM);
                            reply2.setContent("\n" + myAgent.getLocalName() + "\n" + s);
                            send(reply2);
                            break;
                        //worker leave swarm and delete images
                        case "Shutdown":
                            String leave = "sudo docker swarm leave && sudo docker rmi $(sudo docker images -q)";
                            try {
                                r.exec(leave);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            ACLMessage reply4 = msg.createReply();
                            reply4.setPerformative(ACLMessage.INFORM);
                            reply4.setContent("\n" + myAgent.getLocalName() + "Worker Node Left Swarm & Docker Images Deleted");
                            send(reply4);
                            break;
                    }
                }
            }
        });
    }
}
