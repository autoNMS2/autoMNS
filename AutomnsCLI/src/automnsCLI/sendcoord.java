package automnsCLI;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import automnsCLI.VMFunctions;

public class sendcoord extends Agent {
	public void menu () throws IOException {

		System.out.println("\n Select Option: " +
				"\n 1. Check Agents Status " +
				"\n 2. Deploy Agents " +
				"\n 3. Deploy Services " +
				"");

		String privateKey = "autoMNS/jade/src/test0/test.pem";
		String[] agentCommands =
				{"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/database_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.95.147 -local-port 5000 -container Database:automnsCLI.database_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/authenticator_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.95.128 -local-port 5001 -container Authenticator:automnsCLI.authenticator_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/image_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.81.122 -local-port 5002 -container Image:automnsCLI.image_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/persistence_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.86.26 -local-port 5003 -container Persistence:automnsCLI.persistence_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/recommender_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.84.201 -local-port 5004 -container Recommender:automnsCLI.recommender_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/registry_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.86.40 -local-port 5005 -container Registry:automnsCLI.registry_agent",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/webui_agent.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.80.175 -local-port 5006 -container Webui:automnsCLI.webui_agent"
				};

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();
		String msgContent = null;

		switch (cmd)
		{
			case 1:
				msgContent = "Check";
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent(msgContent);
				msg.addReceiver(new AID("Database", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Authenticator", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Image", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Persistence", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Recommender", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Registry", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Webui", AID.ISLOCALNAME));
				send(msg);
				break;
			case 2:
				String[] ip = {"3.86.106.64","3.80.152.2","3.90.15.18","52.202.217.96","3.88.133.117","54.172.145.46","3.92.50.138"};
				int x = 0;
				int y = 0;
				int z = 1;
				try
				{
					do
					{
						VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y]);
						VMFunctions.noOutputSSH(ip[x], privateKey, agentCommands[y+1]);
						x++; y+=2; z++;
					} while(z<8);
				}
				//Throw a failure in Input & Output operations
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 3:
				Runtime r = Runtime.getRuntime();
				String deploy = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore";
				try {
					r.exec(deploy);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}
	public boolean checkmsg() {

		ACLMessage msg = receive();
		if (msg != null) {
			System.out.println("\n New Message: " + msg.getContent()
					+ " from ( " + msg.getSender().getName() + " )");
			return true;
		}
		return false;
	}
	protected void setup() {
		addBehaviour(new CyclicBehaviour(this) {

			public void action() {
				try {
					TimeUnit.SECONDS.sleep(1);
				}catch (InterruptedException e) {
				}
				while (checkmsg());
				try {
					menu();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
