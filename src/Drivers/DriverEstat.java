package Drivers;
import Domini.*;
import IA.DistFS.*;
import jdk.nashorn.internal.ir.RuntimeNode;

import java.util.Scanner;

/**
 * Created by xest13 on 21/03/16.
 */


public class DriverEstat {


    private static void provarOp(int idOp, Estat estat, Requests req, Servers ser) {

    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("1: provar solucions inicials, 2: provar operadors.");
        int id = in.nextInt();
        Requests req;

        req = new Requests(5, 3, 50);

        try {
            Servers ser;
            ser = new Servers(10,2,1);
            Estat estat = null;
            while (true){
                switch (id){
                    case 1: System.out.println("Introdueix l'identificador de solucio inicial:");
                        int idSol = in.nextInt();
                        estat = new Estat(req,ser,idSol);
                        System.out.println("numero servidors: " + ser.size());
                        System.out.println("Peticions:");
                        for(int i = 0; i < estat.mPeticions.length; ++i){
                            System.out.println("Nº pet: " + i + " Servidor: " + estat.mPeticions[i]);
                        }
                        System.out.println("Temps totals dels servidors:");
                        for(int i = 0; i < 10; ++i){
                            System.out.println("Servidor: " + i + " Temps total: " + estat.mTempsServidors[i]);
                        }
                        break;
                    case 2: System.out.println("Introdueix l'identificador de l'operador");
                        int idOp = in.nextInt();
                        switch (idOp) {
                            case 1:
                                System.out.println("Assignar servidor x a petició i");
                                Scanner entrada = new Scanner(System.in);
                                System.out.println("introduir peticio : ");
                                int p = entrada.nextInt();
                                System.out.println("introduir servidor : ");
                                int s = entrada.nextInt();
                                if (estat.potAssignar(p, s)) {
                                    int sa = estat.mPeticions[p];
                                    estat.assigna(p, s);
                                    System.out.println("Servidor: " + s + " Temps total: " + estat.mTempsServidors[s]);
                                    System.out.println("Servidor: " + sa + " Temps total: " + estat.mTempsServidors[sa]);
                                } else System.out.println("No es pot assignar");
                                break;
                            case 2:
                                System.out.println("Intercanviar servidors de peticions:");
                                System.out.println("introduir peticio 1: ");
                                Scanner entrada1 = new Scanner(System.in);
                                int p1 = entrada1.nextInt();
                                System.out.println("introduir peticio 2: ");
                                int p2 = entrada1.nextInt();
                                int s1 = estat.mPeticions[p1];
                                int s2 = estat.mPeticions[p2];
                                if (estat.potAssignar(p1, s2) && estat.potAssignar(p2, s1)) {
                                    estat.intercanvia(p1, p2);
                                    System.out.println("Nº pet: " + p1 + " Servidor: " + estat.mTempsServidors[p1]);
                                    System.out.println("Nº pet: " + p2 + " Servidor: " + estat.mTempsServidors[p2]);
                                }
                                else System.out.println("no es pot intercanviar");
                                break;
                        }
                        break;
                    case -1: System.exit(0);
                }
                System.out.println("1: provar solucions inicials, 2: provar operadors.");
                id = in.nextInt();
            }
        } catch (Servers.WrongParametersException e) {
            e.printStackTrace();
        }


    }
}
