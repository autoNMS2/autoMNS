package automnsCLI;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import com.jcraft.jsch.*;

public class VMFunctions {
	
	public static void addVMs() throws IOException {
		List<String> vms = new ArrayList<>();
		Menus.clearScreen();
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to AutoMNS\n" + "Please enter the path of your VM config file: ");

		String filePath = input.nextLine();
		try {
		      File myObj = new File(filePath);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        vms.add(myReader.nextLine().replaceAll("\\s+",""));
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		//first element in vms should always be private key path
		System.out.println(vms.get(0));
		String privateKey = vms.get(0);

		String[] commands = {
				"sudo apt-get update -y\n sudo apt-get install -y\r\n" + "apt-transport-https -y\r\n"
						+ "ca-certificates -y\r\n" + "curl -y\r\n" + "gnupg -y\r\n"
						+ "lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v",
				"sudo docker swarm init --advertise-addr " + vms.get(1),
				"sudo apt-get install git -y\n sudo git clone https://github.com/autoNMS2/autoMNS.git",
				"sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore",
				"sudo apt update\r\n" + "sudo apt install default-jre -y\r\n" + "sudo apt install default-jdk -y\r\n",
				"javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/jade/src/test0/send0.java\r\n "
						+ MessageFormat.format(
								"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -host {0} -port 1099 -agents coordinator:test0.send0\r\n",
								vms.get(1)),
				"javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/jade/src/test0/receive0.java\r\n "
						+ "java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -agents a1:test0.receive0\r\n" };

		for (int j = 1; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, commands[0]);
		}

		// add the first vm to a swarm and get the swarm token
		String output = SSH(vms.get(1), privateKey, commands[1]);
		String joinToken = "sudo " + output.substring(142, 273);
		// print for error checking purposes
		System.out.println("Token: " + joinToken);
		// add other vms to swarm
		for (int j = 2; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, joinToken);
		}
		// install git and pull repository on vms
		for (int j = 1; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, commands[2]);
		}
		// install java on vms
		for (int j = 1; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, commands[4]);
		}
		// initialise agents
		for (int j = 2; j < vms.size(); j++) {
		//	SSH(vms.get(j), privateKey, commands[6]);
		}
		// initilise coordinator
		//SSH(vms.get(1), privateKey, commands[5]);

		// deploy the application on the swarm
		// SSH(vms.get(0), privateKey, commands[3]);
		// System.out.println("Application container stack deployed");
        System.out.println("VMs initialised.\n" + "Press enter to return to main menu...");
		String userInput;
		userInput = input.next();
		Menus.MainMenu();
		}
	

	public static void initialiseAgents() throws IOException {
		Menus.clearScreen();
		/*
		 * Scanner input = new Scanner(System.in);
		 * System.out.println("Welcome to AutoMNS\n" +
		 * "Please enter the number of VMs to demonstrate: ");
		 * 
		 * int y = input.nextInt(); List<String> vms = new ArrayList<>();
		 * 
		 * Scanner ipScanner = new Scanner(System.in);
		 * 
		 * for (int x = 0; x < y; x++) { System.out.println("Welcome to AutoMNS\n" +
		 * "Please enter the ip of your VM" + (x + 1) + ": ");
		 * vms.add(ipScanner.next()); }
		 */
		// String privateKey;
		// System.out.println("Enter the file path of your private key: ");
		// privateKey = input.next();
		String jamesPrivateKey = "C:\\Users\\James\\Downloads\\test.ppk";

		// intialise the platform on the reciever agent
		SSH("18.232.183.161", jamesPrivateKey,
				"javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/jade/src/test0/receive0.java");
		SSH("18.232.183.161", jamesPrivateKey,
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -agents a1:test0.receive0");

		// initialise the sender agent and send a message
		SSH("44.195.186.234", jamesPrivateKey,
				"javac -classpath autoMNS/jade/lib/jade.jar -d classes autoMNS/jade/src/test0/send0.java");
		SSH("44.195.186.234", jamesPrivateKey,
				"java -cp autoMNS/jade/lib/jade.jar:classes jade.Boot -container -host 172.31.29.138 -port 1099 -agents coordinator:test0.send0");

	}

	public static void interactiveShell() throws IOException {
		//ShellSSH("3.91.56.84", "C:\\Users\\James\\Downloads\\test.ppk");
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
			//System.out.println("error: " + errorBuffer.toString());

			channel.disconnect();
			session.disconnect();
			return output;
		} catch (JSchException e) {
			throw new RuntimeException("Error during SSH command execution. Command: " + command);
		}
	}
}