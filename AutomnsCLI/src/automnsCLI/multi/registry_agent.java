package automnsCLI.multi;

import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class registry_agent extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                //reads and output the received msg
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(" Message to " + myAgent.getLocalName()
                            + " received. Message is : " + content);
                    switch (content) {
                        case "Deploy Services":
			    //refer autheticator_agent for output processing
			    Runtime r = Runtime.getRuntime();
                        	String cmd = "docker stack deploy --compose-file /autoMNS/Prototype/lib/Services/registry.yaml TeaStore ";
                        	
						try {
							r.exec(cmd);
						} catch (IOException e) {
							e.printStackTrace();
						} 
                            content = "Registery service deployed";
                            break;
                    }
                    //sends a reply to the sender
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(content);
                    send(reply);
                }
                block();
            }
        });
    }
}
