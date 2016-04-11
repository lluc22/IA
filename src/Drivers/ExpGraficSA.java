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

public class ExpGraficSA {
    public static void main(String[] args) {
        int nserv = 50;
        int nrep = 5;
        int users = 200;
        int request = 5;
        int seed = 100;
        int initial = 3;
        int steps = 1000000;
        int stiter = 1000;
        int k = 5;
        double lamb = 0.0001;
        Requests requests = new Requests(users,request,seed);
        Servers servers = null;
        try {
            servers = new Servers(nserv,nrep,seed);
        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }

        Estat initialState = initialState = new Estat(requests,servers,initial,nserv);
        System.out.println(Heuristic.getHeuristic(initialState));
        Problem simulatedAnnealing = new Problem(initialState,new GeneradoraSuccesorsSA(),new EstatFinal(),new Heuristic());
        Problem hillClimbing = new Problem(initialState,new GeneradoraSuccesors(),new EstatFinal(),new Heuristic());
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
        System.out.println("Hill Climbing: " + Heuristic.getHeuristic(HCState));
        System.out.println("Simulated Anealling: " + Heuristic.getHeuristic(SAState));
        //499878.58838290063
        int step = 0;
        XYSeries series = new XYSeries("");
        series.add(step,Heuristic.getHeuristic(initialState));
        double anterior = Heuristic.getHeuristic(initialState);
        ++step;
        for(Object o : simulatedAnnealingSearch.getPathStates()){
            Estat estat = (Estat) o;
            series.add(step,Heuristic.getHeuristic(estat));
            if(anterior < Heuristic.getHeuristic(estat)) System.out.println("true!: " + anterior + " " + Heuristic.getHeuristic(estat));
            anterior = Heuristic.getHeuristic(estat);
            ++step;
        }
        JFrame graph = new ChartSA(series,"SA " + steps + " iterations " + "k = "+ k + " lambda = " + lamb);
        graph.setVisible(true);

    }

}



