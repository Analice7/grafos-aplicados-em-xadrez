package xadrez;

import java.util.ArrayList;

public class ListaPosicoes {
	private ArrayList<NoPosicao> lista;
	public ListaPosicoes(){
		this.lista= new ArrayList<NoPosicao>();
	}
	public void inserir(NoPosicao novoNo){
		//percorre-se a lista, se acha-se uma posição igual adiciona-se 1 a sua incidência
		for(NoPosicao no: lista) {
			if(no.getPosicao().equals(novoNo.getPosicao())){
				no.incrementarIncidencia();
				return;
			}
		}
		//se não acha-se posição igual, adiciona-se a nova posição a lista
		lista.add(novoNo);
	}
	public boolean empatePorRepeticao() {
		for(NoPosicao no: lista) {
			if (no.getIncidencia()==3) {
				return true;
			}
		}
		return false;
	}
}
