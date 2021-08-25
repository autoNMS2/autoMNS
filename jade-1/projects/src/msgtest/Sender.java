package msgtest;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class Sender extends Agent {
    protected void setup() {

        addBehaviour(new CyclicBehaviour(this) {
            //read and output the reply
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("a1 replied: "+msg.getContent());
                            //+ " ( " + msg.getSender().getName()+ " )");
                block();
            }
        });
        // creates a msg and send to a specified agent
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("install docker");
        //this is used to find the agent inside a local platform
        msg.addReceiver(new AID("a1", AID.ISLOCALNAME));
        //this is used to find the agent on the network
        //issue here is i have to define the agent ip
        //msg.addReceiver(new AID("a1@192.168.1.1:1099/JADE", AID.ISGUID));
        send(msg);
    }
}
