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
        int  seed = 1234;
        int initial = 3;
        int steps = 1000000;
        int stiter = 1000;
        int k = 5;
        double lamb = 0.1;
        Requests requests = new Requests(users,request,seed);
        Servers servers = null;
        try {
            servers = new Servers(nserv,nrep,seed);
        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }

        Estat initialState = initialState = new Estat(requests,servers,initial,nserv);
        System.out.println(Heuristic.getMax(initialState));
        Problem simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic2());
        Problem hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic2());
        Search hillClimbingSearch = new HillClimbingSearch();
        Search simulatedAnnealingSearch = new SimulatedAnnealingSearch(steps,stiter,k,lamb);
        SearchAgent searchAgentHC = null;
        SearchAgent searchAgent = null;
        try {
            searchAgent = new SearchAgent(simulatedAnnealing, simulatedAnnealingSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            searchAgentHC = new SearchAgent(hillClimbing, hillClimbingSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Estat HCState = (Estat) hillClimbingSearch.getGoalState();
        Estat SAState = (Estat) simulatedAnnealingSearch.getGoalState();
        System.out.println("Hill Climbing: " + Heuristic.getMax(HCState));
        System.out.println("Simulated Anealling: " + Heuristic.getMax(SAState));

        int step = 0;
        XYSeries series = new XYSeries("");
        ++step;
        series.add(step,Heuristic2.getMax(initialState));
        for(Object o : simulatedAnnealingSearch.getPathStates()){
            Estat estat = (Estat) o;
            series.add(step,Heuristic2.getMax(estat));
            ++step;
        }
        JFrame graph = new ChartSA(series,"SA " + steps + " iterations " + "k = "+ k + " lambda = " + lamb);
        graph.setVisible(true);

    }

}



