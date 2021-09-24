package automnsCLI.multi;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.io.IOException;
import java.util.Scanner;

import automnsCLI.VMFunctions;

public class coordinator extends Agent {
    public void menu () throws IOException {
        System.out.println("select command: " +
                "\n 1. Deploy Agents " +
                "\n 2. Deploy Services " +
                "\n 3. Kill services " +
                "\n 4. List nodes " +
                "\n 5. Service reports " +
                "\n 6. Inspect Service " +
                "\n 7. Promote to Manager " +
                "\n 8. Demote to Worker " +
                "\n 9. Nuke Swarm " +
                "");
        Scanner scanner = new Scanner(System.in);
        int cmd = scanner.nextInt();
	
	 //add ip here
       // String[] ip = {"172.31.23.202","","","","","",""};
	String[] agent = {"authenticator_agent","db_agent","image_agent","persistence_agent","recommender_agent","registry_agent","webui_agent"};
        String msgContent = null;
        switch (cmd) {
            case 1:
                String PrivateKey = "jade/src/test0/AWSKey.ppk";
                String hostIp = "172.17.0.1";
		for(int i=0;i<7;i++)
		{
                	//VMFunctions.SSH(ip[i], PrivateKey, "javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/"+agent[i]+".java");
                	//VMFunctions.SSH(ip[i], PrivateKey, "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -host " + hostIp + " -port 1099 -agents main:automnsCLI."+agent[i]);
                	msgContent = "Deploy Agents";
                	System.out.println("Deploying Agent"+i);
		}
                	break;
		
            case 2:
                msgContent = "Deploy Services";
                System.out.println("Deploy Services");
                break;
            case 3:
                msgContent = "Kill services";
                System.out.println("Kill services");
                break;
            case 4:
                msgContent = "List nodes ";
                System.out.println("List nodes ");
                break;
            case 5:
                msgContent = "Service reports";
                System.out.println("Service reports");
                break;
            case 6:
                msgContent = "Inspect Service";
                System.out.println("Inspect Service");
                break;
            case 7:
                msgContent = "Promote to Manager";
                System.out.println("Promote to Manager");
                break;
            case 8:
                msgContent = "Demote to Worker";
                System.out.println("Demote to Worker");
                break;
            case 9:
                msgContent = "Nuke Swarm";
                System.out.println("Nuke Swarm");
                break;
        }
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent(msgContent);
        msg.addReceiver(new AID("db", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Auth", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Image", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Persistence", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Recommender", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Registry", AID.ISLOCALNAME));
        msg.addReceiver(new AID("Webui", AID.ISLOCALNAME));
        send(msg);
    }
    
    protected void setup() {
        String privateKey = "jade/src/test0/AWSKey.ppk";
    	String coordinatorPrivateIp = "172.31.31.191";
    	String[] agentCommands = {"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/db_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Database -host " + coordinatorPrivateIp + " -port 1099 -agents db:automnsCLI.db_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/authenticator_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Authenticator -host " + coordinatorPrivateIp + " -port 1099 -agents Auth:automnsCLI.authenticator_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/image_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Image -host " + coordinatorPrivateIp + " -port 1099 -agents Image:automnsCLI.image_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/persistence_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Persistence -host " + coordinatorPrivateIp + " -port 1099 -agents Persistence:automnsCLI.persistence_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/recommender_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Recommender -host " + coordinatorPrivateIp + " -port 1099 -agents Recommender:automnsCLI.recommender_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/registry_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Registry -host " + coordinatorPrivateIp + " -port 1099 -agents Registry:automnsCLI.registry_agent",
				"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/webui_agent.java",
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -container-name Webui -host " + coordinatorPrivateIp + " -port 1099 -agents Webui:automnsCLI.webui_agent"
				};
    	try {
			VMFunctions.SSH("107.22.154.37", privateKey, agentCommands[3]);
	    	VMFunctions.SSH("107.22.154.37", privateKey, agentCommands[4]);
	    	VMFunctions.SSH("54.205.63.203", privateKey, agentCommands[5]);
	    	VMFunctions.SSH("54.205.63.203", privateKey, agentCommands[6]);
	    	VMFunctions.SSH("34.201.45.122", privateKey, agentCommands[7]);
	    	VMFunctions.SSH("34.201.45.122", privateKey, agentCommands[8]);
	    	VMFunctions.SSH("3.82.120.4", privateKey, agentCommands[9]);
	    	VMFunctions.SSH("3.82.120.4", privateKey, agentCommands[10]);
	    	VMFunctions.SSH("3.83.255.57", privateKey, agentCommands[11]);
	    	VMFunctions.SSH("3.83.255.57", privateKey, agentCommands[12]);
	    	VMFunctions.SSH("54.144.70.212", privateKey, agentCommands[13]);
	    	VMFunctions.SSH("54.144.70.212", privateKey, agentCommands[14]);
	    	VMFunctions.SSH("54.175.174.39", privateKey, agentCommands[15]);
	    	VMFunctions.SSH("54.175.174.39", privateKey, agentCommands[16]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //receives and output the reply from a1
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                block();
                /*
                try {
                    menu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
            }
        });
    }
}
