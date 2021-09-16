package automnsCLI;

import java.io.IOException;
import java.util.Scanner;

public class Menus {
	
	public static void MainMenu() throws IOException {
		clearScreen();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to AutoMNS\n"
				+ "Please select an option:\n"
				+ "1. VM Options \n"
				+ "2. Docker Options \n"
				+ "3. User Options \n" 
				+ "4. Open Application \n"
				+ "0. Exit Application");
		int i = input.nextInt();
		
		switch(i) {
		case 1:
			VMOptions();
			break;
		case 2:
			DockerOptions();
			break;
		case 3:
			UserOptions();
			break;
		case 4:
			LaunchApp();
			break;
		case 0:
			System.exit(0);
		}
	}
	
	public static void VMOptions() throws IOException {
		clearScreen();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to AutoMNS\n"
				+ "Please select an option:\n"
				+ "1. Add VMs \n"
				+ "2. Initialise VMs \n"
				+ "3. List VMs \n" 
				+ "0. Back");
		int i = input.nextInt();
		
		switch(i) {
		case 1:
			VMFunctions.addVMs();
			break;
		case 2:
			System.out.println("Initilising, please wait...");
			break;
		case 3:
			System.out.println("VMs:");
			break;
		case 0:
			MainMenu();
		}
	}
	
	public static void DockerOptions() throws IOException {
		clearScreen();
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome to AutoMNS\n"
				+ "Please select an option:\n"
				+ "1. Initialise Swarm \n"
				+ "2. Add workers to Swarm \n"
				+ "3. Promote worker to Manager \n" 
				+ "4. Demote Manager to Worker \n"
				+ "5. Remove Node \n"
				+ "6. Inspect Node \n"
				+ "7. View Node Tasks \n"
				+ "8. Nuke Swarm \n"
				+ "0. Back");
		int i = input.nextInt();
		
		switch(i) {
		case 1:
			System.out.println("Adding VMs...");
			break;
		case 2:
			System.out.println("Initilising, please wait...");
			break;
		case 3:
			System.out.println("VMs:");
			break;
		case 0:
			MainMenu();
		}
	}
	
	public static void UserOptions() throws IOException {
		clearScreen();
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
	
	public static void LaunchApp() {
		clearScreen();
		System.out.println("Launching App");
	}
	
	public static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}
}
