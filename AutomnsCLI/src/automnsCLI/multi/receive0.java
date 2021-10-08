package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class receive0 extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //reads and output the received msg
                ACLMessage msg = receive();

                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received. Message is : " + content);
                    switch (content) {
                        case "Deploy Agents":
                            //msgContent = "Deploy Agents";
                            System.out.println("Agent 1 Alive!");
                            break;
                        case "Deploy Services":
                            //msgContent = "Deploy Services";
                            System.out.println("Deploying Services");
                            break;
                        case "Kill services":
                            //msgContent = "Kill services";
                            System.out.println("Killinggggg services");
                            break;
                    }

                    //sends a reply to the sender
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(" Hello reply from " + myAgent.getLocalName());
                    send(reply);
                }
                block();
            }
        });
    }
}
