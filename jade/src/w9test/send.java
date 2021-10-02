package w9test;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class send extends Agent {
    protected void setup() {
        //receives and output the reply from a1
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                doDelete();
                block();
            }
            
        });
        // creates a msg and send to a specified agent (a1)
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(" Hi from Sender ");
        msg.addReceiver(new AID("a1", AID.ISLOCALNAME));
        send(msg);
    }
}
