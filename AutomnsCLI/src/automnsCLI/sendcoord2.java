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
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.89.48 -port 6001 -agents db:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.84.108 -port 6002 -agents auth:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.81.124 -port 6003 -agents image:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.184 -port 6004 -agents pers:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.38 -port 6005 -agents recom:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.81.175 -port 6006 -agents reg:automnsCLI.receive0",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.94.221 -port 6007 -agents webui:automnsCLI.receive0"
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
				AID dest = new AID("db@172.31.89.48:6001/JADE", AID.ISGUID);
				dest.addAddresses("http://172.31.89.48:40885/acc");
				msg.addReceiver(dest);
				//
				AID dest1 = new AID("auth@172.31.84.108:6002/JADE", AID.ISGUID);
				dest1.addAddresses("http://172.31.84.108:45519/acc");
				msg.addReceiver(dest1);
				//
				AID dest2 = new AID("image@172.31.81.124:6003/JADE", AID.ISGUID);
				dest2.addAddresses("http://172.31.81.124:33303/acc");
				msg.addReceiver(dest2);
				//
				AID dest3 = new AID("pers@172.31.80.184:6004/JADE", AID.ISGUID);
				dest3.addAddresses("http://172.31.80.184:40791/acc");
				msg.addReceiver(dest3);
				//
				AID dest4 = new AID("recom@172.31.80.38:6005/JADE", AID.ISGUID);
				dest4.addAddresses("http://172.31.80.38:45975/acc");
				msg.addReceiver(dest4);
				//
				AID dest5 = new AID("reg@172.31.81.175:6006/JADE", AID.ISGUID);
				dest5.addAddresses("http://172.31.81.175:39159/acc");
				msg.addReceiver(dest5);
				//
				AID dest6 = new AID("webui@172.31.94.221:6007/JADE", AID.ISGUID);
				dest6.addAddresses("http://172.31.94.221:37167/acc");
				msg.addReceiver(dest6);
				//
				msg.setContent(msgContent);
				send(msg);
				break;
			case 2:
				//msgContent = "Deploy Agents";
				String[] ip = {"52.87.203.147","18.208.149.42","18.212.166.86","18.204.196.102","54.146.141.84","34.238.176.2","100.24.20.48"};
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
