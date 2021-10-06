package automnsCLI.multi;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class send extends Agent {
    protected void setup() {
        // creates a msg and send to a specified agent (a1)
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        //uses GUID with a1 name,platform name, and default port
        AID dest = new AID("coordinator@172.31.29.72:1099/JADE", AID.ISGUID);
        //uses MTP address of a1 platform and default port
        dest.addAddresses("http://172.31.29.72:7778/acc");
        msg.addReceiver(dest);
        msg.setContent("Hi from db!");
        send(msg);
    }
}
