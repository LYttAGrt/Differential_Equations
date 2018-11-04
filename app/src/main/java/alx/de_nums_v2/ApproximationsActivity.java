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

public class ApproximationsActivity extends AppCompatActivity {

    // Uses GraphView library
    GraphView graphView;
    LineGraphSeries<DataPoint> series_euler, series_euler_plus, series_runge_kutta, series_exact;
    DataPoint[] dataPoints;
    NumericalMethods methods;

    // Declare and fill ArrayLists as storage of values, separated by axis
    ArrayList<Double> approximated_euler_values;
    ArrayList<Double> approximated_improved_euler_values;
    ArrayList<Double> approximated_runge_kutta_values;
    ArrayList<Double> exact_values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approximations);

        // Find all GUI units
        graphView = findViewById(R.id.graphView_approximations);
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
            intent = new Intent(ApproximationsActivity.this, DefineActivity.class);
            startActivity(intent);
        }

        // Fill and separate ArrayLists for Euler method, then create LineGraphSeries for it
        if (euler_bool.equals("T")) {
            approximated_euler_values = methods.approximate_by_euler_method(x0, x1, step_number, y0);
            series_euler = create_line_series(approximated_euler_values, Color.BLUE, "Euler method");
        } else {
            approximated_euler_values = new ArrayList<>(0);
        }

        // Fill and separate ArrayLists for improved Euler method, then create LineGraphSeries for it
        if (eulerPlus_bool.equals("T")) {
            approximated_improved_euler_values = methods.approximate_by_improved_euler_method(x0, x1, step_number, y0);
            series_euler_plus = create_line_series(approximated_improved_euler_values, Color.GREEN, "Improved Euler method");
        } else {
            approximated_improved_euler_values = new ArrayList<>(0);
        }

        // Fill and separate ArrayLists for Runge-Kutta method, then create LineGraphSeries for it
        if (rungeKutta_bool.equals("T")) {
            approximated_runge_kutta_values = methods.approximate_by_runge_kutta_method(x0, x1, step_number, y0);
            series_runge_kutta = create_line_series(approximated_runge_kutta_values, Color.RED, "Runge-Kutta method");
        } else {
            approximated_runge_kutta_values = new ArrayList<>(0);
        }

        // Do the same with exact, then create LineGraphSeries for it
        double constant = methods.get_constant_from_ivp_problem(x0, y0);
        exact_values = methods.calculate_exact_solution(x0, x1, step_number, constant);
        series_exact = create_line_series(exact_values, Color.YELLOW, "Exact solution");

        // Add all lines to the graphView, then add nice settings for it
        if(exact_values.size() > 0)                         graphView.addSeries(series_exact);
        if(approximated_euler_values.size() > 0)            graphView.addSeries(series_euler);
        if(approximated_improved_euler_values.size() > 0)   graphView.addSeries(series_euler_plus);
        if(approximated_runge_kutta_values.size() > 0)      graphView.addSeries(series_runge_kutta);

        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
        graphView.getViewport().setScrollable(true);
        graphView.getViewport().setScrollableY(true);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        approximated_euler_values.clear();
        approximated_improved_euler_values.clear();
        approximated_runge_kutta_values.clear();
        exact_values.clear();
    }

    // Local method used to simplify the code
    private LineGraphSeries<DataPoint> create_line_series(ArrayList<Double> values, int color, String title){
        // Separate the values
        ArrayList<Double> x_values = new ArrayList<>();
        ArrayList<Double> y_values = new ArrayList<>();
        for (int i = 0; i < values.size(); i+=2) {
            x_values.add(values.get(i));
            y_values.add(values.get(i+1));
        }

        // Create new points
        dataPoints = new DataPoint[x_values.size()];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(x_values.get(i), y_values.get(i));
        }

        // Fill the line, add settings and return
        LineGraphSeries<DataPoint> result = new LineGraphSeries<>(dataPoints);
        result.setColor(color);
        result.setTitle(title);
        return result;
    }
}
