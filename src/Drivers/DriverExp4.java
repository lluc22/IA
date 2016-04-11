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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by xester13 on 07/04/16.
 */

public class DriverExp4 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Usuaris: 1, Servidors: 2");
        Random random = new Random();
        int op = sc.nextInt();
        switch (op){
            case 1:
                System.out.print("Nombre d'iteracions: ");
                int n = sc.nextInt();
                System.out.println("Nombre de repeticions de l'experiment: ");
                int rep = sc.nextInt();
                int[] tempsRep = new int[n];
                for(int i = 0; i < n; ++i) tempsRep[i] = 0;
                ArrayList<long[]> desviacions = new ArrayList<long[]>();
                for(int i = 0; i < n; ++i){
                    long[] d = new long[rep];
                    for(int j = 0; j < rep; ++j) d[j] = 0;
                    desviacions.add(d);
                }
                for(int i = 0; i < rep; ++i) {
                    int users = 100;
                    for (int j = 0; j < n; ++j) {
                        long startTime = System.currentTimeMillis();
                        Object[] obj = new Object[2];
                        obj = creaEstructures(50, 5, users, 5, random.nextInt());
                        Servers ser = (Servers) obj[0];
                        Requests req = (Requests) obj[1];
                        Estat initialState = initialState = new Estat(req, ser, 3, 50);
                        Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic2());
                        Search hillClimbingSearch = new HillClimbingSearch();
                        SearchAgent searchAgent = null;
                        searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        long[] desv = desviacions.get(j);
                        desv[i] = estimatedTime;
                        desviacions.add(desv);
                        users += 100;
                    }
                }
                PrintStream out = new PrintStream(new FileOutputStream("outputUsers.txt"));
                System.setOut(out);
                System.out.println("Usuaris" + "\t" + "Temps");
                for(int i = 0; i < tempsRep.length; ++i){
                    long[] d = desviacions.get(i);
                    System.out.println((100 + i*100) +"\t" + getSD(d));
                }
                break;
            case 2:
                System.out.print("Nombre d'iteracions: ");
                int n2 = sc.nextInt();
                System.out.println("Nombre de repeticions de l'experiment: ");
                int rep2 = sc.nextInt();
                int[] tempsRep2 = new int[n2];
                for(int i = 0; i < n2; ++i) tempsRep2[i] = 0;
                ArrayList<long[]> desviacions2 = new ArrayList<long[]>();
                for(int i = 0; i < n2; ++i){
                    long[] d = new long[rep2];
                    for(int j = 0; j < rep2; ++j) d[j] = 0;
                    desviacions2.add(d);
                }
                for(int i = 0; i < rep2; ++i) {
                    int servers = 50;
                    for (int j = 0; j < n2; ++j) {
                        long startTime = System.currentTimeMillis();
                        Object[] obj = new Object[2];
                        obj = creaEstructures(servers, 5, 100, 5, random.nextInt());
                        Servers ser = (Servers) obj[0];
                        Requests req = (Requests) obj[1];
                        Estat initialState = initialState = new Estat(req, ser, 3, servers);
                        Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic2());
                        Search hillClimbingSearch = new HillClimbingSearch();
                        SearchAgent searchAgent = null;
                        searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        long[] desv2 = desviacions2.get(j);
                        desv2[i] = estimatedTime;
                        desviacions2.add(desv2);
                        servers += 50;
                    }
                }
                PrintStream out2 = new PrintStream(new FileOutputStream("outputServ.txt"));
                System.setOut(out2);
                System.out.println("Servidors" + "\t" + "Temps");
                for(int i = 0; i < tempsRep2.length; ++i) {
                    long[] desv2 = desviacions2.get(i);

                    System.out.println((50 + i * 50) + "\t" + getSD(desv2));

                }
                break;
            case -1: System.exit(0);
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
    private static int[] createSeed(int n){
        int[] seeds = new int[n];
        Random random = new Random();
        for(int i = 0; i < n; ++i){
            seeds[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return seeds;
    }
    private  static double getSD(long[] d){
        double mean = 0.0;
        double M2 = 0.0;
        double sd;
        double m = 0;
        for(int i = 0; i < d.length ; ++i) {
            m+=1;
            double t = d[i];
            double delta = t - mean;
            mean += delta / m;
            M2 += delta * (t - mean);
        }
        if (m < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / (m - 1));
        return sd;
    }
}
