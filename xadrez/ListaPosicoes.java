package xadrez;

import java.util.ArrayList;
/**
  * Lista feita para armazenar cada posição de xadrez apresentada no jogo e a sua frequência até então.
  * Usada para verificar se houve empate por repetição tripla de posição.
  */
public class ListaPosicoes {
	private ArrayList<NoPosicao> posicoes;
	
	public ListaPosicoes(){
		this.posicoes= new ArrayList<NoPosicao>();
	}
	public void inserir(NoPosicao novoNo){
		//percorre-se a lista, se acha-se uma posição igual adiciona-se 1 a sua incidência
		for(NoPosicao no: posicoes) {
			if(no.getPosicao().equals(novoNo.getPosicao())){
				no.incrementarIncidencia();
				return;
			}
		}
		//se não acha-se posição igual, adiciona-se a nova posição a lista
		posicoes.add(novoNo);
	}
	public ArrayList<NoPosicao> getPosicoes() {
		return posicoes;
	}

}
