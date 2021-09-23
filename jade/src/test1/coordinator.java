package remote;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class coordinator extends Agent {
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
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        //uses GUID with a1 name,platform name, and default port
        AID dest = new AID("a1@172.31.29.138:1098/JADE", AID.ISGUID);
        AID dest1 = new AID("a2@172.31.29.138:1099/JADE", AID.ISGUID);
        //uses MTP address of a1 platform and default port
        dest.addAddresses("http://172.31.29.138:7778/acc");
        dest1.addAddresses("http://172.31.29.138:42599/acc");
        msg.addReceiver(dest);
        msg.addReceiver(dest1);
        msg.setContent("whoami");
        send(msg);
    }
}