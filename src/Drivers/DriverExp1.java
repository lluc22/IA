package Drivers;


import Domini.*;
import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DriverExp1 {
    public static void main(String[] args) {
        int nre_experiments = 100;
        int nserv = 50;
        int nrep = 5;
        int users = 200;
        int request = 5;
        int [] seed = {90, 58, 13, 23, 21, 34,  6, 45, 55, 16, 40, 73, 74, 75, 31, 68, 12, 47,  2,100,
                7, 94, 83, 48, 98, 63, 49, 62, 69, 86, 17, 70, 84, 43, 33, 95, 91, 96, 18, 53, 25, 92,
                20, 27, 82, 42, 54, 76, 41, 22, 35, 39, 77, 79, 59,  4,  8, 66, 36,  5,  3, 38, 80, 93,
                67, 10, 46, 52, 61, 89, 51,  9, 88, 65, 26, 32, 97, 72, 24, 78, 60, 57, 64, 71, 81, 11,
                56, 29, 19, 50,  1, 87, 85, 99, 30, 28, 15, 44, 37, 14};
        int initial = 3;

        /*PrintStream out2 = null;
        try {
            out2 = new PrintStream(new FileOutputStream("exp1.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out2);*/
        System.out.println("Heuristic" + "\t" + "Max Time"+ "\t" +"SD"+ "\t" +"Time");
        for(int i = 0; i < nre_experiments; ++i){


            Servers servers = null;
            Requests requests = new Requests(users,request,seed[i]);
            try {
                servers = new Servers(nserv,nrep,seed[i]);

            } catch (Servers.WrongParametersException e) {
                e.printStackTrace();
            }
            long startTime = System.currentTimeMillis();
            Estat initialState = initialState = new Estat(requests,servers,initial,nserv);
            Problem hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic2());
            Problem simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic2());
            Search hillClimbingSearch = new HillClimbingSearch();
            Search simulatedAnnealingSearch = new SimulatedAnnealingSearch(2000,100,5,0.001);
            SearchAgent searchAgent = null;
            switch (0) {
                case 0:
                    try {
                        searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                        Estat estat = (Estat) hillClimbingSearch.getGoalState();
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        System.out.println(Heuristic2.getMax(estat)+ "\t" + Heuristic.getMax(estat)+ "\t" +Heuristic.getSD(estat)+ "\t" +estimatedTime);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    break;
                case 1:
                    try {
                        searchAgent = new SearchAgent(simulatedAnnealing, simulatedAnnealingSearch);
                        Estat estat = (Estat) simulatedAnnealingSearch.getGoalState();
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        System.out.println(Heuristic2.getMax(estat)+ "\t" + Heuristic.getMax(estat)+ "\t" +Heuristic.getSD(estat)+ "\t" +estimatedTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }



}
