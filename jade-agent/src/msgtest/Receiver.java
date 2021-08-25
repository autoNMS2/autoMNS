package msgtest;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Receiver extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //reads and output the received msg
                int i = 0;
                ACLMessage msg = receive();
                if (msg != null) {
                    String save = msg.getContent();
                    do {
                        System.out.println(myAgent.getLocalName()
                                + " received a message saying " + save);
                        System.out.println(myAgent.getLocalName()
                                + " is now installing docker");
                        i++;
                    } while  (save == "install docker" && i < 2);
                    // sends a reply to the sender
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Docker installed");
                    send(reply);
                }
                block();
            }
        });
    }
}