package Drivers;

import Domini.*;
import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


public class DriverExp7 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre de repeticions de l'experiment");
        int n = sc.nextInt();
        int[] seeds = new int[]{51827, 65994, 125207, 420590, 479666, 557253, 825573, 924403, 964453, 987988};
        int[] tTrans = new int[5];
        int[] tExec = new int[5];
        for(int i = 0; i < 5; ++i){
            tExec[i] = 0;
            tTrans[i] = 0;
        }
        for(int i = 0; i < n; ++i){
            for(int nrep = 5; nrep <= 25; nrep +=5) {
                long startTime = System.currentTimeMillis();
                Object[] object = creaEstructures(50,nrep,200,5,seeds[i]);
                Servers ser = (Servers) object[0];
                Requests req = (Requests) object[1];
                Estat initialState = initialState = new Estat(req, ser, 3, 50);
                Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic());
                Search hillClimbingSearch = new HillClimbingSearch();
                SearchAgent searchAgent = null;
                searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                long estimatedTime = System.currentTimeMillis() - startTime;
                tExec[nrep/5 - 1] += estimatedTime;
                Estat estat = (Estat) hillClimbingSearch.getGoalState();
                tTrans[nrep/5 -1] += Heuristic.getSum(estat);
            }
        }
        PrintStream out = new PrintStream(new FileOutputStream("outputExp7Exech1.txt"));
        System.setOut(out);
        System.out.println("Replicacions" + "\t" + "Temps exec");
        for(int i = 0; i < 5; ++i) {
            System.out.println( (5 + 5*i) + "\t" + tExec[i]/n);
        }
        PrintStream out2 = new PrintStream(new FileOutputStream("outputExp7Transh1.txt"));
        System.setOut(out2);
        System.out.println("Replicacions" + "\t" + "Temps transmissió");
        for(int i = 0; i < 5; ++i) {
            System.out.println( (5 + 5*i) + "\t" + tTrans[i]/n);
        }
    }
    private static Object[] creaEstructures(int nserv, int nrep, int users, int request,int seed){
        Object[] obj = new Object[2];
        Servers servers = null;
        Requests requests = new Requests(users,request,seed);
        try {
            servers = new Servers(nserv,nrep,seed);

        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }
        obj[0] = servers;
        obj[1] = requests;
        return obj;
    }
}
