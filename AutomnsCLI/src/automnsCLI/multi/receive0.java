package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class receive0 extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received. Message is : " + content);
                    switch (content) {
                        case "Deploy Agents":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("Agent " + myAgent.getLocalName() + " is alive!");
                            send(reply);
                            break;
                        case "Deploy Services":
                            //msgContent = "Deploy Services";
                            System.out.println("Deploying Services");
                            break;
                        case "Kill services":
                            //msgContent = "Kill services";
                            System.out.println("Killinggggg services");
                            break;
                    }
                }
            }
        });
    }
}
