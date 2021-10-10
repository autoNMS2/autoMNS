package automnsCLI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;

public class registry_agent extends Agent {
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received message is : " + content);
                    switch (content) {
                        case "Deploy Agents":
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent("\n" + myAgent.getLocalName() + " Agent is alive!");
                            send(reply);
                            break;
                        case "Deploy Services":
                            Runtime r = Runtime.getRuntime();
                            String cmd = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/registry.yaml Registry";
                            try {
                                r.exec(cmd);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
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
