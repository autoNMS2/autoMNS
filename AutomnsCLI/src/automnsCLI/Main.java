package automnsCLI;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Enumeration;

public class Main {

	public static void main(String[] args) throws IOException {
		Menus.MainMenu();
	}
}
