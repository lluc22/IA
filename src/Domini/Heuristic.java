package Domini;

import aima.search.framework.HeuristicFunction;

import java.util.Arrays;

public class Heuristic  implements HeuristicFunction{



    @Override
    public double getHeuristicValue(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double mean = 0.0;
        double max = 0.0;
        double M2 = 0.0;
        double sd;
        double sum = 0.0;
        double m = 0;
        for(int i = 0; i < n ; ++i) {
            m+=1;
            double t = state.getTime(i);
            if(t > max) max = t;
            sum =sum + t;
            double delta = t - mean;
            mean += delta / m;
            M2 += delta * (t - mean);
        }
        if (m < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / (m - 1));
        return sd + sum + max;
    }

    static public double getSD(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double mean = 0.0;
        double M2 = 0.0;
        double sd;
        double m = 0;
        for(int i = 0; i < n ; ++i) {
            m+=1;
            double t = state.getTime(i);
            double delta = t - mean;
            mean += delta / m;
            M2 += delta * (t - mean);
        }
        if (m < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / (m - 1));
        return sd;
    }

    static public double getMax(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double max = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if(t > max) max = t;
        }
        return max;
    }

    static public double getSum(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double sum = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            sum =sum + t;
        }
        return sum;
    }


}


