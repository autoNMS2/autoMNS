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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jadex.bridge.ComponentIdentifier;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.annotation.OnStart;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.Argument;
import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.OnMessage;
import jadex.commons.SUtil;

@Description("This agent will initalise the system and then message another agent once complete")

@Arguments({
	@Argument(name="receiver", clazz=IComponentIdentifier.class, description="The component receiver of Agent2."),
	@Argument(name="missed_max", clazz=int.class, description="Maximum number of allowed missed replies", defaultvalue="3"),
	@Argument(name="timeout", clazz=long.class, description="Timeout for reply", defaultvalue="1000"),
})

@Agent(type = "bdi")
public class agent {

	@Agent
	protected IInternalAccess agent;
	
	/** The receiver. */
	protected IComponentIdentifier receiver;
	
	/** The difference between sent messages and received replies. */
	protected int dif;
	
	/** Hashset with conversation ids of sent messages. */
	protected Set<String> sent;
	
	
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

	@OnInit
	private void agentStartup(IBDIAgentFeature bdi) {
		bdi.dispatchTopLevelGoal(new InitSystem());
		bdi.dispatchTopLevelGoal(new ScanSystem());

	}
	
	@OnStart
	public IFuture<Void> executeBody()
	{
		final Future<Void> ret = new Future<Void>();
		
		receiver = (IComponentIdentifier)agent.getFeature(IArgumentsResultsFeature.class).getArguments().get("receiver");
		final int missed_max = ((Number)agent.getFeature(IArgumentsResultsFeature.class).getArguments().get("missed_max")).intValue();
		final long timeout = ((Number)agent.getFeature(IArgumentsResultsFeature.class).getArguments().get("timeout")).longValue();
		sent = new HashSet<String>();
		
		final IComponentStep<Void> step = new IComponentStep<Void>()
		{
			public IFuture<Void> execute(IInternalAccess ia)
			{
				if(dif>missed_max)
				{
					agent.getLogger().warning("Agent2 does not respond: "+receiver);
					ret.setResult(null);
				}
				else
				{
					String convid = SUtil.createUniqueId(agent.getId().getName());
					Map<String, Object> msg = new HashMap<String, Object>();
					msg.put(SFipa.CONTENT, "Hello am Agent1");
					//msg.put(SFipa.PERFORMATIVE, SFipa.QUERY_IF);
					//msg.put(SFipa.CONVERSATION_ID, convid);
					//msg.put(SFipa.RECEIVERS, new IComponentIdentifier[]{receiver});
					// sender is used for reply
					msg.put(SFipa.SENDER, agent.getId());
					dif++;
					sent.add(convid);
					agent.getFeature(IMessageFeature.class).sendMessage(msg, receiver).get();
					agent.getFeature(IExecutionFeature.class).waitForDelay(timeout, this);
				}
				return IFuture.DONE;
			}
		};
		
		if(receiver==null)
		{
			receiver = new ComponentIdentifier("Agent2", agent.getId().getParent());
		}

		agent.getFeature(IExecutionFeature.class).scheduleStep(step);
		
		return ret;
	}
	
}