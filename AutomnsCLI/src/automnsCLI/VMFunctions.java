package automnsCLI;
import java.awt.Desktop;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import com.jcraft.jsch.*;

public class VMFunctions {
	
	private static List<String> VMs;
	
	public static List<String> getVMS(){
		return VMs;
	}
	
	public static void setVMS(List<String> newVMS) {
		VMFunctions.VMs = newVMS;
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
		List<String> vms = new ArrayList<>();
		vms = getVmConfig();
		//set the vms list
		setVMS(vms);
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
	
	public static void updateVMs() throws IOException {
		//runs commands on each of the user's vms that updates them
		List<String> localVMs = getVMS();
		String privateKey = localVMs.get(0);
		for (int j = 1; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[0]);
		}
	}
	
	public static void addVMsToSwarm() throws IOException {
		//makes the first vm in the list a swarm manager, then adds all other vms to swarm as workers
		List<String> localVMs = getVMS();
		String privateKey = localVMs.get(0);
		// add the first vm to a swarm and get the swarm token
		String output = SSH(localVMs.get(1), privateKey, vmCommands[1] + localVMs.get(1));
		String joinToken = "sudo " + output.substring(142, 273);
		// add other vms to swarm
		for (int j = 2; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, joinToken);
		}
	}
	
	public static void gitSetup() throws IOException {
		//installs git on vms then pulls autoMNS repository to vms
		List<String> localVMs = getVMS();
		String privateKey = localVMs.get(0);
		for (int j = 1; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[2]);
		}
	}
	
	public static void javaSetup() throws IOException {
		//installs jdk and jre on each vm
		List<String> localVMs = getVMS();
		String privateKey = localVMs.get(0);
		for (int j = 1; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[4]);
		}
	}
	
	//this function reads in the user's vm configuration file and saves the ip addresses 
	//to a list of strings and returns it
	public static List<String> getVmConfig(){
		List<String> localVmsList = new ArrayList<>();
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to AutoMNS\n" + "Please enter the path of your VM config file: ");

		String filePath = input.nextLine();
		try {
		      File myObj = new File(filePath);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		    	  localVmsList.add(myReader.nextLine().replaceAll("\\s+",""));
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return localVmsList;	
	}
	

	public static void initialiseAgents() throws IOException {		
		//The following array contains the commands necessary to initialise the coordinator agent on the first vm provided by the user
		String[] agentCommands = 
			{ "javac -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar -d classes autoMNS/AutomnsCLI/src/automnsCLI/*.java",
			  "java -cp autoMNS/jade/lib/jade.jar:autoMNS/jade/lib/jsch-0.1.55.jar:classes jade.Boot -agents coordinator:automnsCLI.coordinator"};
		//get the list of vms from the user, if the config file has not yet been input then the length will be 0 and the user will be asked to input
		List<String> vms = getVMS();
			if (vms.size() == 0) {
				vms = getVmConfig();
		}
		//first element in vms should always be private key path
		String privateKey = vms.get(0);
		// intialise the platform on the coordinator agent
		// coordinator should deploy other agents from it's own code
		SSH(vms.get(1), privateKey, agentCommands[0]);
		SSH(vms.get(1), privateKey, agentCommands[1]);
		System.out.println("Agents Deployed");
		Menus.MainMenu();
	}
	
	public static void launchApplication() throws IOException {
		List<String> vms = getVMS();
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
