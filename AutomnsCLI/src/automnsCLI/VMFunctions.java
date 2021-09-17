package automnsCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class VMFunctions {

	public static void addVMs() throws IOException {

		Menus.clearScreen();
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to AutoMNS\n" + "Please enter the number of VMs to initialise: ");

		int y = input.nextInt();
		List<String> vms = new ArrayList<>();

		Scanner ipScanner = new Scanner(System.in);

		for (int x = 0; x < y; x++) {
			System.out.println("Welcome to AutoMNS\n" + "Please enter the ip of your VM" + (x + 1) + ": ");
			vms.add(ipScanner.next());
		}

		String privateKey;
		System.out.println("Enter the file path of your private key: ");
		privateKey = input.next();
		
		String[] commands = {"sudo apt-get update -y\n sudo apt-get install -y\r\n"
				+ "            apt-transport-https -y\r\n"
				+ "            ca-certificates -y\r\n"
				+ "            curl -y\r\n"
				+ "            gnupg -y\r\n"
				+ "            lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v", "sudo docker swarm init --advertise-addr " + vms.get(0)
				};
		
		for (int j = 0; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, commands[0]);
		}

		System.out.println("VMs initialised, woo!");
		
		//add the first vm to a swarm and get the swarm token
		String output = SSH(vms.get(0), privateKey, commands[1]);	
		String joinToken = "sudo " + output.substring(142, 273);
		//print for error checking purposes
		System.out.println("Token: " + joinToken);
		//add other vms to swarm
		for (int j = 1; j < vms.size(); j++) {
			SSH(vms.get(j), privateKey, joinToken);
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
			//System.out.println("error: " + errorBuffer.toString());

			channel.disconnect();
			session.disconnect();
			return output;
		} catch (JSchException e) {
			throw new RuntimeException("Error during SSH command execution. Command: " + command);
		}

	}

}
