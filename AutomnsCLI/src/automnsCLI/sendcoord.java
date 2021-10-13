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

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import automnsCLI.VMFunctions;

public class sendcoord extends Agent {
	public void menu () throws IOException {

		System.out.print("\n *Automns Agent Platform Menu* " +
				"\n 1. List Swarm Nodes" +
				"\n 2. Deploy Agents " +
				"\n 3. Agents Status " +
				"\n 4. Deploy/Update Services" +
				"\n 5. Services Status" +
				"\n 6. Agents Services Status" +
				"\n 7. Remove Services" +
				"\n 8. Shutdown & Erase Environment" +
				"\n Enter Option Number: ");

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();
		String msgContent = null;

		String privateKey = "autoMNS/jade/src/test0/test.pem";
		String[] agentCommands =
				{"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker1.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.95.147 -local-port 5000 -container Worker1:automnsCLI.Worker1",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker2.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.95.128 -local-port 5001 -container Worker2:automnsCLI.Worker2",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker3.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.81.122 -local-port 5002 -container Worker3:automnsCLI.Worker3",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker4.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.86.26 -local-port 5003 -container Worker4:automnsCLI.Worker4",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker5.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.84.201 -local-port 5004 -container Worker5:automnsCLI.Worker5",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker6.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.86.40 -local-port 5005 -container Worker6:automnsCLI.Worker6",
						"javac -cp autoMNS/jade/lib/jade.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/multi/Worker7.java",
						"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -host 172.31.80.125 -port 1099 -local-host 172.31.80.175 -local-port 5006 -container Worker7:automnsCLI.Worker7"
				};

		switch (cmd)
		{
			//swarm nodes summary
			case 1:
				System.out.println("\n *Swarm Nodes Summary* \n");
				Runtime r = Runtime.getRuntime();
				String nodecmd = "sudo docker node ls";

				try {
					Process proc = r.exec(nodecmd);
					BufferedReader stdInput = new BufferedReader(new
							InputStreamReader(proc.getInputStream()));
					BufferedReader stdError = new BufferedReader(new
							InputStreamReader(proc.getErrorStream()));

					// Read the output from the command
					String s = null;
					while ((s = stdInput.readLine()) != null) {
						System.out.println(s);
					}
					// Read any errors from the attempted command
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			//deploy agents
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
			//Agents Status
			case 3:
				System.out.println("\n *Agents Status* \n");
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("Check");
				msg.addReceiver(new AID("Worker1", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker2", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker3", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker4", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker5", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker6", AID.ISLOCALNAME));
				msg.addReceiver(new AID("Worker7", AID.ISLOCALNAME));
				send(msg);
				break;
			//Deploy Services
			case 4:
				System.out.print("\n *Automns Agent Service Deployment Menu* " +
						"\n 1. Deploy/Update all Services" +
						"\n 2. Deploy Authenticator Service" +
						"\n 3. Deploy Database Service " +
						"\n 4. Deploy Image Service " +
						"\n 5. Deploy Persistence Service " +
						"\n 6. Deploy Recommender Service" +
						"\n 7. Deploy Registry Service" +
						"\n 8. Deploy WebUi Service" +
						"\n 0. Return" +
						"\n Enter Option Number: ");
				int cmd3 = scanner.nextInt();
				Runtime r1 = Runtime.getRuntime();

				switch (cmd3){
					case 1:
						String deploy_all = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore";
						try {
							r1.exec(deploy_all);
							System.out.println("All Services Deployed/Updated");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						String deploy_auth = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/auth.yaml TeaStore";
						try {
							r1.exec(deploy_auth);
							System.out.println("Authenticator Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						String deploy_db = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/db.yaml TeaStore";
						try {
							r1.exec(deploy_db);
							System.out.println("Database Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						String deploy_image = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/image.yaml TeaStore";
						try {
							r1.exec(deploy_image);
							System.out.println("Image Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						String deploy_pers = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/persistence.yaml TeaStore";
						try {
							r1.exec(deploy_pers);
							System.out.println("Persistence Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 6:
						String deploy_rec = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/recommender.yaml TeaStore";
						try {
							r1.exec(deploy_rec);
							System.out.println("Recommender Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 7:
						String deploy_reg = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/registry.yaml TeaStore";
						try {
							r1.exec(deploy_reg);
							System.out.println("Registry Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 8:
						String deploy_web = "sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/webui.yaml TeaStore";
						try {
							r1.exec(deploy_web);
							System.out.println("WebUi Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 0:
						break;
				}
				break;
			//Services Status
			case 5:
				System.out.println("\n *Services Status Summary* \n");
				Runtime r2 = Runtime.getRuntime();
				String servicecmd = "sudo docker service ls";
				try {
					Process proc = r2.exec(servicecmd);
					BufferedReader stdInput = new BufferedReader(new
							InputStreamReader(proc.getInputStream()));
					BufferedReader stdError = new BufferedReader(new
							InputStreamReader(proc.getErrorStream()));

					// Read the output from the command
					String s = null;
					while ((s = stdInput.readLine()) != null) {
						System.out.println(s);
					}
					// Read any errors from the attempted command
					while ((s = stdError.readLine()) != null) {
						System.out.println(s);
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
			//Agent Service Status
			case 6:
				ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
				msg2.setContent("Service Update");
				msg2.addReceiver(new AID("Worker1", AID.ISLOCALNAME));
				msg2.addReceiver(new AID("Worker2", AID.ISLOCALNAME));
				msg2.addReceiver(new AID("Worker3", AID.ISLOCALNAME));
				msg2.addReceiver(new AID("Worker4", AID.ISLOCALNAME));
			    msg2.addReceiver(new AID("Worker5", AID.ISLOCALNAME));
				msg2.addReceiver(new AID("Worker6", AID.ISLOCALNAME));
				msg2.addReceiver(new AID("Worker7", AID.ISLOCALNAME));
				send(msg2);
				break;
			case 7:
				System.out.print("\n *Automns Agents Service Removal Menu* " +
						"\n 1. Remove Authenticator Service" +
						"\n 2. Remove Database Service " +
						"\n 3. Remove Image Service " +
						"\n 4. Remove Persistence Service " +
						"\n 5. Remove Recommender Service" +
						"\n 6. Remove Registry Service" +
						"\n 7. Remove WebUi Service" +
						"\n 8. Remove all Services" +
						"\n 0. Return" +
						"\n Enter Option Number: ");
				int cmd2 = scanner.nextInt();
				Runtime r7 = Runtime.getRuntime();
				switch (cmd2){
					case 1:
						String rm_auth = "sudo docker service rm TeaStore_auth";
						try {
							r7.exec(rm_auth);
							System.out.println("Authenticator Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						String rm_db = "sudo docker service rm TeaStore_db";
						try {
							r7.exec(rm_db);
							System.out.println("Database Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						String rm_Image = "sudo docker service rm TeaStore_image";
						try {
							r7.exec(rm_Image);
							System.out.println("Image Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						String rm_pers = "sudo docker service rm TeaStore_persistence";
						try {
							r7.exec(rm_pers);
							System.out.println("Persistence Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						String rm_recom = "sudo docker service rm TeaStore_recommender";
						try {
							r7.exec(rm_recom);
							System.out.println("Recommender Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 6:
						String rm_reg = "sudo docker service rm TeaStore_registry";
						try {
							r7.exec(rm_reg);
							System.out.println("Registry Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 7:
						String rm_web = "sudo docker service rm TeaStore_webui";
						try {
							r7.exec(rm_web);
							System.out.println("WebUi Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 8:
						String rm_all = "sudo docker stack rm TeaStore";
						try {
							r7.exec(rm_all);
							System.out.println("All Services Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 0:
						break;
				}
				break;
			case 8:
				//workers leave swarm and delete images
				//manager leave swarm and delete images
				System.out.print("\n *Automns Swarm Shutdown Menu* " +
						"\n 1. Remove Swarm Workers & Erase Docker Images" +
						"\n 2. Remove Swarm Manager & Erase Docker Images" +
						"\n 0. Return" +
						"\n Enter Option Number: ");
				int cmd8 = scanner.nextInt();
				Runtime r8 = Runtime.getRuntime();
				switch (cmd8){
					case 1:
						ACLMessage msg8 = new ACLMessage(ACLMessage.INFORM);
						msg8.setContent("Shutdown");
						msg8.addReceiver(new AID("Worker1", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker2", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker3", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker4", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker5", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker6", AID.ISLOCALNAME));
						msg8.addReceiver(new AID("Worker7", AID.ISLOCALNAME));
						send(msg8);
						break;
					case 2:
						String leave = "sudo docker swarm leave -f && sudo docker rmi $(sudo docker images -q)";
						try {
							r8.exec(leave);
							System.out.println("Manager Node Left Swarm & Docker Images Deleted");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 0:
						break;
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
