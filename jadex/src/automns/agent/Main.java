package automns.agent;

import java.util.logging.Level;

import java.util.logging.Level;
import javax.swing.SwingUtilities;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.threadpool.IThreadPoolService;
import jadex.commons.future.IFuture;

public class Main {

	public static void main(String[] args) {
		// Start from minimal configuration
		IPlatformConfiguration	conf	= PlatformConfigurationHandler.getMinimal();
		
		// Set logging level to provider better debugging output for agents.
		conf.setLoggingLevel(Level.WARNING);

		// Add BDI kernel (required when running BDI agents)
		conf.setValue("kernel_bdi", true);

		//adding the automns agent
		conf.addComponent("automns/agent/agent.class");
		
		//chat bot test agents
		//conf.addComponent("automns/agent/SystemMonitorAgent.class");

		
		// Start a Jadex platform (asynchronously in background).
		IFuture<IExternalAccess>	fut	= Starter.createPlatform(conf);
		// IFuture.get() will block until background startup is complete.
		// Without this, errors might not get shown.
		
		
		fut.get();

	}

}