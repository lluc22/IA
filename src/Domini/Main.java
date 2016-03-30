package Domini;

import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int problem = sc.nextInt();
		int nserv = sc.nextInt();
		int nrep = sc.nextInt();
		int users = sc.nextInt();
		int request = sc.nextInt();
		int seed = sc.nextInt();
		int initial = sc.nextInt();
		Servers servers = null;
		Requests requests = new Requests(users,request,seed);
		try {
			servers = new Servers(nserv,nrep,seed);

		} catch (WrongParametersException e) {
			e.printStackTrace();
		}
		Estat initialState = initialState = new Estat(requests,servers,initial);
		Problem hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic());
		Problem simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic());
		Search hillClimbingSearch = new HillClimbingSearch();
		Search simulatedAnnealingSearch = new SimulatedAnnealingSearch();
		SearchAgent searchAgent = null;
		switch (problem){
			case 0:
				try {
					searchAgent = new SearchAgent(hillClimbing,hillClimbingSearch);
					printActions(searchAgent.getActions());
					printInstrumentation(searchAgent.getInstrumentation());
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case 1:
				try {
					searchAgent = new SearchAgent(simulatedAnnealing,simulatedAnnealingSearch);
					printActions(searchAgent.getActions());
					printInstrumentation(searchAgent.getInstrumentation());
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				//Error
		}



	}

	private static void printInstrumentation(Properties properties) {
		Iterator keys = properties.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String property = properties.getProperty(key);
			System.out.println(key + " : " + property);
		}

	}

	private static void printActions(List actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = (String) actions.get(i);
			System.out.println(action);
		}
	}

}
