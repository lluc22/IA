package Domini;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradoraSuccesorsSA  implements SuccessorFunction{
    @Override
    public List getSuccessors(Object o) {
        Heuristic heuristic = new Heuristic();
        Estat actual = (Estat) o;
        Random random = new Random();
        List retVal = new ArrayList<>();
        int ranOp = random.nextInt(2);
        boolean aplicat = false;
        while (!aplicat) {
            if (ranOp == 0) {
                int pet = random.nextInt(actual.mPeticions.length);
                int ser = random.nextInt(actual.mServers.size());
                if (actual.potAssignar(pet, ser)) {
                    Estat succesor1 = new Estat(actual.mRequests, actual.mServers, actual.mPeticions, actual.mTempsServidors);
                    succesor1.assigna(pet, ser);
                    double heur1 = heuristic.getHeuristicValue(succesor1);
                    String msg1 = "Assign server " + ser + " to petition " + pet + " with cost " + heur1;
                    retVal.add(new Successor(msg1, succesor1));
                }
            } else {

            }
        }
        return retVal;
    }
}
