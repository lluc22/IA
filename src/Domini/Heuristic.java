package Domini;

import aima.search.framework.HeuristicFunction;

import java.util.Arrays;

public class Heuristic  implements HeuristicFunction{
    @Override
    public double getHeuristicValue(Object o) {
        double p_sd = 0.5;
        Estat state = (Estat) o;
        int n = state.serversNum();
        double mean = 0.0;
        double max = 0.0;
        double M2 = 0.0;
        double sd;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if( t > max ) max = t;
            double delta = t - mean;
            mean += delta / i;
            M2 += delta * (t - mean);
        }
        if (n < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / n);
        return p_sd * sd + (1-p_sd) * max;
    }

}


