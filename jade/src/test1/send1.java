package test1;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class send1 extends Agent {
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
        //uses GUID with a1 name,platform name, and default port
        AID dest = new AID("a1@100.91.52.21:1099/JADE", AID.ISGUID);
        //uses MTP address of a1 platform and default port
        dest.addAddresses("http://100.91.52.21:7778/acc");
        msg.addReceiver(dest);
        msg.setContent("Hi from Sender!");
        send(msg);
    }
}