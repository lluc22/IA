package Drivers;
import Domini.*;
import IA.DistFS.*;

import java.util.Scanner;

/**
 * Created by xest13 on 21/03/16.
 */
public class DriverEstat {
    private static void provarSolIni(int idSol){
        Requests req = new Requests(10,5,1234);
        Servers ser = null;
        try {
            ser = new Servers(10,2,1234);
            Estat estat = new Estat(req,ser,idSol);
            System.out.println("Peticions:");
            for(int i = 0; i < estat.mPeticions.length; ++i){
                System.out.println("Nº pet: " + i + " Servidor: " + estat.mPeticions[i]);
            }
            System.out.println("Temps totals dels servidors:");
            for(int i = 0; i < estat.mTempsServidors.length; ++i){
                System.out.println("Servidor: " + i + " Temps total: " + estat.mTempsServidors[i]);
            }
        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }
    }
    private static void provarOp(int idOp){

    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("1: provar solucions inicials, 2: provar operadors.");
        int id = in.nextInt();
        while (true){
            switch (id){
                case 1: System.out.println("Introdueix l'identificador de solucio inicial:");
                    int idSol = in.nextInt();
                    provarSolIni(idSol);
                    break;
                case 2: System.out.println("Introdueix l'identificador de l'operador");
                    int idOp = in.nextInt();
                    provarOp(idOp);
                    break;
                case -1: System.exit(0);
            }
            System.out.println("1: provar solucions inicials, 2: provar operadors.");
            id = in.nextInt();
        }
    }
}
