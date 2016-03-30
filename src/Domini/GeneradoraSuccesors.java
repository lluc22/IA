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
        for(int i = 0; i < actual.mRequests.size(); ++i){
            for(int j = 0; j < actual.mServers.size(); ++j){
                Heuristic heuristic = new Heuristic();
                Estat succesor1 = new Estat(actual.mRequests,actual.mServers,actual.mPeticions,actual.mTempsServidors);
                Estat succesor2 = new Estat(actual.mRequests,actual.mServers,actual.mPeticions,actual.mTempsServidors);
                double heur1 = heuristic.getHeuristicValue(succesor1);
                double heur2 = heuristic.getHeuristicValue(succesor2);
                succesor1.assigna(i,j);
                succesor2.intercanvia(i,j);
                String msg1 = "Assign server " + i + " to petition " + j + " with cost " + heur1;
                String msg2 = "Swap server of petition " + i + " with server of petition " + j + " with cost " + heur2;
                retVal.add(new Successor(msg1,succesor1));
                retVal.add(new Successor(msg2,succesor2));
            }
        }


        return retVal;
    }
}
