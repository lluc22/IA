package Domini;

import IA.DistFS.*;
import IA.DistFS.Servers.WrongParametersException;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.*;

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

		long startTime = System.currentTimeMillis();

		Servers servers = null;
		Requests requests = new Requests(users,request,seed);
		try {
			servers = new Servers(nserv,nrep,seed);

		} catch (WrongParametersException e) {
			e.printStackTrace();
		}
		System.out.println("peticions "+ requests.size());



		Estat initialState = initialState = new Estat(requests,servers,initial,nserv);
		System.out.println("numero servidors: "+ initialState.mTempsServidors.length);
		System.out.println("Temps totals dels servidors:");
		for(int i = 0; i < initialState.mTempsServidors.length; ++i){
			System.out.println("Servidor: " + i + " Temps total: " + initialState.mTempsServidors[i]);
		}
		Problem hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic());
		Problem simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic());
		Search hillClimbingSearch = new HillClimbingSearch();
		Search simulatedAnnealingSearch = new SimulatedAnnealingSearch(2000,100,5,0.001);
		SearchAgent searchAgent = null;
		switch (problem){
			case 0:
				try {
					searchAgent = new SearchAgent(hillClimbing,hillClimbingSearch);
					System.out.println(searchAgent.getActions());
					printActions(searchAgent.getActions());
					printInstrumentation(searchAgent.getInstrumentation());
					Estat estat = (Estat) hillClimbingSearch.getGoalState();
					System.out.println("numero servidors: " + estat.mTempsServidors.length);
					System.out.println("Peticions:");
					for(int i = 0; i < estat.mPeticions.length; ++i){
						System.out.println("Nº pet: " + i + " Servidor: " + estat.mPeticions[i]);
					}
					System.out.println("Temps totals dels servidors:");
					for(int i = 0; i < estat.mTempsServidors.length; ++i){
						System.out.println("Servidor: " + i + " Temps total: " + estat.mTempsServidors[i]);
					}

					System.out.println("Temps total: " + Heuristic.getSum(estat));
					System.out.println("Temps màxim: " + Heuristic.getMax(estat));
					System.out.println("SD: " + Heuristic.getSD(estat));
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case 1:
				try {
					searchAgent = new SearchAgent(simulatedAnnealing,simulatedAnnealingSearch);
					System.out.println(searchAgent.getActions());
					//printActions(searchAgent.getActions());
					printInstrumentation(searchAgent.getInstrumentation());
					Estat estat = (Estat) simulatedAnnealingSearch.getGoalState();
					System.out.println("numero servidors: " + estat.mTempsServidors.length);
					System.out.println("Peticions:");
					for(int i = 0; i < estat.mPeticions.length; ++i){
						System.out.println("Nº pet: " + i + " Servidor: " + estat.mPeticions[i]);
					}
					System.out.println("Temps totals dels servidors:");
					for(int i = 0; i < estat.mTempsServidors.length; ++i){
						System.out.println("Servidor: " + i + " Temps total: " + estat.mTempsServidors[i]);
					}
					System.out.println("Temps total: " + Heuristic.getSum(estat));
					System.out.println("Temps màxim: " + Heuristic.getMax(estat));
					System.out.println("SD: " + Heuristic.getSD(estat));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				//Error
		}
		long estimatedTime = System.currentTimeMillis() - startTime;



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
