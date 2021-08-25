package msgtest;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Sender extends Agent {
    protected void setup() {
        //read and output the reply
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                block();
            }
        });
        // creates a msg and send to a specified agent (a1)
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(" Hi from Sender ");
        msg.addReceiver(new AID("a1", AID.ISLOCALNAME));
        //this is used to find the agent on the network
        //must define the agent ip
        //msg.addReceiver(new AID("a1@172.31-93.198:1099/JADE", AID.ISGUID));
        send(msg);
    }
}