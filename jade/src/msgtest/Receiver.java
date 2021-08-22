package msgtest;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Receiver extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {

                ACLMessage msg = receive(); // receive sent message
                if (msg != null) {
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received. Message is : " + msg.getContent());

                    // create a reply back to the Sender agent
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(" Hello reply from Receiver");
                    send(reply);
                }
                block();
            }
        });
    }
}
