package msgtest;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Sender extends Agent {
    protected void setup() {
// Setup answering behavior
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                block();
            }
        });
// Send message to agent "a1"
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(" Hi from Sender ");
        msg.addReceiver(new AID("a1", AID.ISLOCALNAME));
        send(msg);
    }
}
