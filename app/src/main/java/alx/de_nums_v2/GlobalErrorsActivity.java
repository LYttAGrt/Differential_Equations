package alx.de_nums_v2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GlobalErrorsActivity extends AppCompatActivity {

    // Uses GraphView library
    GraphView errorsView;
    DataPoint[] euler_array, improved_euler_array, runge_kutta_array;
    NumericalMethods methods;

    LineGraphSeries<DataPoint> euler_line, improved_euler_line, runge_kutta_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_errors);

        // Find all GUI units
        errorsView = findViewById(R.id.global_error_graphView);
        methods = new NumericalMethods();

        // All right. Read all input via Intent
        double x0 = .0, y0 = .0, x1 = .0, step_number = .0;
        String euler_bool = "F", eulerPlus_bool = "F", rungeKutta_bool = "F";
        Intent intent;

        try /* to get all input */ {
            intent = getIntent();
            x0 = Double.parseDouble(intent.getSerializableExtra("X0").toString());
            y0 = Double.parseDouble(intent.getSerializableExtra("Y0").toString());
            x1 = Double.parseDouble(intent.getSerializableExtra("X1").toString());
            euler_bool = intent.getSerializableExtra("DRAW_EULER").toString();
            eulerPlus_bool = intent.getSerializableExtra("DRAW_EULER_PLUS").toString();
            rungeKutta_bool = intent.getSerializableExtra("DRAW_RUNGE_KUTTA").toString();
            step_number = Integer.parseInt(intent.getSerializableExtra("STEPS").toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error of parsing!", Toast.LENGTH_SHORT).show();
            intent = new Intent(GlobalErrorsActivity.this, DefineActivity.class);
            startActivity(intent);
        }

        // Build lines for graphView
        if (euler_bool.equals("T")) {
            euler_array = create_dataPoints_for_euler_method(x0, x1, step_number, y0);
            euler_line = create_line_series(euler_array, Color.BLUE, "Euler method");
            errorsView.addSeries(euler_line);
        }

        if (eulerPlus_bool.equals("T")) {
            improved_euler_array = create_dataPoints_for_improved_euler_method(x0, x1, step_number, y0);
            improved_euler_line = create_line_series(improved_euler_array, Color.GREEN, "Improved Euler method");
            errorsView.addSeries(improved_euler_line);
        }

        if (rungeKutta_bool.equals("T")) {
            runge_kutta_array = create_dataPoints_for_runge_kutta_method(x0, x1, step_number, y0);
            runge_kutta_line = create_line_series(runge_kutta_array, Color.RED, "Runge-Kutta method");
            errorsView.addSeries(runge_kutta_line);
        }

        // add nice settings
        errorsView.getViewport().setScalable(true);
        errorsView.getViewport().setScalableY(true);
        errorsView.getViewport().setScrollable(true);
        errorsView.getViewport().setScrollableY(true);
        errorsView.getLegendRenderer().setVisible(true);
        errorsView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    // Local method used to simplify the code
    private LineGraphSeries<DataPoint> create_line_series(DataPoint[] points, int color, String title){
        // Fill the line, add settings and return
        LineGraphSeries<DataPoint> result = new LineGraphSeries<>(points);
        result.setColor(color);
        result.setTitle(title);
        return result;
    }

    // Local service method
    private double get_sum_of_list(ArrayList<Double> doubles){
        double result = .0;
        for (int i = 0; i < doubles.size(); i+=2)
            result += doubles.get(i+1);
        return result;
    }

    // Local service method
    private DataPoint[] create_dataPoints_for_euler_method(double x0, double x1, double step_number, double y0){
        methods = new NumericalMethods();
        double actual_value;
        ArrayList<Double> approximated_values, exact_values, error_values;

        double constant = methods.get_constant_from_ivp_problem(x0, y0);
        exact_values = methods.calculate_exact_solution(x0, x1, step_number, constant);
        DataPoint[] result = new DataPoint[exact_values.size() / 2];
        exact_values.clear();

        int counter = 0;
        for (double i = 1.0; i < step_number; i += 1.0) {
            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            exact_values = methods.calculate_exact_solution(x0, x1, i, constant);
            approximated_values = methods.approximate_by_euler_method(x0, x1, step_number, y0);

            error_values = methods.get_difference(approximated_values, exact_values);
            actual_value = get_sum_of_list(error_values);
            result[counter] = new DataPoint(i, actual_value);

            approximated_values.clear();
            error_values.clear();
            exact_values.clear();
            counter += 1;
        }
        return result;
    }

    // Local service method
    private DataPoint[] create_dataPoints_for_improved_euler_method(double x0, double x1, double step_number, double y0){
        methods = new NumericalMethods();
        double actual_value;
        ArrayList<Double> approximated_values, exact_values, error_values;

        double constant = methods.get_constant_from_ivp_problem(x0, y0);
        exact_values = methods.calculate_exact_solution(x0, x1, step_number, constant);
        DataPoint[] result = new DataPoint[exact_values.size() / 2];
        exact_values.clear();

        int counter = 0;
        for (double i = 1.0; i < step_number; i += 1.0) {
            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            exact_values = methods.calculate_exact_solution(x0, x1, i, constant);
            approximated_values = methods.approximate_by_improved_euler_method(x0, x1, step_number, y0);

            error_values = methods.get_difference(approximated_values, exact_values);
            actual_value = get_sum_of_list(error_values);
            result[counter] = new DataPoint(i, actual_value);

            approximated_values.clear();
            error_values.clear();
            exact_values.clear();
            counter += 1;
        }
        return result;
    }

    // Local service method
    private DataPoint[] create_dataPoints_for_runge_kutta_method(double x0, double x1, double step_number, double y0){
        methods = new NumericalMethods();
        double actual_value;
        ArrayList<Double> approximated_values, exact_values, error_values;

        double constant = methods.get_constant_from_ivp_problem(x0, y0);
        exact_values = methods.calculate_exact_solution(x0, x1, step_number, constant);
        DataPoint[] result = new DataPoint[exact_values.size() / 2];
        exact_values.clear();

        int counter = 0;
        for (double i = 1.0; i < step_number; i += 1.0) {
            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            exact_values = methods.calculate_exact_solution(x0, x1, i, constant);
            approximated_values = methods.approximate_by_runge_kutta_method(x0, x1, step_number, y0);

            error_values = methods.get_difference(approximated_values, exact_values);
            actual_value = get_sum_of_list(error_values);
            result[counter] = new DataPoint(i, actual_value);

            approximated_values.clear();
            error_values.clear();
            exact_values.clear();
            counter += 1;
        }
        return result;
    }
}
