package automnsCLI.multi;



import java.io.IOException;
import java.io.InputStreamReader;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.io.BufferedReader;
import java.io.FileWriter;

public class db_agent extends Agent
{
	protected void setup() {
		// create behaviour for receive and send message to Sender
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				//reads and output the received msg
				ACLMessage msg = receive();
				if (msg != null) {
					String content = msg.getContent();

					System.out.println(" Message to " + myAgent.getLocalName()
							+ " received. Message is : " + content);
					switch (content) {
						case "Deploy Agents":
							//msgContent = "Deploy Services";
							System.out.println("Deploying Agents");
							break;
						case "Get Services Update":
							Runtime r = Runtime.getRuntime();
							String cmd = "docker ps";
							try {
								Process proc = r.exec(cmd);
								BufferedReader stdInput = new BufferedReader(new
										InputStreamReader(proc.getInputStream()));

								BufferedReader stdError = new BufferedReader(new
										InputStreamReader(proc.getErrorStream()));

								// Read the output from the command
								System.out.println("Here is the standard output of the command:\n");
								String s = null;
								while ((s = stdInput.readLine()) != null) {
									System.out.println(s);
									FileWriter log=new FileWriter("autoMNS/log.txt");
									log.write(s);
									System.out.println("Writing successful");
									//close the file
									log.close();
								}

								// Read any errors from the attempted command
								System.out.println("Here is the standard error of the command (if any):\n");
								while ((s = stdError.readLine()) != null) {
									System.out.println(s);
								}
							} catch (IOException e)
							{
								e.printStackTrace();
							}
	                                                	Runtime r1 = Runtime.getRuntime();
							String cmd1 = "cd autoMNS/\n sudo git commit -m "db agent Update" log.txt\n sudo git push -u origin master ";
							try {
								r1.exec(cmd1);
								}
							 catch (IOException e)
							{
								e.printStackTrace();
							}
							//sends a reply to the sender
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.INFORM);
							reply.setContent("Log file updated");
							send(reply);
							break;
						case "Kill services":
							//msgContent = "Kill services";
							System.out.println("Killinggggg services");
							break;
					}
				}
			}
		});
	}
}
