package msgs;

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

@Arguments({
	@Argument(name="receiver", clazz=IComponentIdentifier.class, description="The component receiver of Agent2."),
	@Argument(name="missed_max", clazz=int.class, description="Maximum number of allowed missed replies", defaultvalue="3"),
	@Argument(name="timeout", clazz=long.class, description="Timeout for reply", defaultvalue="1000"),
})

@Agent
public class Agent1 
{	
	@Agent
	protected IInternalAccess agent;
	
	/** The receiver. */
	protected IComponentIdentifier receiver;
	
	/** The difference between sent messages and received replies. */
	protected int dif;
	
	/** Hashset with conversation ids of sent messages. */
	protected Set<String> sent;
	
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
	
	//Called when a message arrives.
	@OnMessage
	public void messageArrived(Map<String, Object> msg)
	{
		System.out.println("New message to: "+agent.getId()+"\n Message details is: "+msg);
	}
}