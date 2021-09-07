package msgs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jadex.bridge.ComponentIdentifier;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IInternalAccess;
//import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.fipa.SFipa;
import jadex.bridge.service.annotation.OnStart;
//import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
//import jadex.micro.annotation.Argument;
//import jadex.micro.annotation.Arguments;
import jadex.micro.annotation.OnMessage;
//import jadex.commons.SUtil;


@Agent   //component factory 
public class Agent1 
{	
	@Agent    //defines an agent 
	protected IInternalAccess agent;
	//creates a receiver component  
	protected IComponentIdentifier receiver;
	//Hashset with conversation ids of sent messages.
	protected Set<String> sent;
	
	@OnStart
	public void executeBody()
	{
		sent = new HashSet<String>();
		//creates a new component process
		final IComponentStep<Void> step = new IComponentStep<Void>()
		{
			public IFuture<Void> execute(IInternalAccess ia)
			{
				//String convid = SUtil.createUniqueId(agent.getId().getName());
				Map<String, Object> msg = new HashMap<String, Object>();
				msg.put(SFipa.CONTENT, "Hello am Agent1");
				// receiver uses this to reply 
				msg.put(SFipa.SENDER, agent.getId());
				//sent.add(convid);
				agent.getFeature(IMessageFeature.class).sendMessage(msg, receiver).get();
				return IFuture.DONE;
			}
		};
		//identifies the receiver
		receiver = new ComponentIdentifier("Agent2", agent.getId().getParent());
		//uses a built in process to execute 
		agent.getFeature(IExecutionFeature.class).scheduleStep(step);
	}
	//Called when a message arrives.
	@OnMessage
	public void messageArrived(Map<String, Object> msg)
	{
		System.out.println("New message to: "+agent.getId()+"\n Message details is: "+msg);
	}
}
