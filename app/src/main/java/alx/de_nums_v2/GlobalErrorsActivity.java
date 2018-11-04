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
    DataPoint[] euler_array, impr_euler_array, runge_kutta_array;
    NumericalMethods methods;

    // Declare and fill ArrayLists as storage of values, separated by axis
    ArrayList<Double> approximated_euler_values, approximated_improved_euler_values;
    ArrayList<Double> approximated_runge_kutta_values;
    ArrayList<Double> exact_values;

    LineGraphSeries<DataPoint> euler_line, impr_euler_line, runge_kutta_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_errors);

        // Find all GUI units
        errorsView = findViewById(R.id.global_error_graphView);
        ArrayList<Double> error_values;
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

        // Create DataPoint arrays
        double constant = methods.get_constant_from_ivp_problem(x0, y0);
        error_values = methods.calculate_exact_solution(x0, x1, step_number, constant);
        euler_array = new DataPoint[error_values.size()];
        for (int i = 0; i < euler_array.length; i++) euler_array[i] = new DataPoint(i, 0);
        impr_euler_array = new DataPoint[error_values.size()];
        for (int i = 0; i < impr_euler_array.length; i++) impr_euler_array[i] = new DataPoint(i, 0);
        runge_kutta_array = new DataPoint[error_values.size()];
        for (int i = 0; i < runge_kutta_array.length; i++) runge_kutta_array[i] = new DataPoint(i, 0);
        // Big loop, starting from 1 because there's no sense in 0 steps
        int counter = 0;
        double actual_value;
        for (double i = 1.0; i < step_number; i += 1.0){
            // Get exact values of exact solution
            constant = methods.get_constant_from_ivp_problem(x0, y0);
            exact_values = methods.calculate_exact_solution(x0, x1, i, constant);

            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            if (euler_bool.equals("T")) {
                approximated_euler_values = methods.approximate_by_euler_method(x0, x1, step_number, y0);
                error_values.clear();
                error_values = methods.get_difference(approximated_euler_values, exact_values);
                actual_value = get_max_value(error_values);
                euler_array[counter] = new DataPoint(i, actual_value);
            } else approximated_euler_values = new ArrayList<>(0);

            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            if (eulerPlus_bool.equals("T")) {
                approximated_improved_euler_values = methods.approximate_by_improved_euler_method(x0, x1, step_number, y0);
                error_values.clear();
                error_values = methods.get_difference(approximated_improved_euler_values, exact_values);
                actual_value = get_max_value(error_values);
                impr_euler_array[counter] = new DataPoint(i, actual_value);
            } else approximated_improved_euler_values = new ArrayList<>(0);

            // Get exact values of approximating method, then - get its error arrayList, then - add new DataPoint to its array
            if (rungeKutta_bool.equals("T")) {
                approximated_runge_kutta_values = methods.approximate_by_runge_kutta_method(x0, x1, step_number, y0);
                error_values.clear();
                error_values = methods.get_difference(approximated_runge_kutta_values, exact_values);
                actual_value = get_max_value(error_values);
                runge_kutta_array[counter] = new DataPoint(i, actual_value);
            } else approximated_runge_kutta_values = new ArrayList<>(0);

            counter += 1;
        }

        // Build lines for graphView
        if(euler_bool.equals("T")) {
            euler_line = create_line_series(euler_array, Color.BLUE, "Euler method");
            errorsView.addSeries(euler_line);
        }
        if(eulerPlus_bool.equals("T")){
            impr_euler_line = create_line_series(impr_euler_array, Color.GREEN, "Improved Euler method");
            errorsView.addSeries(impr_euler_line);
        }
        if(rungeKutta_bool.equals("T")){
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
    private double get_max_value(ArrayList<Double> doubles){
        double result = .0;
        for (int i = 0; i < doubles.size(); i+=2)
            result += doubles.get(i+1);
        return result;
    }
}
