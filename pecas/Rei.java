package pecas;
import java.util.*;

import regras.RegrasXadrez;
import xadrez.Peca;
import xadrez.Tabuleiro;

public class Rei extends Peca {
    public Rei(String cor) {
        super(cor);
        this.valor = 100; // Valor alto para o rei (peça mais importante)
    }

    @Override
    public List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = new ArrayList<>();
        int[] pos = parsePosicao(posicao);
        int lin = pos[0];
        int col = pos[1];
        
        // Todos os movimentos possíveis do rei (1 casa em qualquer direção)
        int[][] direcoes = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},  // horizontal/vertical
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}  // diagonal
        };
        
        for (int[] dir : direcoes) {
            int novaLin = lin + dir[0];
            int novaCol = col + dir[1];
            
            if (novaLin >= 0 && novaLin < 8 && novaCol >= 0 && novaCol < 8) {
                String novaPos = formatPosicao(novaLin, novaCol);
                Peca peca = tabuleiro.getPeca(novaPos);
                
                if (peca == null || !peca.getCor().equals(cor)) {
                    movimentos.add(novaPos);
                }
            }
        }
        
        movimentos.addAll(verificarRoque(tabuleiro));
        
        return movimentos;
    }

    private ArrayList<String> verificarRoque(Tabuleiro tabuleiro) {
		ArrayList<String> movimentos= new ArrayList<String>();
		//se o rei se moveu, o roque não poderá ser executado
		if(!moveu_se) {
			Peca pecaH8 = tabuleiro.getPeca("h8");
			Peca pecaA8 = tabuleiro.getPeca("a8");
			Peca pecaH1 = tabuleiro.getPeca("h1");
			Peca pecaA1 = tabuleiro.getPeca("a1");
			if(cor.equals("preta")) {
			//verifica se é possível fazer um roque menor
				if(tabuleiro.getPeca("f8")==null && tabuleiro.getPeca("g8")==null 
						&& pecaH8 instanceof Torre && !pecaH8.seMoveu()) {
					//adiciona peças temporarias para simular a passada do rei pelas 
					tabuleiro.getPecasOriginais().put("f8", new Rei("preta"));
					tabuleiro.getPecasOriginais().put("g8", new Rei("preta"));
					//se ao passar por alguma das casas ele toma cheque, o movimento de roque não é possível
					if(!RegrasXadrez.estaEmXeque("preta", tabuleiro)){
						movimentos.add("g8");
					}
					//termina a simulação removendo as peças temporariass
					tabuleiro.getPecasOriginais().remove("f8");
					tabuleiro.getPecasOriginais().remove("g8");
				}
				if(tabuleiro.getPeca("d8")==null && tabuleiro.getPeca("c8")==null && tabuleiro.getPeca("b8")==null 
						&& pecaA8 instanceof Torre && !pecaA8.seMoveu()) {
					//adiciona peças temporarias para simular a passada do rei pelas 
					tabuleiro.getPecasOriginais().put("d8", new Rei("preta"));
					tabuleiro.getPecasOriginais().put("c8", new Rei("preta"));
					tabuleiro.getPecasOriginais().put("b8", new Rei("preta"));
					//se ao passar por alguma das casas ele toma cheque, o movimento de roque não é possível
					if(!RegrasXadrez.estaEmXeque("preta", tabuleiro)){
						movimentos.add("c8");
					}
					//termina a simulação removendo as peças temporariass
					tabuleiro.getPecasOriginais().remove("d8");
					tabuleiro.getPecasOriginais().remove("c8");
					tabuleiro.getPecasOriginais().remove("b8");
				}
			}
			else{
			//lógica análoga a das peças pretas
				if(tabuleiro.getPeca("f1")==null && tabuleiro.getPeca("g1")==null 
						&& pecaH1 instanceof Torre && !pecaH1.seMoveu()) {
					
					tabuleiro.getPecasOriginais().put("f1", new Rei("branca"));
					tabuleiro.getPecasOriginais().put("g1", new Rei("branca"));
					if(!RegrasXadrez.estaEmXeque("branca", tabuleiro)){
							movimentos.add("g1");
					}
					tabuleiro.getPecasOriginais().remove("f1");
					tabuleiro.getPecasOriginais().remove("g1");
				}
				if(tabuleiro.getPeca("d1")==null && tabuleiro.getPeca("c1")==null && tabuleiro.getPeca("b1")==null 
						&& pecaA1 instanceof Torre && !pecaA1.seMoveu()) {
					tabuleiro.getPecasOriginais().put("d1", new Rei("branca"));
					tabuleiro.getPecasOriginais().put("c1", new Rei("branca"));
					tabuleiro.getPecasOriginais().put("b1", new Rei("branca"));
					if(!RegrasXadrez.estaEmXeque("branca", tabuleiro)){
							movimentos.add("c1");
					}
					tabuleiro.getPecasOriginais().remove("d1");
					tabuleiro.getPecasOriginais().remove("c1");
					tabuleiro.getPecasOriginais().remove("b1");
				}
			}
		}
		return movimentos;
	}
}