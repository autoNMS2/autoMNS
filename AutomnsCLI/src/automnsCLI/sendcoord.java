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

		System.out.println("select command: " +
				"\n 1. Get Update " +
				"\n 2. Deploy Agents " +
				"\n 3. Deploy Services " +
				"");

		String privateKey = "autoMNS/jade/src/test0/test.pem";
		String[] agentCommands =
			{"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.16.27 -local-port 5000 -container Database:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.21.119 -local-port 5001 -container Authenticator:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.21.120 -local-port 5002 -container Image:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.24.157 -local-port 5003 -container Persistence:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.27.194 -local-port 5004 -container Recommender:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.16.180 -local-port 5005 -container Registry:automnsCLI.receive0",
					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.28.122 -port 1099 -local-host 172.31.21.33 -local-port 5006 -container Webui:automnsCLI.receive0"};

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();
		String msgContent = null;

		switch (cmd)
		{
			case 1:
				msgContent = "Deploy Agents";
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
				String[] ip = {"3.80.113.155"};
				//String[] ip = {"3.80.113.155","54.147.29.37","54.235.236.254","18.212.96.141","34.207.80.67","34.224.68.95","34.230.29.173"};
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
					} while(z<2);
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
				String cmd1 = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore ";
				try
				{
					r.exec(cmd1);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
		}
	}
	public boolean checkmsg() {

		ACLMessage msg = receive();
		if (msg != null) {

			System.out.println("Message" + msg.getContent()
					+ " ( " + msg.getSender().getName() + " )");

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
