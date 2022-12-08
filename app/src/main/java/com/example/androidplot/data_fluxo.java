package com.example.androidplot;

public class data_fluxo {
    private Number[] dias = {1,2,3,4,5,6,7,8,9,10, 11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    //private int[] turnoManha, turnoMeioDia, turnoTarde, turnoNoite;

    private Number[] turnoManha = {50,0,95, 4, 5 ,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,200};
    private Number[] turnoMeioDia = {50,55,0, 50, 20 ,0,0,0,0,0,0,0,0,0,30,50,0,0,0,0,60,0,0,0,0,0,0,0,0,0,0};
    private Number[] turnoTarde = {50,80,76, 40, 40,0,0,0,0,0,0,0,0,0,0,0,20,06,0,0,0,0,0,50,0,0,07,0,0,0,0};
    private Number[] turnoNoite = {50,0,200,25,20 ,0,0,0,0,0,0,0,0,0,01,0,0,0,0,0,0,30,0,0,0,70,0,0,0,0,0};


    public void setTurnoManha(Number[] turnoManha) {
        this.turnoManha = turnoManha;
    }
    public Number[] getTurnoManha() {
        return turnoManha;
    }

    public Number[] getTurnoMeioDia() {
        return turnoMeioDia;
    }
    public void setTurnoMeioDia(Number[] turnoMeioDia) {
        this.turnoMeioDia = turnoMeioDia;
    }

    public Number[] getTurnoTarde() {
        return turnoTarde;
    }
    public void setTurnoTarde(Number[] turnoTarde) {
        this.turnoTarde = turnoTarde;
    }

    public Number[] getTurnoNoite() {
        return turnoNoite;
    }
    public void setTurnoNoite(Number[] turnoNoite) {
        this.turnoNoite = turnoNoite;
    }

    public Number[] getDias() {
        return dias;
    }
}
