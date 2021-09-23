package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import java.io.IOException;
import java.util.Scanner;

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

        String[] ip = {"172.31.23.202","","","","","",""};
	String[] agent = {"authenticator_agent","db_agent","image_agent","persistence_agent","recommender_agent","registry_agent","webui_agent"};
        String msgContent = null;
        switch (cmd) {
            case 1:
                String PrivateKey = "jade/src/test0/test.pem";
                String hostIp = "172.17.0.1";
		for(int i=0;i<7;i++)
		{
                	VMFunctions.SSH(ip[i], PrivateKey, "javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/"+agent[i]+".java");
                	VMFunctions.SSH(ip[i], PrivateKey, "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -host " + hostIp + " -port 1099 -agents main:automnsCLI."+agent[i]+"");
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

        //receives and output the reply from a1
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null)
                    System.out.println("Message"+msg.getContent()
                            + " ( " + msg.getSender().getName()+ " )");
                block();
                try {
                    menu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
