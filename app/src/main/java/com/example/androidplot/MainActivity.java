package com.example.androidplot;

import static com.example.androidplot.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.graphics.*;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.ui.Anchor;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.SeriesBundle;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import com.androidplot.Region;

import java.text.DecimalFormat;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private static final String NO_SELECTION_TXT = "Touch bar to select.";
    private XYPlot myPlot;
    private XYSeries series1,series2,series3,series4;
    private MyBarFormatter formatter1, formatter2, formatter3, formatter4,selectionFormatter;
    private CheckBox boxManha, boxMeioDia, boxTarde, boxNoite;
    private TextLabelWidget selectionWidget;
    private Pair<Integer, XYSeries> selection;

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
        //formatter1.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
        formatter2 = new MyBarFormatter(Color.RED, Color.BLACK);
        //formatter2.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
        formatter3 = new MyBarFormatter(Color.GRAY, Color.BLACK);
        //formatter3.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
        formatter4 = new MyBarFormatter(Color.GREEN, Color.BLACK);
        //formatter4.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
        selectionFormatter = new MyBarFormatter(Color.YELLOW, Color.WHITE);

        //selectionBar2ShowValue();

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

        myPlot.getGraph().getBackgroundPaint().setColor(Color.TRANSPARENT);

        myPlot.setRangeStep(StepMode.INCREMENT_BY_VAL, 50);
        myPlot.setRangeBoundaries(0,400, BoundaryMode.FIXED);

        // Define o espaçamento do dominio de 1 em 1 e o tamanho total de 5
        myPlot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        myPlot.setDomainBoundaries(0.5,5,BoundaryMode.FIXED );

        myPlot.getLegend().setVisible(false); // Desabilita a legenda
        //myPlot.getLegend().setTableModel(new DynamicTableModel(2, 2, TableOrder.ROW_MAJOR));


        // Define a largura e o modo de exibição das barras como "empiplhadas"
        MyBarRenderer render = (MyBarRenderer) myPlot.getRenderer(MyBarRenderer.class);
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

    void selectionBar2ShowValue(){
        selectionWidget = new TextLabelWidget(myPlot.getLayoutManager(), NO_SELECTION_TXT,
                new Size(
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE),
                TextOrientation.HORIZONTAL);

        selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));

        selectionWidget.position(
                0, HorizontalPositioning.RELATIVE_TO_CENTER,
                PixelUtils.dpToPix(45), VerticalPositioning.ABSOLUTE_FROM_TOP,
                Anchor.TOP_MIDDLE);
        selectionWidget.pack();

        // add a dark, semi-transparent background to the selection label widget:
        Paint p = new Paint();
        p.setARGB(100, 0, 0, 0);
        selectionWidget.setBackgroundPaint(p);

        myPlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(new PointF(motionEvent.getX(), motionEvent.getY()));
                }
                return true;
            }
        });


    }
    private void onPlotClicked(PointF point) {

        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        if (myPlot.containsPoint(point.x, point.y)) {
            Number x = myPlot.getXVal(point);
            Number y = myPlot.getYVal(point);

            selection = null;
            double xDistance = 0;
            double yDistance = 0;

            // find the closest value to the selection:
            for (SeriesBundle<XYSeries, ? extends XYSeriesFormatter> sfPair : myPlot.getRegistry().getSeriesAndFormatterList()) {
                XYSeries series = sfPair.getSeries();
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance = Region.measure(x, thisX).doubleValue();
                        double thisYDistance = Region.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance &&
                                thisYDistance < yDistance && thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }

        } else {
            // if the press was outside the graph area, deselect:
            selection = null;
        }

        if (selection == null) {
            selectionWidget.setText(NO_SELECTION_TXT);
        } else {
            selectionWidget.setText("Selecionado " + selection.second.getTitle() + " Valor: " + selection.second.getY(selection.first));
        }
        myPlot.redraw();
    }

    class MyBarFormatter extends BarFormatter {

        public MyBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return MyBarRenderer.class;
        }

        @Override
        public SeriesRenderer doGetRendererInstance(XYPlot plot) {
            return new MyBarRenderer(plot);
        }
    }

    class MyBarRenderer extends BarRenderer<MyBarFormatter> {

        public MyBarRenderer(XYPlot plot) {
            super(plot);
        }

        /**
         * Implementing this method to allow us to inject our
         * special selection getFormatter.
         * @param index index of the point being rendered.
         * @param series XYSeries to which the point being rendered belongs.
         * @return
         */
        @Override
        public MyBarFormatter getFormatter(int index, XYSeries series) {
            if (selection != null &&
                    selection.second == series &&
                    selection.first == index) {
                return selectionFormatter;
            } else {
                return getFormatter(series);
            }
        }
    }

}

/*
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

 */




