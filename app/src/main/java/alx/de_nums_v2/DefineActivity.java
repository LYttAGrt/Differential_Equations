package alx.de_nums_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DefineActivity extends AppCompatActivity {

    CheckBox euler_cb, euler_plus_cb, runge_kutta_cb;
    EditText x0_editText, y0_editText, X_editText, step_editText;
    Button btnPlotSolutions, btnPlotErrors, btnPlotGlobalErrors;
    String needDrawEuler, needDrawEulerPlus, needDrawRungeKutta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define);

        // Find all Views
        euler_cb = findViewById(R.id.checkBox_euler);
        euler_plus_cb = findViewById(R.id.checkBox_euler_plus);
        runge_kutta_cb = findViewById(R.id.checkBox_runge_kutta);
        x0_editText = findViewById(R.id.editText_x0);
        y0_editText = findViewById(R.id.editText_ivp);
        X_editText = findViewById(R.id.editText_x1);
        step_editText = findViewById(R.id.editText_step);

        btnPlotSolutions = findViewById(R.id.buttonDrawSolutions);
        btnPlotSolutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needDrawEuler = (euler_cb.isChecked()) ? "T" : "F";
                needDrawEulerPlus = (euler_plus_cb.isChecked()) ? "T" : "F";
                needDrawRungeKutta = (runge_kutta_cb.isChecked()) ? "T" : "F";

                // Try to get values from EditTexts
                double x0, y0, x1;
                int step_amount;
                try {
                    x0 = Double.parseDouble(x0_editText.getText().toString());
                    y0 = Double.parseDouble(y0_editText.getText().toString());
                    x1 = Double.parseDouble(X_editText.getText().toString());
                    step_amount = Integer.parseInt(step_editText.getText().toString());
                    if(step_amount < 1) step_amount = 1;

                    // Standby to send the message
                    Intent intent = new Intent(DefineActivity.this, ApproximationsActivity.class)
                            .putExtra("DRAW_EULER", needDrawEuler)
                            .putExtra("DRAW_EULER_PLUS", needDrawEulerPlus)
                            .putExtra("DRAW_RUNGE_KUTTA", needDrawRungeKutta)
                            .putExtra("X0", x0)
                            .putExtra("Y0", y0)
                            .putExtra("X1", x1)
                            .putExtra("STEPS", step_amount);
                    // Send it
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Incorrect values!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPlotErrors = findViewById(R.id.buttonDrawErrors);
        btnPlotErrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needDrawEuler = (euler_cb.isChecked()) ? "T" : "F";
                needDrawEulerPlus = (euler_plus_cb.isChecked()) ? "T" : "F";
                needDrawRungeKutta = (runge_kutta_cb.isChecked()) ? "T" : "F";

                // Try to get values from EditTexts
                double x0, y0, x1;
                int step_amount;
                try {
                    x0 = Double.parseDouble(x0_editText.getText().toString());
                    y0 = Double.parseDouble(y0_editText.getText().toString());
                    x1 = Double.parseDouble(X_editText.getText().toString());
                    step_amount = Integer.parseInt(step_editText.getText().toString());
                    if(step_amount < 1) step_amount = 1;

                    // Standby to send the message
                    Intent intent = new Intent(DefineActivity.this, ErrorsActivity.class)
                            .putExtra("DRAW_EULER", needDrawEuler)
                            .putExtra("DRAW_EULER_PLUS", needDrawEulerPlus)
                            .putExtra("DRAW_RUNGE_KUTTA", needDrawRungeKutta)
                            .putExtra("X0", x0)
                            .putExtra("Y0", y0)
                            .putExtra("X1", x1)
                            .putExtra("STEPS", step_amount);
                    // Send it
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Incorrect values!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPlotGlobalErrors = findViewById(R.id.buttonDrawGlobalErrors);
        btnPlotGlobalErrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                needDrawEuler = (euler_cb.isChecked()) ? "T" : "F";
                needDrawEulerPlus = (euler_plus_cb.isChecked()) ? "T" : "F";
                needDrawRungeKutta = (runge_kutta_cb.isChecked()) ? "T" : "F";

                // Send a warning
                Toast.makeText(getApplicationContext(), "Calculating...", Toast.LENGTH_SHORT).show();

                // Try to get values from EditTexts
                double x0, y0, x1;
                int step_amount;
                try {
                    x0 = Double.parseDouble(x0_editText.getText().toString());
                    y0 = Double.parseDouble(y0_editText.getText().toString());
                    x1 = Double.parseDouble(X_editText.getText().toString());
                    step_amount = Integer.parseInt(step_editText.getText().toString());
                    if(step_amount < 1) step_amount = 1;

                    // Standby to send the message
                    Intent intent = new Intent(DefineActivity.this, GlobalErrorsActivity.class)
                            .putExtra("DRAW_EULER", needDrawEuler)
                            .putExtra("DRAW_EULER_PLUS", needDrawEulerPlus)
                            .putExtra("DRAW_RUNGE_KUTTA", needDrawRungeKutta)
                            .putExtra("X0", x0)
                            .putExtra("Y0", y0)
                            .putExtra("X1", x1)
                            .putExtra("STEPS", step_amount);
                    // Send it
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Incorrect values!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
