package automns.agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.*;
import jadex.bdiv3.runtime.*;
import jadex.bridge.service.annotation.*;
import jadex.micro.annotation.*;
import jadex.quickstart.cleanerworld.environment.*;
import jadex.quickstart.cleanerworld.gui.*;
import java.io.InputStream;


@Description("This agent will initalise the system and then message another agent once complete")
@Agent(type = "bdi")
public class agent {

	@Goal(deliberation = @Deliberation(inhibits = { ScanSystem.class }))
	class InitSystem {

	}

	@Plan(trigger = @Trigger(goals = InitSystem.class))
	private void performInitSystem() throws IOException, InterruptedException {
		System.out.println("Initialising system...");

		/**
		 * String cmd = "docker-compose -f C:\\Users\\James\\docker-compose_default.yaml
		 * up -d"; Runtime run = Runtime.getRuntime(); Process pr = run.exec(cmd);
		 * pr.waitFor(); BufferedReader buf = new BufferedReader(new
		 * InputStreamReader(pr.getInputStream())); String line = ""; while ((line =
		 * buf.readLine()) != null) { System.out.println(line); }
		 **/
		try {
			// Run a command
			Process process = Runtime.getRuntime()
					.exec("cmd /c docker-compose -f C:\\Users\\James\\docker-compose_default.yaml up -d");

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
			} else {
				// abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//The following code can be used to ssh into a VM and run a command
		//You will need the VM username, IP address, and a private key file 
		/**
		 * String username = "ubuntu"; String host = "203.101.228.168";
		 * 
		 * JSch jsch = new JSch(); Session session = null;
		 * 
		 * String privateKeyPath = "C:\\Users\\James\\Downloads\\automns3.ppk";
		 * 
		 * try { jsch.addIdentity(privateKeyPath); session = jsch.getSession(username,
		 * host, 22); session.setConfig("PreferredAuthentications",
		 * "publickey,keyboard-interactive,password"); java.util.Properties config = new
		 * java.util.Properties(); config.put("StrictHostKeyChecking", "no");
		 * session.setConfig(config); } catch (JSchException e) { throw new
		 * RuntimeException("Failed to create Jsch Session object.", e); } String
		 * command = "whoami"; try { session.connect(); System.out.println("session
		 * connected....."); Channel channel = session.openChannel("exec");
		 * ((ChannelExec) channel).setCommand(command); ((ChannelExec)
		 * channel).setPty(false); StringBuilder outputBuffer = new StringBuilder();
		 * StringBuilder errorBuffer = new StringBuilder();
		 * 
		 * InputStream in = channel.getInputStream(); InputStream err =
		 * channel.getExtInputStream();
		 * 
		 * channel.connect();
		 * 
		 * byte[] tmp = new byte[1024]; while (true) { while (in.available() > 0) { int
		 * i = in.read(tmp, 0, 1024); if (i < 0) break; outputBuffer.append(new
		 * String(tmp, 0, i)); } while (err.available() > 0) { int i = err.read(tmp, 0,
		 * 1024); if (i < 0) break; errorBuffer.append(new String(tmp, 0, i)); } if
		 * (channel.isClosed()) { if ((in.available() > 0) || (err.available() > 0))
		 * continue; System.out.println("exit-status: " + channel.getExitStatus());
		 * break; } try { Thread.sleep(1000); } catch (Exception ee) { } }
		 * 
		 * System.out.println("output: " + outputBuffer.toString());
		 * //System.out.println("error: " + errorBuffer.toString());
		 * 
		 * channel.disconnect(); session.disconnect(); } catch (JSchException e) { throw
		 * new RuntimeException("Error during SSH command execution. Command: " +
		 * command); }
		 **/
	}

	@Goal(recur = false)
	class ScanSystem {
	}

	@Plan(trigger = @Trigger(goals = ScanSystem.class))
	private void performScanSystem() throws IOException, InterruptedException {
		System.out.println("Scanning system...");
		try {
			// Run a command
			Process process = Runtime.getRuntime().exec("cmd /c docker container ls");

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output);
			} else {
				// abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@OnStart
	private void agentStartup(IBDIAgentFeature bdi) {
		bdi.dispatchTopLevelGoal(new InitSystem());
		bdi.dispatchTopLevelGoal(new ScanSystem());

	}
}