package com.example.androidplot;

import static com.example.androidplot.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.graphics.*;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.ui.SeriesRenderer;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.text.DecimalFormat;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private XYPlot myPlot;
    private XYSeries series1,series2,series3,series4;
    private MyBarFormatter formatter1, formatter2, formatter3, formatter4;
    private CheckBox boxManha, boxMeioDia, boxTarde, boxNoite;
    private ImageView buttonDataInicial;
    EditText selecData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);


        plotBarGraph();


        Calendar calendario = Calendar.getInstance();
        String dataDeHoje = (calendario.get(Calendar.MONTH)+1)+"/"+calendario.get(Calendar.YEAR);

        EditText selectDataInicial= (EditText) findViewById(id.selectMonth);
        selectDataInicial.setText(dataDeHoje);

        ImageView selectMonthButtom = (ImageView) findViewById(id.selectMonthButtom);

        selectMonthButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new MonthDatePicker();
                dialogFragment.show(getSupportFragmentManager(),"Mes");
            }
        });


    }

    private void plotBarGraph(){
        data_fluxo data = new data_fluxo(); //Busca os dados de fluxo de pessoas

        myPlot = findViewById(id.barPlotMensal);
        PanZoom.attach(myPlot,PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.NONE); // Habilita apenas o scroll horizontal
        myPlot.getOuterLimits().set(0, 31.5, 0, 400);

        // Cria as séries que serão plotada no gráfico
        series1 = new SimpleXYSeries(Arrays.asList(data.getDias()), Arrays.asList(data.getTurnoManha()),"Manhã");
        series2 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoMeioDia()), "Meio Dia");
        series3 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoTarde()), "Tarde");
        series4 = new SimpleXYSeries(Arrays.asList(data.getDias()),Arrays.asList(data.getTurnoNoite()), "Noite");

        // Define o formato em barra, cor do grafico e cor da margem
        formatter1 = new MyBarFormatter(Color.BLUE, Color.BLACK);
        formatter2 = new MyBarFormatter(Color.RED, Color.BLACK);
        formatter3 = new MyBarFormatter(Color.GRAY, Color.BLACK);
        formatter4 = new MyBarFormatter(Color.GREEN, Color.BLACK);

        myPlot.addSeries(series1, formatter1);
        myPlot.addSeries(series2, formatter2);
        myPlot.addSeries(series3, formatter3);
        myPlot.addSeries(series4, formatter4);

        //Altera para formato decimal os valores dos eixos
        myPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new DecimalFormat("####"));
        myPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).setFormat(new DecimalFormat("####"));

        // Define valores fixos a origem para que as linhas do grid se movimentem dinamicamente com o scroll
        myPlot.setUserDomainOrigin(0);
        myPlot.setUserRangeOrigin(0);

        myPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 50);
        myPlot.setRangeBoundaries(0,400, BoundaryMode.FIXED);

        // Define o espaçamento do dominio de 1 em 1 e o tamanho total de 5
        myPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        myPlot.setDomainBoundaries(0.5,5,BoundaryMode.FIXED );

        myPlot.getLegend().setVisible(false); // Desabilita a legenda
        //myPlot.getLegend().setTableModel(new DynamicTableModel(2, 2, TableOrder.ROW_MAJOR));


        // Define a largura e o modo de exibição das barras como "empiplhadas"
        MyBarRender render = (MyBarRender) myPlot.getRenderer(MyBarRender.class);
        render.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(35));
        render.setBarOrientation(BarRenderer.BarOrientation.STACKED);

        //Check box para adicionar e remover series do grafico
        boxManha = (CheckBox) findViewById(id.checkBoxManha);
        boxMeioDia = (CheckBox) findViewById(id.checkBoxMeioDia);
        boxTarde = (CheckBox) findViewById(id.checkBoxTarde);
        boxNoite = (CheckBox) findViewById(id.checkBoxNoite);

        checkBoxsListeners();

        boxManha.setChecked(true);
        boxMeioDia.setChecked(true);
        boxTarde.setChecked(true);
        boxNoite.setChecked(true);

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




