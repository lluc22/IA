package Drivers;

import Domini.*;
import IA.DistFS.Requests;
import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import com.sun.imageio.plugins.common.I18N;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by asus on 10/4/2016.
 */

public class DriverExp5 {
    public static void main(String[] args) throws Exception {
        int[] seed = {100, 6848, 5973, 180, 1855, 1127, 97, 6378,
                2077, 7085, 5289, 773, 8827, 2100, 3007, 4285, 2460,
                6624, 190, 5696, 3338, 4623, 1266, 7134, 5959, 3871, 1524, 4842, 6150, 4817};
        int n = 30;
        double[] tTrans = new double[n];
        long[] tExec = new long[n];


        Scanner sc = new Scanner(System.in);
        System.out.println("Heuristic SD: 1, Heuristic max: 2");
        int op = sc.nextInt();
        PrintStream out = new PrintStream(new FileOutputStream("outputExp5.txt"));
        System.setOut(out);
        System.out.println("Número rep." + "\t" + "Temps execució" + "\t" + "Temps Transmissió" + "\t" + "Desviació");
        int mE = 0;
        int mT = 0;

        switch (op) {
            case 1:
                for (int i = 0; i < n; ++i) {
                    long startTime = System.currentTimeMillis();
                    Object[] object = creaEstructures(50, 5, 200, 5, seed[i]);
                    Servers ser = (Servers) object[0];
                    Requests req = (Requests) object[1];
                    Estat initialState = initialState = new Estat(req, ser, 3, 50);
                    Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic());
                    Search hillClimbingSearch = new HillClimbingSearch();
                    SearchAgent searchAgent = null;
                    searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                    long estimatedTime = System.currentTimeMillis() - startTime;
                    Estat estat = (Estat) hillClimbingSearch.getGoalState();
                    mE += estimatedTime;
                    mT += Heuristic.getSum(estat);
                    System.out.println( i + "\t" + estimatedTime +"\t" + Heuristic.getSum(estat) + "\t" + Heuristic.getSD(estat));
                }
               // System.out.println("Temps execució mitjana: " + mE/n);
                //System.out.println("Temps total de transmissió: " + mT/n);
                

                break;
            case 2:
                for (int i = 0; i < n; ++i) {
                    long startTime = System.currentTimeMillis();
                    Object[] object = creaEstructures(50, 5, 200, 5, seed[i]);
                    Servers ser = (Servers) object[0];
                    Requests req = (Requests) object[1];
                    Estat initialState = initialState = new Estat(req, ser, 3, 50);
                    Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic2());
                    Search hillClimbingSearch = new HillClimbingSearch();
                    SearchAgent searchAgent = null;
                    searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                    long estimatedTime = System.currentTimeMillis() - startTime;
                    Estat estat = (Estat) hillClimbingSearch.getGoalState();
                    tExec[i] = estimatedTime;
                    tTrans[i] = Heuristic2.getSum(estat);
                }
                out = new PrintStream(new FileOutputStream("outputExp5.txt"));
                System.setOut(out);
                mE = 0;
                mT = 0;
                for (int i = 0; i < n; ++i) {
                    mE += tExec[i];
                    mT += tTrans[i];
                }
                mE = mE / n;
                mT = mT / n;
                System.out.println("Temps execució mitjana: " + mE);
                System.out.println("Temps total de transmissió: " + mT);

                System.out.println("Número rep." + "\t" + "Temps execució" + "\t" + "Temps Transmissió");
                for(int i = 0; i < n; ++i) {
                    System.out.println( i + "\t" + tExec[i] +"\t" + tTrans[i]);
                }
                break;
        }
    }

    private static Object[] creaEstructures(int nserv, int nrep, int users, int request, int seed) {
        Object[] obj = new Object[2];
        Servers servers = null;
        Requests requests = new Requests(users, request, seed);
        try {
            servers = new Servers(nserv, nrep, seed);

        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }
        obj[0] = servers;
        obj[1] = requests;
        return obj;
    }
}
