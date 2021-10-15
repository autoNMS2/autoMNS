package automnsCLI;

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

public class JAMEScoordinator extends Agent {
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
				} catch (InterruptedException e) {
				}
				while (checkmsg()) ;
				try {
					menu();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getPrivateKeyPath() {
		return getArguments()[getArguments().length - 1].toString();    // dynamic
	}

	public int getIPWorkerCount() {
		if (getArguments().length % 2 == 0) {
			System.out.println("ERROR: Even number of coordinator arguments detected. There should be an even number of ips (2 per worker) and a private key path. Check your coordinator parameters.");
		}
		return ((getArguments().length - 1) / 2);
	}

	public List<String> getWorkerPublicIPs() {
		Object[] VMs = getArguments();

		//Defining the path to the AWS key

		List<String> workerVMsPublic = new ArrayList<String>();
		int count = getIPWorkerCount();
		for (int k = count; k < count * 2; k++) {
			workerVMsPublic.add(getArguments()[k].toString());
		}
		return workerVMsPublic;
	}

	public List<String> getWorkerPrivateIPs() {
		Object[] VMs = getArguments();

		//Defining the path to the AWS key
		String privateKey = (String) VMs[VMs.length - 1];    // dynamic

		List<String> workerVMsPrivate = new ArrayList<String>();
		int count = getIPWorkerCount();
		for (int k = 0; k < count; k++) {
			workerVMsPrivate.add(getArguments()[k].toString());
		}
		return workerVMsPrivate;
	}

	public String[] getAgentCommands() {
		List<String> workerPrivateIPs = getWorkerPrivateIPs();

		String[] agentCommands = new String[workerPrivateIPs.size()];
		String[] names = {"db", "Auth", "Image", "Persistence", "Recommender", "Registry", "Webui"};
		String name;
		for (int i = 0; i < workerPrivateIPs.size(); i++) {
			agentCommands[i * 2] = "javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java";
			if (i < names.length) {
				name = names[i];
			} else name = "worker" + i;
			agentCommands[i * 2 + 1] = "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerPrivateIPs.get(i) + " -port " + (5001 + i) + " -agents " + name + ":automnsCLI.multi.receive0";
		}

		//	//Compiling, running, and joining main platform command array (for each agent)
		//	String[] agentCommands =
		//			{"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 5001 -agents db:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(1) + " -port 5002 -agents Auth:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(2) + " -port 5003 -agents Image:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(3) + " -port 5004 -agents Persistence:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(4) + " -port 5005 -agents Recommender:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(5) + " -port 5006 -agents Registry:automnsCLI.multi.receive0",
		//					"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/receive0.java",
		//					"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(6) + " -port 5007 -agents Webui:automnsCLI.multi.receive0"
		//			};
		return agentCommands;
	}

	public void deployAgents() {
		String[] agentCommands = getAgentCommands();
		try {
			int commandCounter = 0;
			int workerCounter = 0;
			while (commandCounter < agentCommands.length) {
				for (int m = 0; m < 2; m++) {
					VMFunctions.noOutputSSH(getWorkerPublicIPs().get(workerCounter), getPrivateKeyPath(), agentCommands[commandCounter]);
					commandCounter++;
				}
				workerCounter++;
			}
		}
		//Throw a failure in Input & Output operations
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void menu() throws IOException {
		//Defining the path to the AWS key

		System.out.println("select command: " +
				"\n 1. Deploy Agents " +
				"\n 2. Deploy Services " +
				"\n 3. Get Services Update" +
				"");

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();
		String msgContent = null;

		switch (cmd) {
			case 1:
				deployAgents();
				break;
			case 2:
				//msgContent = "Deploy Services";
				Runtime r = Runtime.getRuntime();
				String cmd1 = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore ";
				try {
					r.exec(cmd1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 3:

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				//
				AID dest = new AID("db@172.31.19.10:5001/JADE", AID.ISGUID);
				dest.addAddresses("http://172.31.19.10:7778/acc");
				msg.addReceiver(dest);
				//
				AID dest1 = new AID("auth@172.31.26.27:5002/JADE", AID.ISGUID);
				dest1.addAddresses("http://172.31.26.27:7778/acc");
				msg.addReceiver(dest1);
				//
				AID dest2 = new AID("image@172.31.24.147:5003/JADE", AID.ISGUID);
				dest2.addAddresses("http://172.31.24.147:7778/acc");
				msg.addReceiver(dest2);
				//
				AID dest3 = new AID("pers@172.31.24.53:5004/JADE", AID.ISGUID);
				dest3.addAddresses("http://172.31.24.53:7778/acc");
				msg.addReceiver(dest3);
				//
				AID dest4 = new AID("recom@172.31.26.237:5005/JADE", AID.ISGUID);
				dest4.addAddresses("http://172.31.26.237:7778/acc");
				msg.addReceiver(dest4);
				//
				AID dest5 = new AID("reg@172.31.21.219:5006/JADE", AID.ISGUID);
				dest5.addAddresses("http://172.31.21.219:7778/acc");
				msg.addReceiver(dest5);
				//
				AID dest6 = new AID("webui@172.31.29.252:5007/JADE", AID.ISGUID);
				dest6.addAddresses("http://172.31.29.252:7778/acc");
				msg.addReceiver(dest6);
				//
				msg.setContent(msgContent);
				send(msg);
				break;
		}
	}
}
