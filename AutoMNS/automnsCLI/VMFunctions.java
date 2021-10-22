package automnsCLI;
import java.awt.Desktop;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
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

	public static List<String> getVMPublicIps() {
		return VMPublicIps;
	}

	public static void setVMPublicIps(List<String> newPublicVMS) {
		VMFunctions.VMPublicIps = newPublicVMS;
	}

	private static List<String> VMPrivateIps;

	public static List<String> getVMPrivateIps() {
		return VMPrivateIps;
	}

	public static void setVMPrivateIps(List<String> newPrivateVMS) {
		VMFunctions.VMPrivateIps = newPrivateVMS;
	}

	//this array of commands is used to run on the vms to bring them up to a working state 
	public static final String[] vmCommands =
			{
			"sudo apt-get update -y\n sudo apt-get install -y\r\n" + "apt-transport-https -y\r\n"
					+ "ca-certificates -y\r\n" + "curl -y\r\n" + "gnupg -y\r\n"
					+ "lsb-release -y\n sudo apt-get install docker.io -y\nsudo docker -v",				// 0
			"sudo docker swarm init --advertise-addr ",													// 1
			"sudo apt-get install git -y\n sudo git clone https://github.com/autoNMS2/autoMNS.git",		// 2
			"sudo docker stack deploy --compose-file autoMNS/Prototype/lib/Services/all.yaml TeaStore",	// 3
			"sudo apt update\n sudo apt install default-jre -y\n sudo apt install default-jdk -y\n"	// 4
			};

	/*
	This function runs all the necessary commands on the virtual machines to bring them up
	to the working level we need them at to run the program
	*/
	public static int addVMs() throws IOException {
		//create a list variable and read the user's ips from the file
		System.out.println("Initialising Virtual Machines");
		if (getVmConfig(inputVMConfigPath())) {
			//update vms
			updateVMs();
			// add the first vm to a swarm and get the swarm token
			addVMsToSwarm();
			// install git and pull repository on vms
			gitSetup();
			// install java on vms
			javaSetup();
			//wait for user input before returning to main menu
			Scanner enter = new Scanner(System.in);
			System.out.print("VMs initialised \nPress Enter to Continue..");
			//pause until user press enter
			enter.nextLine();
			Menus.MainMenu();
		} else {
			System.out.println("VM Config file incorrectly formatted");
		}
		return 1;
	}

	public static boolean checkDirectoryExists(String ip, String directory) throws IOException
	{
		String command = "find . -iname " + directory;
		String output = SSH(ip, getLocalKeyFilePath(), command, false);
		return output.contains(directory);
	}

	public static void updateVMs() throws IOException {
		//runs update command on each of the user's vms
		for (int j = 0; j < getVMPublicIps().size(); j++) {
			if (!checkDirectoryExists(getVMPublicIps().get(j), "docker"))
			{
				System.out.println("No docker installation detected, installing...");
				SSH(getVMPublicIps().get(j), getLocalKeyFilePath(), vmCommands[0], true);
			}
			else {
				System.out.println("VM already has docker installed.");
			}
		}
	}

	public static boolean addVMsToSwarm() throws IOException {
		/*
		makes the first vm in the list a swarm manager, then adds all other vms to swarm as workers
	    add the first vm to a swarm and get the swarm token
	    */
		String joinToken = getJoinToken();
		if (!joinToken.equals(""))
		{
			// if a join token is returned continue initialising the swarm, otherwise the swarm should already be initialised so skip
			joinSwarmWithToken(joinToken);
			return true;	// created a new swarm
		}
		else return false;	// did not create a swarm, does not tell you why though
	}

	public static String getJoinToken() throws IOException
	{
		//	also adds first vm to swarm
		//	vmCommands[1]
		SSH(getVMPublicIps().get(0), getLocalKeyFilePath(), vmCommands[1] + getVMPublicIps().get(0), true);

		String command = "sudo docker swarm join-token worker";
		String output = SSH(getVMPublicIps().get(0), getLocalKeyFilePath(), command, false);

		output = parseJoinToken(output);

		if (output.contains("docker swarm join")) {    // only try to add nodes if the token is returned
			// add other vms to swarm
			return output;
		}
		else return "";
	}

	public static String parseJoinToken(String output)
	{
		String[] splitOutput = output.split("\n");

		for (int i = 0; i < splitOutput.length; i++)
		{
			if (splitOutput[i].contains("docker swarm join"))	// search for correct line
			{
				return "sudo" + output.split("\n")[i];
			}
		}
		return "";
	}

	public static void joinSwarmWithToken(String joinToken) throws IOException
	{
		for (int j = 1; j < getVMPublicIps().size(); j++) {
			SSH(getVMPublicIps().get(j), getLocalKeyFilePath(), joinToken, true);
		}
	}

	public static void gitSetup() throws IOException {
		//installs git on vms then pulls autoMNS repository to vms
		for (int j = 0; j < getVMPublicIps().size(); j++) {
			if (!checkDirectoryExists(getVMPublicIps().get(j), "autoMNS")) {
				System.out.println("autoMNS repository was not detected, installing...");
				SSH(getVMPublicIps().get(j), getLocalKeyFilePath(), vmCommands[2], true);
			} else {
				System.out.println("VM already has repository installed.");
			}
		}
	}

	public static void javaSetup() throws IOException {
		//installs jdk and jre on each vm
		List<String> localVMs = getVMPublicIps();
		String privateKey = getLocalKeyFilePath();
		for (int j = 0; j < localVMs.size(); j++) {
			SSH(localVMs.get(j), privateKey, vmCommands[4], true);
		}
	}
	/*
	this function reads in the user's vm configuration file and saves the ip addresses
	to a list of strings and returns it
	*/
	public static String inputVMConfigPath() {
		Scanner input = new Scanner(System.in);
		System.out.println("\n" + "Please enter the path of your VM config file >>  ");
		String filePath = input.nextLine();
		return filePath;
	}

	public static boolean getVmConfig(String filePath) {
		List<String> ipConfigList = new ArrayList<>();

		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				ipConfigList.add(myReader.nextLine().replaceAll("\\s+", ""));
			}
			myReader.close();
			AssignVMConfig(ipConfigList);
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			return false;
		}
	}

	public static void AssignVMConfig(List<String> ipConfigList) {
		String localKeyPath = ipConfigList.get(0);
		String repoKeyPath = ipConfigList.get(1);

		List<String> localPrivateVmsList = new ArrayList<>();
		List<String> localPublicVmsList = new ArrayList<>();
		for (int i = 2; i < ipConfigList.size(); i += 2) {
			localPrivateVmsList.add(ipConfigList.get(i));
			localPublicVmsList.add(ipConfigList.get(i + 1));
		}
		setLocalKeyFilePath(localKeyPath);
		setRepoKeyFilePath(repoKeyPath);
		setVMPrivateIps(localPrivateVmsList);
		setVMPublicIps(localPublicVmsList);
	}

	public static void checkVMConfig()
	{
		if (getVMPublicIps() == null) {
			getVmConfig(inputVMConfigPath());
		}
	}

	public static String[] getCoordinatorCompilationCommand()
	{
		//The following array contains the commands necessary to initialise the coordinator agent on the first vm provided by the user
		String[] agentCommands =
				{
				"javac -cp autoMNS/AutoMNS/lib/jade.jar:autoMNS/AutoMNS/lib/jsch-0.1.55.jar:autoMNS/AutoMNS/lib/ascii-table-1.1.0.jar -d classes autoMNS/AutoMNS/automnsCLI/*.java",
				"java -cp autoMNS/AutoMNS/lib/jade.jar:autoMNS/AutoMNS/lib/ascii-table-1.1.0.jar:autoMNS/AutoMNS/lib/jsch-0.1.55.jar:classes jade.Boot -host " + getVMPrivateIps().get(0) + " -port 1099 -agents 'coordinator:automnsCLI.coordinator("};
		/*
		the following for loops adds the users public and private vm ips as arguments on to the end of the java command
		this allows the coordinator agent to access these ip addresses
		*/
		agentCommands[1] += getCoordinatorParameters();
		agentCommands[1] += ")'";
		return agentCommands;
	}

	public static String getCoordinatorParameters()
	{
		String parameters = "";
		for (int i = 0; i < getVMPrivateIps().size(); i++) {
			parameters += getVMPrivateIps().get(i) + ",";
		}
		for (int i = 0; i < getVMPublicIps().size(); i++) {
			parameters += getVMPublicIps().get(i) + ",";
		}
		//add a closing bracket to the end of the java command
		parameters+= getRepoKeyFilePath();
		return parameters;
	}

	public static int initialiseAgents() throws IOException {
		//get the list of vms from the user, if the config file has not yet been input then the length will be 0 and the user will be asked to input
		System.out.println("Initialising Agents");
		checkVMConfig();

		String[] agentCommands = getCoordinatorCompilationCommand();

		// initialise the platform on the coordinator agent
		// coordinator should deploy other agents from it's own code
		SSH(getVMPublicIps().get(0), getLocalKeyFilePath(), agentCommands[0], true);
		shellSSH(getVMPublicIps().get(0), getLocalKeyFilePath(), agentCommands[1]);
		System.out.println("Agents Deployed");
		//	Menus.MainMenu();
		return 2;
	}

	// this might need to be removed !!!!!!!!!!!!!!!!!!!!!!
	public static int launchApplication() throws IOException {
		System.out.println("Launching App");
		checkVMConfig();

		String URL = "https://" + getVMPublicIps().get(1) + "/8080";
		openWebpage(URL);
		return 4;
		//	Menus.MainMenu();
	}
	// this might need to be removed !!!!!!!!!!!!!!!!!!!!!!
	public static void openWebpage(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean noOutputSSH(String host, String privateKeyPath, String command) throws IOException {
		// The following code can be used to ssh into a VM and run a command
		// You will need the VM username, IP address, and a private key file
		Session session = getSSHSession(host, privateKeyPath);
		return (runSSHCommand(session, command).isConnected());
	}

	public static String SSH(String host, String privateKeyPath, String command, boolean print) throws IOException {
		// The following code can be used to ssh into a VM and run a command
		// You will need the VM username, IP address, and a private key file
		Session session = getSSHSession(host, privateKeyPath);
		Channel channel = runSSHCommand(session, command);
		if (channel.isConnected()) {
			return getSSHOutput(channel, session, print);
		} else {
			throw new RuntimeException("Error during SSH command execution. \n Channel is not connected. \n Command: " + command);
		}
	}

	// The following code can be used to ssh into a VM and create an interactive ssh shell
	// You will need the IP address, a private key file, and a command to run
	public static ChannelShell shellSSH(String host, String privateKeyPath, String command) throws IOException {
		Session session = getSSHSession(host, privateKeyPath);
		return runShellCommand(session, command);
	}

	public static ChannelShell runShellCommand(Session session, String command) throws IOException
	{
		try {
			session.connect(3000);
			Channel channel = session.openChannel("shell");
			((ChannelShell) channel).setPtyType("vt102");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			channel.connect();
			ps.println(command);
			return (ChannelShell) channel;
		} catch (JSchException e) {
			System.out.println(e.getCause());
			throw new RuntimeException(e);
		}
	}

	public static Session getSSHSession(String host, String privateKeyPath)
	{
		String username = "ubuntu";
		Session session = null;
		JSch jsch = new JSch();

		try {
			jsch.addIdentity(privateKeyPath);
			session = jsch.getSession(username, host, 22);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			return session;
		} catch (JSchException e) {
			throw new RuntimeException("Failed to create Jsch Session object.", e);
		}
	}

	public static Channel runSSHCommand(Session session, String command)
	{
		try {
			session.connect();
			System.out.println(session.getHost() + " session connected.....");
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setPty(false);
			channel.connect();
			return channel;
		} catch (JSchException e) {
			System.out.println(e.getCause());
			throw new RuntimeException("Error during SSH command execution. Command: " + command);
		}
	}
	public static String getSSHOutput(Channel channel, Session session, boolean print) throws IOException
	{
		StringBuilder outputBuffer = new StringBuilder();
		StringBuilder errorBuffer = new StringBuilder();
		InputStream in = channel.getInputStream();
		InputStream err = channel.getExtInputStream();

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
		if (print) {
			System.out.println("output: " + output);
			System.out.println("error: " + errorBuffer.toString());
		}

		channel.disconnect();
		session.disconnect();
		return output;
	}
}


