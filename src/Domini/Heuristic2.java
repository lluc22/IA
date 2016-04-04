package Domini;

import aima.search.framework.HeuristicFunction;

/**
 * Created by asus on 3/4/2016.
 */
public class Heuristic2 implements HeuristicFunction{

    @Override
    public double getHeuristicValue(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double max = 0.0;
        int t;
        for(int i = 0; i < n ; ++i) {
            t = state.mTempsServidors[i];
            if (t > max) max = t;
        }
        return max;
    }

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

    static public double getSum(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double sum = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.mTempsServidors[i-1];
            sum += t;
        }
        return sum;
    }


}

