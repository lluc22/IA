package Domini;


import aima.search.framework.GoalTest;

/**
 * Classe utilitzada per a determinar si un estat final.
 */
public class EstatFinal implements GoalTest{
    @Override
    /**
     * Sempre retorna fals.
     */
    public boolean isGoalState(Object o) {
        return false;
    }
}
