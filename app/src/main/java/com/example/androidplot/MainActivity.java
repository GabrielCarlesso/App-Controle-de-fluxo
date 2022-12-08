package com.example.androidplot;

import static com.example.androidplot.R.*;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private XYPlot myPlot;
    private XYSeries series1,series2,series3,series4;
    private MyBarFormatter formatter1, formatter2, formatter3, formatter4;
    private CheckBox boxManha, boxMeioDia, boxTarde, boxNoite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        data_fluxo data = new data_fluxo();
        myPlot = findViewById(id.barPlotMensal);
        PanZoom.attach(myPlot,PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.NONE);

        TextView titulo = (TextView) findViewById(id.titulo);
        titulo.setText("Plot");

        boxManha = (CheckBox) findViewById(id.checkBoxManha);
        boxMeioDia = (CheckBox) findViewById(id.checkBoxMeioDia);
        boxTarde = (CheckBox) findViewById(id.checkBoxTarde);
        boxNoite = (CheckBox) findViewById(id.checkBoxNoite);
        checkBoxsListeners();

        boxManha.setChecked(true);
        boxMeioDia.setChecked(true);
        boxTarde.setChecked(true);
        boxNoite.setChecked(true);

        series1 = new SimpleXYSeries(Arrays.asList(data.getDias()), Arrays.asList(data.getTurnoManha()),"Manhã");
        series2 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoMeioDia()), "Meio Dia");
        series3 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoTarde()), "Tarde");
        series4 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoNoite()), "Noite");


        formatter1 = new MyBarFormatter(Color.BLUE, Color.BLACK);
        formatter2 = new MyBarFormatter(Color.RED, Color.BLACK);
        formatter3 = new MyBarFormatter(Color.GRAY, Color.BLACK);
        formatter4 = new MyBarFormatter(Color.GREEN, Color.BLACK);


        myPlot.addSeries(series1, formatter1);
        myPlot.addSeries(series2, formatter2);
        myPlot.addSeries(series3, formatter3);
        myPlot.addSeries(series4, formatter4);

        double[] inc_domain = new double[]{1};
        myPlot.setDomainStepModel(new StepModelFit(myPlot.getBounds().getxRegion(),inc_domain,0));
        myPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new DecimalFormat("####"));

        // set a fixed origin and a "by-value" step mode so that grid lines will
        // move dynamically with the data when the users pans or zooms:
        myPlot.setUserDomainOrigin(0);
        myPlot.setUserRangeOrigin(0);

        myPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 50);
        myPlot.setRangeBoundaries(0,400, BoundaryMode.FIXED);

        //myPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        myPlot.setDomainBoundaries(0,5,BoundaryMode.FIXED );

        myPlot.getLegend().setVisible(false);

        MyBarRender render = (MyBarRender) myPlot.getRenderer(MyBarRender.class);
        render.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(35));
        render.setBarOrientation(BarRenderer.BarOrientation.STACKED);



    }

    private void manhaCheckBoxClicked(boolean checked){
        if (checked) {
            myPlot.addSeries(series1, formatter1);
        } else {
            myPlot.removeSeries(series1);
        }
        myPlot.redraw();
    }
    private void meioDiaCheckBoxClicked(boolean checked){
        if (checked) {
            myPlot.addSeries(series2, formatter2);
        } else {
            myPlot.removeSeries(series2);
        }
        myPlot.redraw();
    }
    private void tardeCheckBoxClicked(boolean checked){
        if (checked) {
            myPlot.addSeries(series3, formatter3);
        } else {
            myPlot.removeSeries(series3);
        }
        myPlot.redraw();
    }
    private void noiteCheckBoxClicked(boolean checked){
        if (checked) {
            myPlot.addSeries(series4, formatter4);
        } else {
            myPlot.removeSeries(series4);
        }
        myPlot.redraw();
    }

    private void checkBoxsListeners(){
        boxManha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manhaCheckBoxClicked(((CheckBox) v).isChecked());
            }
        });
        boxMeioDia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                meioDiaCheckBoxClicked(((CheckBox) v).isChecked());
            }
        });
        boxTarde.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tardeCheckBoxClicked(((CheckBox) v).isChecked());
            }
        });
        boxNoite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noiteCheckBoxClicked(((CheckBox) v).isChecked());
            }
        });
    }

}


class MyBarFormatter extends BarFormatter{
    public MyBarFormatter(int fillColor, int borderColor) {
        super(fillColor, borderColor);
    }

    @Override
    public Class<? extends SeriesRenderer> getRendererClass() {
        return MyBarRender.class;
    }

    @Override
    public SeriesRenderer doGetRendererInstance(XYPlot plot) {
        return new MyBarRender(plot);
    }
}

class MyBarRender extends BarRenderer<MyBarFormatter>{
    public MyBarRender(XYPlot plot) {
        super(plot);
    }
}



