package Drivers;


import Domini.*;
import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;

public class ExpSA {
    public static void main(String[] args) {
        int nserv = 50;
        int nrep = 5;
        int users = 200;
        int request = 5;
        int seed[] = {6125, 1629, 8096, 3002, 9004, 6063, 6777, 6479, 9819, 1353};
        int initial = 3;
        int steps = 500000;
        int stiter = 1000;
        int[] k = {1, 5, 25, 125, 150};
        double[] lamb = {0.01,0.001,0.0001,0.00001,0.000001};
        System.out.println("k" + "\t" + "lambda" + "\t" + "cost" + "\t" + "time"+ "\t" +"HC" );
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                for (int it = 0; it < 1; ++it) {
                    Requests requests = new Requests(users, request, seed[it]);
                    Servers servers = null;
                    try {
                        servers = new Servers(nserv, nrep, seed[it]);
                    } catch (Servers.WrongParametersException e) {
                        e.printStackTrace();
                    }

                    Estat initialState = initialState = new Estat(requests, servers, initial, nserv);

                    Problem simulatedAnnealing = new Problem(initialState, new GeneradoraSuccesorsSA(), new EstatFinal(), new Heuristic2());
                    Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic2());
                    Search hillClimbingSearch = new HillClimbingSearch();
                    Search simulatedAnnealingSearch = new SimulatedAnnealingSearch(steps, stiter, k[i], lamb[j]);
                    SearchAgent searchAgentHC = null;
                    SearchAgent searchAgent = null;
                    double mean_time = 0.0;
                    double mean_cost = 0.0;
                    for(int iteracions = 0; iteracions < 1; ++iteracions) {
                        long start_time = System.currentTimeMillis();
                        try {
                            searchAgent = new SearchAgent(simulatedAnnealing, simulatedAnnealingSearch);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Estat SAState = (Estat) simulatedAnnealingSearch.getGoalState();
                        long time = System.currentTimeMillis() - start_time;
                        mean_cost += Heuristic2.getMax(SAState);
                        mean_time += time;
                    }
                    mean_cost = mean_cost /1;
                    mean_time = mean_time / 1;


                    try {
                        searchAgentHC = new SearchAgent(hillClimbing, hillClimbingSearch);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Estat HCState = (Estat) hillClimbingSearch.getGoalState();
                    double HCCost = Heuristic2.getMax(HCState);
                    System.out.println(k[i] + "\t" + lamb[j] + "\t" + mean_cost + "\t" + mean_time + "\t" + HCCost );




                }

            }
        }
    }

}



