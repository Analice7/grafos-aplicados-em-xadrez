package xadrez;

import java.util.Map;
/**
 * Nó que representa uma posicao do tabuleiro e sua incidencia na partida
 */
public class NoPosicao {
	Map<String, Peca> posicao;
	int incidencia;
	public NoPosicao(Map<String, Peca> map){
		this.posicao=map;
		this.incidencia=1;
	}
	public Map<String, Peca> getPosicao() {
		return posicao;
	}
	public void incrementarIncidencia() {
		++incidencia;
	}
	public int getIncidencia() {
		return incidencia;
	}
}
