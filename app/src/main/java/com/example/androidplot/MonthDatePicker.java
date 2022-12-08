package com.example.androidplot;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MonthDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Calendar calendario = Calendar.getInstance();
        Integer dia = calendario.get(Calendar.DAY_OF_MONTH);
        Integer mes = calendario.get(Calendar.MONTH);
        Integer ano = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerHoloLight = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, ano,mes,dia);

        ((ViewGroup) datePickerHoloLight.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        return datePickerHoloLight;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {

        String tagClicada = getTag();

        if(tagClicada.equals("Mes")){
            EditText selectMonth = (EditText) getActivity().findViewById(R.id.selectMonth);
            selectMonth.setText((month+1)+"/"+year);
        }
    }
}
