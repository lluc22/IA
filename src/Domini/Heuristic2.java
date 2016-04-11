package Domini;

import aima.search.framework.HeuristicFunction;

import java.util.Arrays;

/**
 * Classe utilitzada per implementar un heurístic que minimitzi el temps més elevat d'entre tots els servidors donat un estat.
 */
public class Heuristic2 implements HeuristicFunction{
    /**
     *
     * @param o estat actual
     * @return el temps màxim d'entre tots els servidors de l'estat o.
     */
    @Override
    public double getHeuristicValue(Object o) {
        Estat state = (Estat) o;
        return getMax(o);
    }

    /**
     *
     * @param o estat actual
     * @return el temps màxim d'entre tots els servidors de l'estat o.
     */
    static public double getMax(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double max = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.mTempsServidors[i-1];
            if(t > max) max = t;
        }
        return max;
    }

}

