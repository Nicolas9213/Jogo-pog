package com.jogo.game;

import java.util.Calendar;

public class Temporizador {

	private long tempoFinal, tempoInicial;
	private Calendar c;
	
	public Temporizador() {
		c = Calendar.getInstance();
		tempoInicial = c.getTimeInMillis();
	}
	
	public String getTempo() {
		c = Calendar.getInstance();
		tempoFinal = c.getTimeInMillis();
		String tempoString = String.valueOf((tempoFinal - tempoInicial) / 1000);
		return "Tempo: " + tempoString;
	}
	
	public void setTempo(long tempo) {
		this.tempoInicial = tempo;
	}
	
	public String getTempoMorte() {
		String tempoString = String.valueOf((tempoFinal - tempoInicial) / 1000);
		return "Tempo: " + tempoString;
	}
}
