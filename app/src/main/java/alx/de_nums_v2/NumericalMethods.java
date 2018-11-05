package alx.de_nums_v2;

import java.util.ArrayList;

class NumericalMethods {

    /**
     * This is given function
     * @param x: first of input parameters
     * @param y: second of input parameters
     * @return result at the step of iteration
     */
    private double diff_equation_function(double x, double y) {
        return 2.0 * y * x + 5.0 - x * x;
    }

    /**
     * Analytical solution of differential equation: y = f(x, c)
     * @param x: variable of function
     * @param c: constant of function
     * @return value for combination of input parameters
     */
    private double exact_solution_function(double x, double c){
        return Math.exp(x*x) * (2.25 * Math.sqrt(Math.PI) * erf(x) + c) + 0.5*x;
        //return 2.25 * Math.sqrt(Math.PI) * Math.exp(x*x) * erf(x) + 0.5*x - c*Math.exp(x*x);
    }

    /**
     *  Service function used by exact solution: Gauss error function
     *  Available at: http://mathworld.wolfram.com/Erf.html, https://en.wikipedia.org/wiki/Error_function
     *
     *  erf(z) = 2 / sqrt(PI) * integral (e ^ (-x*x) dx) for x > 0, according to Wikipedia
     *
     *  Its Maclaurin series is the following:      n=Inf
     *                      erf(z) = 2 / sqrt(PI) * SUM [ (-1)^n * z^(2n+1) / ( n! * (2n+1) ) ]
     *                                              n=0
     * @param z: floating point number
     * @return real value of Gauss error function with accuracy of o(x^6)
     */
    private double erf(double z){
        // Modified version of https://introcs.cs.princeton.edu/java/21function/ErrorFunction.java.html
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
                t * ( 1.00002368 +
                        t * ( 0.37409196 +
                                t * ( 0.09678418 +
                                        t * (-0.18628806 +
                                                t * ( 0.27886807 +
                                                        t * (-1.13520398 +
                                                                t * ( 1.48851587 +
                                                                        t * (-0.82215223 +
                                                                                t * ( 0.17087277))))))))));
        if (z > 0.0){
            return  ans;
        } else if (z == 0.0) {
            return 0.0;
        } else return -ans;
    }

    /**  Four methods that return list of <x,y> values.
     Each method has 4 parameters:
     x_lower_bound, x_upper_bound: REAL, these two values define the x-axis interval for plotting
     step_amount                 : REAL, responses for number of iterations on defined interval
     constant || y_ivp_value     : REAL, responses for making the solution partial instead of general

     Each method returns list of values:
     value on each even index belongs to X-axis,
     value on each odd index belongs to Y-axis.
     */
    ArrayList<Double> calculate_exact_solution(double x_lower_bound, double x_upper_bound, double step_amount, double constant){
        ArrayList<Double> doubles = new ArrayList<>();
        Double x = x_lower_bound;
        double h = (x_upper_bound - x_lower_bound) / step_amount;
        Double u = exact_solution_function(x, constant);
        doubles.add(x);
        doubles.add(u);
        for(double i = 0.0; i < step_amount; i += 1.0){
            x += h;
            u = exact_solution_function(x, constant);
            if(!x.isNaN() && !u.isNaN()){
                doubles.add(x);
                doubles.add(u);
            }
        }

        return doubles;
    }

    /**  Four methods that return list of <x,y> values.
     Each method has 4 parameters:
     x_lower_bound, x_upper_bound: REAL, these two values define the x-axis interval for plotting
     step_amount                 : REAL, responses for number of iterations on defined interval
     constant || y_ivp_value     : REAL, responses for making the solution partial instead of general

     Each method returns list of values:
     value on each even index belongs to X-axis,
     value on each odd index belongs to Y-axis.
     */
    ArrayList<Double> approximate_by_euler_method(double x_lower_bound, double x_upper_bound, double step_amount, double y_ivp_value) {
        // initialize
        ArrayList<Double> doubles = new ArrayList<>();
        double h = (x_upper_bound - x_lower_bound) / step_amount;
        Double x = x_lower_bound;
        Double u = y_ivp_value;
        doubles.add(x);
        doubles.add(u);

        double i = 0.0;
        while(i < step_amount){
            // loop till the asymptote or interval end
            for (; i < step_amount; i += 1.0) {
                u += h * diff_equation_function(x, u);
                x += h;
                if (x.isNaN() || u.isNaN()) {
                    break;
                } else {
                    doubles.add(x);
                    doubles.add(u);
                }
            }

            // either it has found asymptote or it's finished
            if(i == step_amount){
                break;
            } else {
                double c = get_constant_from_ivp_problem(x_lower_bound, y_ivp_value);
                for (; i < step_amount; i += 1.0){
                    x += h;
                    u = exact_solution_function(x, c);
                    if (!x.isNaN() && !u.isNaN()){
                        break;
                    }
                }
            }
        }
        return doubles;
    }

    /**  Four methods that return list of <x,y> values.
     Each method has 4 parameters:
     x_lower_bound, x_upper_bound: REAL, these two values define the x-axis interval for plotting
     step_amount                 : REAL, responses for number of iterations on defined interval
     constant || y_ivp_value     : REAL, responses for making the solution partial instead of general

     Each method returns list of values:
     value on each even index belongs to X-axis,
     value on each odd index belongs to Y-axis.
     */
    ArrayList<Double> approximate_by_improved_euler_method(double x_lower_bound, double x_upper_bound, double step_amount, double y_ivp_value)  {
        // initialize
        ArrayList<Double> doubles = new ArrayList<>();
        double h = (x_upper_bound - x_lower_bound) / step_amount;
        Double x = x_lower_bound;
        Double u = y_ivp_value;
        doubles.add(x);
        doubles.add(u);

        // loop till the asymptote or interval end
        Double k1, k2;
        for (double i = 0.0; i < step_amount; i += 1.0) {
            k1 = diff_equation_function(x, u);
            k2 = diff_equation_function(x + h, u + h * k1);
            x += h;
            u += h * (k1 + k2) / 2.0;
            if (x.isNaN() || u.isNaN()) {
                break;
            } else {
                doubles.add(x);
                doubles.add(u);
            }
        }
        return doubles;
    }

    /**  Four methods that return list of <x,y> values.
     Each method has 4 parameters:
     x_lower_bound, x_upper_bound: REAL, these two values define the x-axis interval for plotting
     step_amount                 : REAL, responses for number of iterations on defined interval
     constant || y_ivp_value     : REAL, responses for making the solution partial instead of general

     Each method returns list of values:
     value on each even index belongs to X-axis,
     value on each odd index belongs to Y-axis.
     */
    ArrayList<Double> approximate_by_runge_kutta_method( double x_lower_bound, double x_upper_bound, double step_amount, double y_ivp_value) {
        // initialize
        ArrayList<Double> doubles = new ArrayList<>();
        double h = (x_upper_bound - x_lower_bound) / step_amount;
        Double x = x_lower_bound;
        Double u = y_ivp_value;
        doubles.add(x);
        doubles.add(u);

        // loop till the asymptote or interval end
        Double k1, k2, k3, k4;
        for (double i = 0.0; i < step_amount; i += 1.0) {
            k1 = diff_equation_function(x + 0.0, u + 0.0);
            k2 = diff_equation_function(x + h / 2.0, u + h * k1 / 2.0);
            k3 = diff_equation_function(x + h / 2.0, u + h * k2 / 2.0);
            k4 = diff_equation_function(x + h, u + h * k3);
            x += h;
            u += h * (k1 + k2 + k3 + k4) / 6.0;
            if (x.isNaN() || u.isNaN()) {
                break;
            } else {
                doubles.add(x);
                doubles.add(u);
            }
        }
        return doubles;
    }

    // Used to calculate constant manually for some way
    double get_constant_from_ivp_problem(double x_lower_bound, double ivp_value){
        double result = ivp_value - 0.5 * x_lower_bound;
        result /= Math.exp(x_lower_bound * x_lower_bound);
        result -= (2.25 * Math.sqrt(Math.PI) * erf(x_lower_bound));
        return result;
    }

    // Used for errors plotting
    // Analogue of python NumPy arrays: array_a - array_b
    ArrayList<Double> get_difference(ArrayList<Double> list_a, ArrayList<Double> list_b){
        ArrayList<Double> result = new ArrayList<>();
        int min_size = Math.min(list_a.size(), list_b.size());
        for (int i = 0; i < min_size; i+=2) {
            // add X
            result.add(list_a.get(i));
            // add Y
            result.add(Math.abs(list_a.get(i+1) - list_b.get(i+1)));
        }
        return result;
    }
}
