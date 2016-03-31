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
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if(t > max) max = t;
            sum =sum + t;
            double delta = t - mean;
            mean += delta / i;
            M2 += delta * (t - mean);
        }
        if (n < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / n);
        return sd + sum + max;
    }

    static public double getSD(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double mean = 0.0;
        double max = 0.0;
        double M2 = 0.0;
        double sd;
        double sum = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if(t > max) max = t;
            sum =sum + t;
            double delta = t - mean;
            mean += delta / i;
            M2 += delta * (t - mean);
        }
        if (n < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / n);
        return sd;
    }

    static public double getMax(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double mean = 0.0;
        double max = 0.0;
        double M2 = 0.0;
        double sd;
        double sum = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if(t > max) max = t;
            sum =sum + t;
            double delta = t - mean;
            mean += delta / i;
            M2 += delta * (t - mean);
        }
        if (n < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / n);
        return max;
    }

    static public double getSum(Object o) {
        Estat state = (Estat) o;
        int n = state.mTempsServidors.length;
        double mean = 0.0;
        double max = 0.0;
        double M2 = 0.0;
        double sd;
        double sum = 0.0;
        for(int i = 1; i <= n ; ++i) {
            double t = state.getTime(i - 1);
            if(t > max) max = t;
            sum =sum + t;
            double delta = t - mean;
            mean += delta / i;
            M2 += delta * (t - mean);
        }
        if (n < 2) sd = Double.NaN;
        else sd =  Math.sqrt(M2 / n);
        return sum;
    }


}


