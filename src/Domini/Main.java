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
		int problem;
		int nserv;
		int nrep;
		int users;
		int request;
		int seed;
		int initial;
		int heuristic;
		System.out.println("Entra en aquest ordre:");
		System.out.println("1) Nombre de servidors");
		System.out.println("2) Nombre de replicacions mínim per fitxer");
		System.out.println("3) Nombre d'usuaris");
		System.out.println("4) Nombre de peticions màximes per usuari");
		System.out.println("5) Llavor per a l'aletorietat");
		System.out.println("6) Solució inicial a triar: NAIF, BALANCED i NEAR  ( 0 / 1 / 2 )");
		System.out.println("7) Heurístic a triar: criteri 1 o Criteri 2 ( 1 / 2 )");
		System.out.println("8) Hill Climbing (0) o Simullated Annealing (1)  (0 / 1)");
		nserv = sc.nextInt();
		nrep = sc.nextInt();
		users = sc.nextInt();
		request = sc.nextInt();
		seed = sc.nextInt();
		while(true) {
			initial = sc.nextInt();
			if (initial != 0 && initial != 1 && initial != 2)
				System.out.println("Error.Solució inicial a triar: NAIF, BALANCED i NEAR  ( 0 / 1 / 2 )");
			else break;
		}
		while(true) {
			heuristic = sc.nextInt();
			if (heuristic != 1 && heuristic != 2)
				System.out.println("Error.Heurístic a triar: criteri 1 o Criteri 2 ( 1 / 2 )");
			else break;
		}

		while(true) {
			problem = sc.nextInt();
			if (problem != 0 && problem != 1)
				System.out.println("Error.Hill Climbing (0) o Simullated Annealing (1)  (0 / 1)");
			else break;
		}


		long startTime = System.currentTimeMillis();

		Servers servers = null;
		Requests requests = new Requests(users,request,seed);

		while(true) {
			try {
				servers = new Servers(nserv, nrep, seed);
				break;
			} catch (WrongParametersException e) {
				System.out.println("Error! Entra els paràmetres un altre cop en aquest ordre:");
				System.out.println("1) Nombre de servidors");
				System.out.println("2) Nombre de replicacions mínim per fitxer");
				System.out.println("3) Llavor per a l'aletorietat");
				nserv = sc.nextInt();
				nrep = sc.nextInt();
				seed = sc.nextInt();

			}
		}


		Estat initialState = initialState = new Estat(requests,servers,initial - 1,nserv);


		Problem hillClimbing;
		if(heuristic == 1) hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic2());
		else hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic());
		Problem simulatedAnnealing;
		if(heuristic == 1) simulatedAnnealing= new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic2());
		else simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic());
		Search hillClimbingSearch = new HillClimbingSearch();
		SearchAgent searchAgent = null;
		double valor_heurstic;
		switch (problem){
			case 0:
				try {
					searchAgent = new SearchAgent(hillClimbing,hillClimbingSearch);
					printActions(searchAgent.getActions());
					printInstrumentation(searchAgent.getInstrumentation());
					Estat estat = (Estat) hillClimbingSearch.getGoalState();
					if(heuristic ==1) valor_heurstic = Heuristic2.getMax(estat);
					else valor_heurstic = Heuristic.getHeuristic(estat);
					System.out.println("Temps total: " + Heuristic2.getSum(estat));
					System.out.println("Temps màxim: " + Heuristic2.getMax(estat));
					System.out.println("Standard Deviation: " + Heuristic.getSD(estat));
					System.out.println("Valor Heurístic: " + valor_heurstic);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case 1:
				try {

					System.out.println("Entra en aquest ordre:");
					System.out.println("1) nombre de passos");
					System.out.println("2) iteracions per pas");
					System.out.println("3) k");
					System.out.println("4) lambda");
					int steps = sc.nextInt();
					int niter= sc.nextInt();
					int k= sc.nextInt();
					double lambda= sc.nextDouble();
					Search simulatedAnnealingSearch = new SimulatedAnnealingSearch(steps,niter,k,lambda);
					searchAgent = new SearchAgent(simulatedAnnealing,simulatedAnnealingSearch);
					printInstrumentation(searchAgent.getInstrumentation());
					Estat estat = (Estat) simulatedAnnealingSearch.getGoalState();
					if(heuristic ==1) valor_heurstic = Heuristic2.getMax(estat);
					else valor_heurstic = Heuristic.getHeuristic(estat);
					System.out.println("Temps total: " + Heuristic2.getSum(estat));
					System.out.println("Temps màxim: " + Heuristic2.getMax(estat));
					System.out.println("Standard Deviation: " + Heuristic.getSD(estat));
					System.out.println("Valor Heurístic: " + valor_heurstic);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
		}
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("Temps (ms) : " + estimatedTime);




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
