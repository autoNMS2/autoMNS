package test2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class receive2 extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //reads and output the received msg
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received. Message is : " + msg.getContent());

                    //sends a reply to the sender
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