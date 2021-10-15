package automnsCLI;
import java.awt.Desktop;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import com.jcraft.jsch.*;

public class VMFunctions {
	
	private static String localKeyFilePath;
	
	public static String getLocalKeyFilePath() {
		return localKeyFilePath;
	}
	
	public static void setLocalKeyFilePath(String localPath) {
		VMFunctions.localKeyFilePath = localPath;
	}
	
	private static String repoKeyFilePath;
	
	public static String getRepoKeyFilePath() {
		return repoKeyFilePath;
	}
	
	public static void setRepoKeyFilePath(String repoPath) {
		VMFunctions.repoKeyFilePath = repoPath;
	}
	
	private static List<String> VMPublicIps;
	
	public static List<String> getVMPublicIps(){
		return VMPublicIps;
	}
	
	public static void setVMPublicIps(List<String> newPublicVMS) {
		VMFunctions.VMPublicIps = newPublicVMS;
	}
	private static List<String> VMPrivateIps;
	
	public static List<String> getVMPrivateIps(){
		return VMPrivateIps;
	}
	
	public static void setVMPrivateIps(List<String> newPrivateVMS) {
		VMFunctions.VMPrivateIps = newPrivateVMS;
	}
	
	//this array of commands is used to run on the vms to bring them up to a working state 
	public static final String[] vmCommands = {
			"sudo apt-get update -y\n sudo apt-get install -y\r\n" + "apt-transport-https -y\r\n"
			+ "ca-certificates -y\r\n" + "curl -y\r\n" + "gnupg -y\r\n"
			+ "lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v",
			"sudo docker swarm init --advertise-addr ",
			"sudo apt-get install git -y\n sudo git clone https://github.com/autoNMS2/autoMNS.git",
			"sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore",
			"sudo apt update\n sudo apt install default-jre -y\n sudo apt install default-jdk -y\n",
			"javac -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/*.java",
			"java -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar:classes jade.Boot -agents coordinator:automnsCLI.coordinator"
	};

	//This function runs all the necessary commands on the virtual machine to bring them up
	//to the working level we need them at to run the program
	public static void addVMs() throws IOException {
		//create a list variable and read the user's ips from the file
			if (getVmConfig()) {
				//update vms
				updateVMs();
				// add the first vm to a swarm and get the swarm token
				addVMsToSwarm();
				// install git and pull repository on vms
				gitSetup();
				// install java on vms
				javaSetup();
				//wait for user input before returning to main menu
				System.out.println("VMs initialised.\n" + "Press any key and then enter to return to main menu...");
				String userInput;
				Scanner input = new Scanner(System.in);
				userInput = input.next();
				Menus.MainMenu();
			}
			else {
				System.out.println("VM Config file incorrectly formatted");
			}
		}
	
	public static void updateVMs() throws IOException {
		//runs commands on each of the user's vms that updates them
		List<String> localVMs = getVMPublicIps();
		String privateKey = getLocalKeyFilePath();
		for (int j = 0; j < localVMs.size(); j++) {
			//	System.out.println(j + " " + privateKey + " " + vmCommands[0]);
			SSH(localVMs.get(j), privateKey, vmCommands[0]);
		}
	}
	
