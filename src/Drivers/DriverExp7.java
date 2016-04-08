package Drivers;

import Domini.Estat;
import Domini.EstatFinal;
import Domini.GeneradoraSuccesors;
import Domini.Heuristic;
import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by xester13 on 07/04/16.
 */
public class DriverExp7 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre de repeticions de l'experiment");
        int n = sc.nextInt();
        int[] tTrans = new int[5];
        int[] tExec = new int[5];
        for(int i = 0; i < 5; ++i){
            tExec[i] = 0;
            tTrans[i] = 0;
        }
        for(int i = 0; i < n; ++i){
            for(int nrep = 5; nrep <= 25; nrep +=5) {
                long startTime = System.currentTimeMillis();
                Object[] object = creaEstructures(50,nrep,200,5,1234);
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
        PrintStream out = new PrintStream(new FileOutputStream("outputExp7.txt"));
        System.setOut(out);
        System.out.printf("Replicacions" + "\t" + "Temps Trans" + "\t" + "Temps exec");
        for(int i = 0; i < 5; ++i) {
            System.out.println( (5 + 5*i) + "\t"+ tTrans[i]/n +"\t" + tExec[i]/n);
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
