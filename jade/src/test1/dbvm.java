package remote;


	import jade.core.Agent;
	import jade.core.behaviours.*;
	import jade.lang.acl.*;

	public class dbvm extends Agent {
	    protected void setup() {
	        // create behaviour for receive and send message to Sender
	        addBehaviour(new CyclicBehaviour(this) {
	            public void action() {
	                //reads and output the received msg
	                ACLMessage msg = receive();
	                if (msg != null) {
	                    System.out.println(" Message to " + myAgent.getLocalName()
	                            + " received. Message is : " + msg.getContent());

	                    //sends a reply to the sender
	                    ACLMessage reply = msg.createReply();
	                    reply.setPerformative(ACLMessage.INFORM);
	                    reply.setContent(" Yes boss");
	                    send(reply);
	                }
	                block();
	            }
	        });
	    }
	}