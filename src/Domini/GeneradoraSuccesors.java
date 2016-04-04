package Domini;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class GeneradoraSuccesors implements SuccessorFunction {
    @Override
    public List getSuccessors(Object o) {
        List retVal = new ArrayList();
        Estat actual = (Estat) o;
        Heuristic2 heuristic = new Heuristic2();
        for(int i = 0; i < actual.mRequests.size(); ++i){
            for(int j = 0; j < actual.mServers.size(); ++j){

                if(actual.potAssignar(i,j)) {
                    Estat succesor1 = new Estat(actual.mRequests, actual.mServers, actual.mPeticions, actual.mTempsServidors);

                    double heur1 = heuristic.getHeuristicValue(succesor1);

                    succesor1.assigna(i, j);

                    String msg1 = "Assign server " + j + " to petition " + i + " with cost " + heur1;

                    retVal.add(new Successor(msg1, succesor1));

                }
            }
        }
/*
        for(int i = 0; i < actual.mRequests.size(); ++i){
            for(int j = i + 1; j < actual.mRequests.size(); ++j){
                if(actual.potAssignar(i,actual.mPeticions[j]) && actual.potAssignar(j,actual.mPeticions[i])) {
                    Estat succesor2 = new Estat(actual.mRequests, actual.mServers, actual.mPeticions, actual.mTempsServidors);
                    double heur2 = heuristic.getHeuristicValue(succesor2);
                    succesor2.intercanvia(i, j);
                    String msg2 = "Swap server of petition " + i + " with server of petition " + j + " with cost " + heur2;
                    retVal.add(new Successor(msg2, succesor2));
                }
            }
        }*/


        return retVal;
    }
}
