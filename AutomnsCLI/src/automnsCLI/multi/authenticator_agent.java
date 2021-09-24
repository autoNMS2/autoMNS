package automnsCLI;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class authenticator_agent extends Agent {
    protected void setup() {
        // create behaviour for receive and send message to Sender
                        Runtime r = Runtime.getRuntime();
                      	String cmd = "docker stack deploy --compose-file /autoMNS/Prototype/lib/Services/auth.yaml TeaStore ";
						try {
							r.exec(cmd);
						} catch (IOException e) {
							e.printStackTrace();
						} 
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
				//this will launch the cmd but no output will be displayed
                        	Runtime r = Runtime.getRuntime();
                        	String cmd = "docker stack deploy --compose-file /autoMNS/Prototype/lib/Services/auth.yaml TeaStore ";
						try {
							r.exec(cmd);
						} catch (IOException e) {
							e.printStackTrace();
						} 
				//this will output in the console of authenticator agent
				//we would have to send this message to our coordinator and to our cli
				/* p = r.exec(cmd);
                                 BufferedReader br = new BufferedReader(
                                     new InputStreamReader(p.getInputStream()));
                                 while ((s = br.readLine()) != null)
                                     System.out.println("line: " + s);
                                 p.waitFor();
                                 System.out.println ("exit: " + p.exitValue());
                                 p.destroy();*/
				//we can use content = "Authenticator service deployed"+s; where s is the output from running the cmd
                            content = "Authenticator service deployed";
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
