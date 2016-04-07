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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by xester13 on 07/04/16.
 */

public class DriverExp4 {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Usuaris: 1, Servidors: 2");

        int op = sc.nextInt();
        switch (op){
            case 1:
                System.out.print("Nombre d'iteracions: ");
                int n = sc.nextInt();
                System.out.println("Nombre de repeticions de l'experiment: ");
                int rep = sc.nextInt();
                int[] tempsRep = new int[n];
                for(int i = 0; i < n; ++i) tempsRep[i] = 0;

                for(int i = 0; i < rep; ++i) {
                    int users = 100;
                    for (int j = 0; j < n; ++j) {
                        long startTime = System.currentTimeMillis();
                        Object[] obj = new Object[2];
                        obj = creaEstructures(50, 5, users, 5, 1234);
                        Servers ser = (Servers) obj[0];
                        Requests req = (Requests) obj[1];
                        Estat initialState = initialState = new Estat(req, ser, 3, 50);
                        Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic());
                        Search hillClimbingSearch = new HillClimbingSearch();
                        SearchAgent searchAgent = null;
                        searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        tempsRep[j] += estimatedTime;
                        users += 100;
                    }
                }
                for(int i = 0; i < tempsRep.length; ++i) System.out.println("Temps mitjà(" + (100 + i*100) + "): " + tempsRep[i]/rep);
                break;
            case 2:
                System.out.print("Nombre d'iteracions: ");
                int n2 = sc.nextInt();
                System.out.println("Nombre de repeticions de l'experiment: ");
                int rep2 = sc.nextInt();
                int[] tempsRep2 = new int[n2];
                for(int i = 0; i < n2; ++i) tempsRep2[i] = 0;

                for(int i = 0; i < rep2; ++i) {
                    int servers = 50;
                    for (int j = 0; j < n2; ++j) {
                        long startTime = System.currentTimeMillis();
                        Object[] obj = new Object[2];
                        obj = creaEstructures(servers, 5, 100, 5, 1234);
                        Servers ser = (Servers) obj[0];
                        Requests req = (Requests) obj[1];
                        Estat initialState = initialState = new Estat(req, ser, 3, servers);
                        Problem hillClimbing = new Problem(initialState, new GeneradoraSuccesors(), new EstatFinal(), new Heuristic());
                        Search hillClimbingSearch = new HillClimbingSearch();
                        SearchAgent searchAgent = null;
                        searchAgent = new SearchAgent(hillClimbing, hillClimbingSearch);
                        long estimatedTime = System.currentTimeMillis() - startTime;
                        tempsRep2[j] += estimatedTime;
                        servers += 50;
                    }
                }
                for(int i = 0; i < tempsRep2.length; ++i) System.out.println("Temps mitjà(" + (100 + i*50) + "): " + tempsRep2[i]/rep2);
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
}
