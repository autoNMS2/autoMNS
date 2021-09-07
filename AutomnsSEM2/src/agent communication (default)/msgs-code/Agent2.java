package msgs;

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
		if("Hello am Agent1".equals(msg.get(SFipa.CONTENT)))
		{
			//content match; print out the received message
			System.out.println("New message to: "+agent.getId()+"\n Message  is: "+msg);
			//create the reply (transform)
			Map<String, Object> reply = msg;
			reply.put(SFipa.RECEIVERS, msg.get(SFipa.REPLY_TO)!=null ?  msg.get(SFipa.REPLY_TO) : msg.get(SFipa.SENDER));
			reply.put(SFipa.IN_REPLY_TO, msg.get(SFipa.REPLY_WITH));
			reply.remove(SFipa.REPLY_WITH);
			reply.remove(SFipa.REPLY_TO);
			//define reply content
			reply.put(SFipa.CONTENT, "Hello back, am Agent2");
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