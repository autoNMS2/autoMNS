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
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.83.213 -port 5001 -agents db:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.94.8 -port 5002 -agents auth:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.82.12 -port 5003 -agents image:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.89.126 -port 5004 -agents pers:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.82.46 -port 5005 -agents recom:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.110 -port 5006 -agents reg:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.24.6 -port 5007 -agents webui:automnsCLI.receive0"
				};

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();
		String msgContent = null;

		switch (cmd)
		{
			case 1:
				msgContent = "Deploy Agents";
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				//
				AID dest = new AID("db@172.31.83.213:5001/JADE", AID.ISGUID);
				dest.addAddresses("http://172.31.83.213:7778/acc");
				msg.addReceiver(dest);
				//
				AID dest1 = new AID("auth@172.31.94.8:5002/JADE", AID.ISGUID);
				dest1.addAddresses("http://172.31.94.8:7778/acc");
				msg.addReceiver(dest1);
				//
				AID dest2 = new AID("image@172.31.82.12:5003/JADE", AID.ISGUID);
				dest2.addAddresses("http://172.31.82.12:7778/acc");
				msg.addReceiver(dest2);
				//
				AID dest3 = new AID("pers@172.31.89.126:5004/JADE", AID.ISGUID);
				dest3.addAddresses("http://172.31.89.126:7778/acc");
				msg.addReceiver(dest3);
				//
				AID dest4 = new AID("recom@172.31.82.46:5005/JADE", AID.ISGUID);
				dest4.addAddresses("http://172.31.82.46:7778/acc");
				msg.addReceiver(dest4);
				//
				AID dest5 = new AID("reg@172.31.80.110:5006/JADE", AID.ISGUID);
				dest5.addAddresses("http://172.31.80.110:7778/acc");
				msg.addReceiver(dest5);
				//
				AID dest6 = new AID("webui@172.31.24.6:5007/JADE", AID.ISGUID);
				dest6.addAddresses("http://172.31.24.6:7778/acc");
				msg.addReceiver(dest6);
				//
				msg.setContent(msgContent);
				send(msg);
				break;
			case 2:
				//msgContent = "Deploy Agents";
				String[] ip = {"34.227.13.150","54.158.196.4","3.84.54.191","3.85.159.166","18.212.184.1","3.83.204.155","54.87.218.111"};
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
					} while(z<3);
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
