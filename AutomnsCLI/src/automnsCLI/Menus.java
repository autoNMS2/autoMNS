package automnsCLI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Menus {
	
	public static void MainMenu() throws IOException {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to AutoMNS\n"
				+ "Please select an option:\n"
				+ "1. Initialise VMs \n"
				+ "2. Initialise Agents \n"
				+ "3. User Options \n" 
				+ "4. Open Application \n"
				+ "0. Exit Application");
		int i = input.nextInt();
		
		switch(i) {
		case 1:
			VMFunctions.addVMs();
			break;
		case 2:
			VMFunctions.initialiseAgents();
			break;
		case 3:
			UserOptions();
			break;
		case 4:
			VMFunctions.launchApplication();
			break;
		case 0:
			System.exit(0);
		}
	}
	
	
	public static void UserOptions() throws IOException {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to AutoMNS\n"
				+ "Please select an option:\n"
				+ "1. Kill Service \n"
				+ "2. Kill Node \n"
				+ "3. Nuke Swarm \n"
				+ "4. List Services \n"
				+ "5. List Nodes \n"
				+ "6. Get Service Logs \n"
				+ "7. Inspect Service \n"
				+ "0. Back");
		int i = input.nextInt();
		
		switch(i) {
		case 1:
			System.out.println("Pick a Service to Kill: ");
			System.out.println("Killing Service...");
			break;
		case 2:
			System.out.println("Killing Node...");
			break;
		case 3:
			System.out.println("Swarm Nuked");
			break;
		case 4:
			System.out.println("Services: ");
			break;
		case 5:
			System.out.println("Nodes: ");
			break;
		case 6:
			System.out.println("Service Logs: ");
			break;
		case 7:
			System.out.println("Pick a Service to Inspect: ");
			break;
		case 0:
			MainMenu();
		}
	}
	

}
