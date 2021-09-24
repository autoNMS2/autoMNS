package automnsCLI.multi;

import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;

public class webui_agent extends Agent {
    protected void setup() {
	    Runtime r = Runtime.getRuntime();
    	String cmd = "docker stack deploy --compose-file /autoMNS/Prototype/lib/Services/webui.yaml TeaStore ";
    	
	try {
		r.exec(cmd);
	} catch (IOException e) {
		e.printStackTrace();
	} 
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
                        	String cmd = "docker stack deploy --compose-file /autoMNS/Prototype/lib/Services/webui.yaml TeaStore ";
                        	
						try {
							r.exec(cmd);
						} catch (IOException e) {
							e.printStackTrace();
						} 
                            content = "Webui service deployed";
                            break;
			    default:
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
