package msgs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.fipa.SFipa;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.OnMessage;

@Agent
public class Agent2 
{

	@Agent
	protected IInternalAccess agent;

	@OnMessage
	public void messageArrived(Map<String, Object> msg)
	{
		if("Please initialise system".equals(msg.get(SFipa.CONTENT)))
		{
			System.out.println("New message to: "+agent.getId()+"\n Message  is: "+msg);
			System.out.println("Agent 2 is initialising the system");
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
			//content match; print out the received message
			
			//create the reply (transform)
			Map<String, Object> reply = msg;
			reply.put(SFipa.RECEIVERS, msg.get(SFipa.REPLY_TO)!=null ?  msg.get(SFipa.REPLY_TO) : msg.get(SFipa.SENDER));
			reply.put(SFipa.IN_REPLY_TO, msg.get(SFipa.REPLY_WITH));
			reply.remove(SFipa.REPLY_WITH);
			reply.remove(SFipa.REPLY_TO);
			//define reply content
			reply.put(SFipa.CONTENT, "System initialised, Please scan system");
			reply.put(SFipa.PERFORMATIVE, SFipa.INFORM);
			reply.put(SFipa.SENDER, agent.getId());
			agent.getFeature(IMessageFeature.class).sendMessage(reply, (IComponentIdentifier)reply.get(SFipa.RECEIVERS)).get();
		}
		else
		{
			agent.getLogger().severe("Could not process message: "+msg);
		}
	}
}