	public static void addVMsToSwarm() throws IOException {
		//makes the first vm in the list a swarm manager, then adds all other vms to swarm as workers
		List<String> localVMs = getVMPublicIps();
		String privateKey = getLocalKeyFilePath();
		// add the first vm to a swarm and get the swarm token
		String output = SSH(localVMs.get(0), privateKey, vmCommands[1] + localVMs.get(0));
		String joinToken = "sudo " + output.substring(142, 273);
		// add other vms to swarm
		for (int j = 1; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, joinToken);
		}
	}
	
	public static void gitSetup() throws IOException {
		//installs git on vms then pulls autoMNS repository to vms
		List<String> localVMs = getVMPublicIps();
		String privateKey = getLocalKeyFilePath();
		for (int j = 0; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[2]);
		}
	}
	
	public static void javaSetup() throws IOException {
		//installs jdk and jre on each vm
		List<String> localVMs = getVMPublicIps();
		String privateKey = getLocalKeyFilePath();
		for (int j = 0; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[4]);
		}
	}
	
	//this function reads in the user's vm configuration file and saves the ip addresses 
	//to a list of strings and returns it
	public static boolean getVmConfig(){
		String localKeyPath;
		String repoKeyPath;
		
		List<String> ipConfigList = new ArrayList<>();
		List<String> localPrivateVmsList = new ArrayList<>();
		List<String> localPublicVmsList = new ArrayList<>();

		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to AutoMNS\n" + "Please enter the path of your VM config file: ");

		String filePath = input.nextLine();
		try {
		      File myObj = new File(filePath);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  ipConfigList.add(myReader.nextLine().replaceAll("\\s+",""));
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		localKeyPath = ipConfigList.get(0);
		repoKeyPath = ipConfigList.get(1);
		for (int i = 2; i < ipConfigList.size(); i+=2) {
			localPrivateVmsList.add(ipConfigList.get(i));
			localPublicVmsList.add(ipConfigList.get(i+1));
		}
		setLocalKeyFilePath(localKeyPath);
		setRepoKeyFilePath(repoKeyPath);
		setVMPrivateIps(localPrivateVmsList);
		setVMPublicIps(localPublicVmsList);
		return true;	
	}
	

	public static void initialiseAgents() throws IOException {		
		//get the list of vms from the user, if the config file has not yet been input then the length will be 0 and the user will be asked to input
		List<String> publicVms = getVMPublicIps();
		if (publicVms == null) {
			getVmConfig();
			publicVms = getVMPublicIps();
		}
		List<String> privateVms = getVMPrivateIps();
		String repoPrivateKey = getRepoKeyFilePath();

		//The following array contains the commands necessary to initialise the coordinator agent on the first vm provided by the user
		String[] agentCommands = 
			{ "javac -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/*.java",
			  "java -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar:classes jade.Boot -host " + privateVms.get(0) + " -port 1099 -agents 'coordinator:automnsCLI.JAMEScoordinator("};	
		//the following for loops adds the users public and private vm ips as arguments on to the end of the java command
		//this allows the coordinator agent to access these ip addresses
		for (int i = 1; i < privateVms.size(); i++) {
			agentCommands[1] += privateVms.get(i) + ",";
		}
		for (int i = 1; i < publicVms.size(); i++) {
			agentCommands[1] += publicVms.get(i) + ",";
		}
		//add a closing bracket to the end of the java command
		agentCommands[1] += repoPrivateKey + ")'";

		//get the local ssh key file path to ssh into coordinator
		String localPrivateKey = getLocalKeyFilePath();
		// intialise the platform on the coordinator agent
		// coordinator should deploy other agents from it's own code
		SSH(publicVms.get(0), localPrivateKey, agentCommands[0]);
		shellSSH(publicVms.get(0), localPrivateKey, agentCommands[1]);
		System.out.println("Agents Deployed");
		Menus.MainMenu();
	}
	
	public static void launchApplication() throws IOException {
		List<String> vms = getVMPublicIps();
		System.out.println("Launching App");
		String URL = "https://" + vms.get(1) + "/8080";
		openWebpage(URL);
		Menus.MainMenu();
	}

	public static void openWebpage(String urlString) {
	    try {
	        Desktop.getDesktop().browse(new URL(urlString).toURI());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// The following code can be used to ssh into a VM and create an interactive ssh shell
	// You will need the IP address, a private key file, and a command to run
	public static ChannelShell shellSSH(String ip, String filePath,String cmd) throws IOException {

		String username = "ubuntu";
		JSch jsch = new JSch();
		Session session = null;
		String host = ip;
		String privateKeyPath = filePath;
		Channel channel;
		try {
			jsch.addIdentity(privateKeyPath);
			session = jsch.getSession(username, host, 22);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		} catch (JSchException e) {
			throw new RuntimeException("Failed to create Jsch Session object.", e);
		}
		try {
			session.connect(3000);
			channel = session.openChannel("shell");
			((ChannelShell) channel).setPtyType("vt102");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			channel.connect();
			ps.println(cmd);
		} catch (JSchException e) {
			System.out.println(e.getCause());
			throw new RuntimeException(e);
		}
		return (ChannelShell) channel;
	}
	
	public static void noOutputSSH(String ip, String filePath, String Command) throws IOException {
		// The following code can be used to ssh into a VM and run a command
		// You will need the VM username, IP address, and a private key file

		String username = "ubuntu";

		JSch jsch = new JSch();
		Session session = null;

		String host = ip;
		String privateKeyPath = filePath;
		String command = Command;

		try {
			jsch.addIdentity(privateKeyPath);
			session = jsch.getSession(username, host, 22);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		} catch (JSchException e) {
			throw new RuntimeException("Failed to create Jsch Session object.", e);
		}
		try {
			session.connect();
			System.out.println("session connected.....");
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setPty(false);
			channel.connect();
		} catch (JSchException e) {
			System.out.println(e.getCause());
			throw new RuntimeException("Error during SSH command execution. Command: " + command);
		}
	}

	public static String SSH(String ip, String filePath, String Command) throws IOException {
		// The following code can be used to ssh into a VM and run a command
		// You will need the VM username, IP address, and a private key file

		String username = "ubuntu";

		JSch jsch = new JSch();
		Session session = null;

		String host = ip;
		String privateKeyPath = filePath;
		String command = Command;

		try {
			jsch.addIdentity(privateKeyPath);
			session = jsch.getSession(username, host, 22);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		} catch (JSchException e) {
			throw new RuntimeException("Failed to create Jsch Session object.", e);
		}
		try {
			session.connect();
			System.out.println("session connected.....");
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setPty(false);
			StringBuilder outputBuffer = new StringBuilder();
			StringBuilder errorBuffer = new StringBuilder();

			InputStream in = channel.getInputStream();
			InputStream err = channel.getExtInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					outputBuffer.append(new String(tmp, 0, i));
				}
				while (err.available() > 0) {
					int i = err.read(tmp, 0, 1024);
					if (i < 0)
						break;
					errorBuffer.append(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					if ((in.available() > 0) || (err.available() > 0))
						continue;
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			String output = outputBuffer.toString();
			System.out.println("output: " + output);
			System.out.println("error: " + errorBuffer.toString());

			channel.disconnect();
			session.disconnect();
			return output;
		} catch (JSchException e) {
			System.out.println(e.getCause());
			throw new RuntimeException("Error during SSH command execution. Command: " + command);
		}
	}
}