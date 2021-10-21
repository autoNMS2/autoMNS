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

public class coordinator extends Agent {
	public void menu () throws IOException {
		//Defining the path to the AWS key
		Object[] VMs = getArguments();
		//Defining the path to the AWS key
		String privateKey = (String) VMs[VMs.length - 1];
		System.out.println(privateKey);
		//Defining the IP address of the main platform for other agents to join
		List<String> workerVMsPrivate = new ArrayList<String>();
		for (int k = 0; k < 7; k++) {
			workerVMsPrivate.add((String) VMs[k]);
		}
		List<String> workerVMsPublic = new ArrayList<String>();
		for (int k = 7; k < VMs.length - 1; k++) {
			workerVMsPublic.add((String) VMs[k]);
		}

		System.out.print("\n<<<<<<<<Platfrom Main Menu>>>>>>>" +
				"\n|1| Swarm Nodes" +
				"\n|2| Deploy Worker Agents " +
				"\n|3| Check Worker Agents State " +
				"\n|4| Deploy/Update Services" +
				"\n|5| Check Available Services" +
				"\n|6| Check Worker Agents Services" +
				"\n|7| Remove Service/s" +
				"\n|8| Shutdown Swarm & Erase Images" +
				"\nEnter Option Number >> ");

		Scanner scanner = new Scanner(System.in);
		int cmd = scanner.nextInt();

		String[] agentCommands =
				{
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker1.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -container Worker1:automnsCLI.Worker1",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker2.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(1) + " -local-port 5001 -container Worker2:automnsCLI.Worker2",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker3.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(2) + " -local-port 5002 -container Worker3:automnsCLI.Worker3",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker4.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(3) + " -local-port 5003 -container Worker4:automnsCLI.Worker4",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker5.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(4) + " -local-port 5004 -container Worker5:automnsCLI.Worker5",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker6.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(5) + " -local-port 5005 -container Worker6:automnsCLI.Worker6",
				"javac -cp AutoMNS/lib/jade.jar -d classes AutoMNS/automnsCLI/workers/Worker7.java",
				"java -cp AutoMNS/lib/jade.jar:classes jade.Boot -host " + workerVMsPrivate.get(0) + " -port 1099 -local-host " + workerVMsPrivate.get(6) + " -local-port 5006 -container Worker7:automnsCLI.Worker7"
				};

		switch (cmd)
		{
			//swarm nodes summary
			case 1:
				System.out.println("\nSwarm Nodes Summary:\n");
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
				System.out.println("\nDeploying The Agents.. \n");
				try
				{
					int commandCounter = 0;
					int workerCounter = 0;
					while(commandCounter < agentCommands.length){
						for(int m = 0; m < 2; m++) {
							VMFunctions.noOutputSSH(workerVMsPublic.get(workerCounter), privateKey, agentCommands[commandCounter]);
							commandCounter ++;
						}
						workerCounter ++;
					}
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
				System.out.println("\nWorker Agents State:");
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("State Check");
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
				System.out.print("\n<<<<<Service Deployment Menu>>>>" +
						"\n|1| Deploy/Update all Services" +
						"\n|2| Deploy Authenticator Service" +
						"\n|3| Deploy Database Service " +
						"\n|4| Deploy Image Service " +
						"\n|5| Deploy Persistence Service " +
						"\n|6| Deploy Recommender Service" +
						"\n|7| Deploy Registry Service" +
						"\n|8| Deploy WebUi Service" +
						"\n|0| Return" +
						"\nEnter Option Number >> ");
				int cmd3 = scanner.nextInt();
				Runtime r1 = Runtime.getRuntime();
				switch (cmd3){
					case 1:
						String deploy_all = "sudo docker stack deploy --compose-file AutoMNS/Services/all.yaml TeaStore";
						try {
							r1.exec(deploy_all);
							System.out.println("\nAll Services Deployed/Updated");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						String deploy_auth = "sudo docker service create --name TeaStore_auth descartesresearch/teastore-auth";
						try {
							r1.exec(deploy_auth);
							System.out.println("\nAuthenticator Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						String deploy_db = "sudo docker service create --name TeaStore_db --publish published=3306,target=3306 descartesresearch/teastore-db";
						try {
							r1.exec(deploy_db);
							System.out.println("\nDatabase Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						String deploy_image = "sudo docker service create --name TeaStore_image descartesresearch/teastore-image";
						try {
							r1.exec(deploy_image);
							System.out.println("\nImage Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						String deploy_pers = "sudo docker service create --name TeaStore_persistence descartesresearch/teastore-persistence";
						try {
							r1.exec(deploy_pers);
							System.out.println("\nPersistence Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 6:
						String deploy_rec = "sudo docker service create --name TeaStore_recommender descartesresearch/teastore-recommender";
						try {
							r1.exec(deploy_rec);
							System.out.println("\nRecommender Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 7:
						String deploy_reg = "sudo docker service create --name TeaStore_registry descartesresearch/teastore-registry";
						try {
							r1.exec(deploy_reg);
							System.out.println("\nRegistry Service Deployed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 8:
						String deploy_web = "sudo docker service create --name TeaStore_webui --publish published=8080,target=8080 descartesresearch/teastore-webui";
						try {
							r1.exec(deploy_web);
							System.out.println("\nWebUi Service Deployed");
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
				System.out.println("\nServices Status Summary: \n");
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
				System.out.println("\nAgents Services Status Summary:");
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
			//Service Removal
			case 7:
				System.out.print(
						"\n<<<<<<Service Removal Menu>>>>>>" +
						"\n|1| Remove all Services" +
						"\n|2| Remove Authenticator Service" +
						"\n|3| Remove Database Service " +
						"\n|4| Remove Image Service " +
						"\n|5| Remove Persistence Service " +
						"\n|6| Remove Recommender Service" +
						"\n|7| Remove Registry Service" +
						"\n|8| Remove WebUi Service" +
						"\n|0| Return" +
						"\n Enter Option Number >> ");
				int cmd2 = scanner.nextInt();
				Runtime r7 = Runtime.getRuntime();
				String rm_all = "sudo docker stack rm TeaStore";
				String rm_auth = "sudo docker service rm TeaStore_auth";
				String rm_db = "sudo docker service rm TeaStore_db";
				String rm_Image = "sudo docker service rm TeaStore_image";
				String rm_pers = "sudo docker service rm TeaStore_persistence";
				String rm_recom = "sudo docker service rm TeaStore_recommender";
				String rm_reg = "sudo docker service rm TeaStore_registry";
				String rm_web = "sudo docker service rm TeaStore_webui";
				switch (cmd2){

					case 1:
						try {
							r7.exec(rm_auth);
							r7.exec(rm_db);
							r7.exec(rm_Image);
							r7.exec(rm_pers);
							r7.exec(rm_recom);
							r7.exec(rm_reg);
							r7.exec(rm_web);
							r7.exec(rm_all);
							System.out.println("\nAll Services Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						try {
							r7.exec(rm_auth);
							System.out.println("\nAuthenticator Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						try {
							r7.exec(rm_db);
							System.out.println("\nDatabase Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						try {
							r7.exec(rm_Image);
							System.out.println("\nImage Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 5:
						try {
							r7.exec(rm_pers);
							System.out.println("\nPersistence Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 6:
						try {
							r7.exec(rm_recom);
							System.out.println("\nRecommender Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 7:
						try {
							r7.exec(rm_reg);
							System.out.println("\nRegistry Service Removed");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case 8:
						try {
							r7.exec(rm_web);
							System.out.println("\nWebUi Service Removed");
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
				System.out.print("\n<<<<<<<<<<Automns Swarm Shutdown Menu>>>>>>>>>" +
						"\n|1| Remove Swarm Workers & Erase Docker Images" +
						"\n|2| Remove Swarm Manager & Erase Docker Images" +
						"\n|0| Return" +
						"\nEnter Option Number >> ");
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
						String leave = "sudo docker swarm leave -f";
						String Erase = "sudo docker rmi $(sudo docker images -q)";
						try {
							r8.exec(leave);
							r8.exec(Erase);
							System.out.println("\nManager Node Left Swarm & Service/s Images Deleted");
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
			System.out.println("\nNew Message From: (" + msg.getSender().getName() + ") \n" + msg.getContent());

			return true;
		}
		return false;
	}
	protected void setup() {
		String header = "\n" +
				"             █████╗ ██╗   ██╗████████╗ ██████╗ ███╗   ███╗███╗   ██╗███████╗             \n" +
				"            ██╔══██╗██║   ██║╚══██╔══╝██╔═══██╗████╗ ████║████╗  ██║██╔════╝             \n" +
				"            ███████║██║   ██║   ██║   ██║   ██║██╔████╔██║██╔██╗ ██║███████╗             \n" +
				"            ██╔══██║██║   ██║   ██║   ██║   ██║██║╚██╔╝██║██║╚██╗██║╚════██║             \n" +
				"            ██║  ██║╚██████╔╝   ██║   ╚██████╔╝██║ ╚═╝ ██║██║ ╚████║███████║             \n" +
				"            ╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═══╝╚══════╝             \n" +
				"                                                                                         \n" +
				" ██████╗ ██████╗  ██████╗ ██████╗ ██████╗ ██╗███╗   ██╗ █████╗ ████████╗ ██████╗ ██████╗ \n" +
				"██╔════╝██╔═══██╗██╔═══██╗██╔══██╗██╔══██╗██║████╗  ██║██╔══██╗╚══██╔══╝██╔═══██╗██╔══██╗\n" +
				"██║     ██║   ██║██║   ██║██████╔╝██║  ██║██║██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝\n" +
				"██║     ██║   ██║██║   ██║██╔══██╗██║  ██║██║██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗\n" +
				"╚██████╗╚██████╔╝╚██████╔╝██║  ██║██████╔╝██║██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║\n" +
				" ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝\n" +
				"                                                                                         \n" +
				"                                  >Press Enter to Start<";
		try {
			TimeUnit.SECONDS.sleep(1);
			for (int i=0; i<header.length(); i++){
				System.out.print(header.charAt(i));
			}
			Scanner enter = new Scanner(System.in);
			System.out.print("");
			enter.nextLine();
		}catch (InterruptedException e) {
		}

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				try {
					TimeUnit.SECONDS.sleep(1);
				}catch (InterruptedException e) {
				}
				while (checkmsg());
				try {
					try {
						TimeUnit.SECONDS.sleep(1);
					}catch (InterruptedException e) {
					}
					menu();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
