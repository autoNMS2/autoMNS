package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;

public class recommender_agent extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received message is : " + content);
                    switch (content) {
                        case "Check":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("\n" + myAgent.getLocalName() + " Agent is alive!");
                            send(reply);
                            break;
                        case "Deploy Services":
                            Runtime r = Runtime.getRuntime();
                            String cmd = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/recommender.yaml Recommender";
                            try {
                                r.exec(cmd);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("\n" + myAgent.getLocalName() + " service deployed");
                            send(reply);
                            break;
                        case "Kill services":
                            System.out.println("Killinggggg services");
                            break;
                    }
                }
            }
        });
    }
}
