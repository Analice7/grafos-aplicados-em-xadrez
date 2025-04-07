package xadrez;
import java.util.*;

import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

public class Tabuleiro {
    private Grafo grafo;
    private Map<String, Peca> pecas;
    //contador de lances consecutivos sem captura ou movimento de peão
    private int contadorLances;

    public Tabuleiro() {
        this.grafo = new Grafo();
        this.pecas = new HashMap<>();
        this.contadorLances=0;
        inicializarTabuleiro();
    }

    /// Inicializa o tabuleiro com as peças em suas posições iniciais
    // e constrói o grafo de movimentos possíveis
    private void inicializarTabuleiro() {
        // Posições iniciais das peças
        String[] colunas = {"a", "b", "c", "d", "e", "f", "g", "h"};
        
        // Peças brancas
        pecas.put("a1", new Torre("branca"));
        pecas.put("b1", new Cavalo("branca"));
        pecas.put("c1", new Bispo("branca"));
        pecas.put("d1", new Rainha("branca"));
        pecas.put("e1", new Rei("branca"));
        pecas.put("f1", new Bispo("branca"));
        pecas.put("g1", new Cavalo("branca"));
        pecas.put("h1", new Torre("branca"));
        for (String col : colunas) {
            pecas.put(col + "2", new Peao("branca"));
        }
        
        // Peças pretas
        pecas.put("a8", new Torre("preta"));
        pecas.put("b8", new Cavalo("preta"));
        pecas.put("c8", new Bispo("preta"));
        pecas.put("d8", new Rainha("preta"));
        pecas.put("e8", new Rei("preta"));
        pecas.put("f8", new Bispo("preta"));
        pecas.put("g8", new Cavalo("preta"));
        pecas.put("h8", new Torre("preta"));
        for (String col : colunas) {
            pecas.put(col + "7", new Peao("preta"));
        }
        
        construirGrafoMovimentos();
    }
    
    // Constrói o grafo de movimentos possíveis para todas as peças no tabuleiro
    // Cada aresta representa um movimento válido que uma peça pode fazer
    public void construirGrafoMovimentos() {
        grafo = new Grafo();
        Map<String, Peca> copiaPecas = new HashMap<>(pecas);
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            
            String posicao = entry.getKey();
            Peca peca = entry.getValue();
            List<String> movimentos = peca.movimentosValidos(posicao, this);
            
            if (movimentos != null) {
                for (String movimento : movimentos) {
                    if (movimento != null) {
                        grafo.adicionarAresta(posicao, movimento, 1);
                    }
                }
            }
        }
    }
    
    // Move uma peça de uma posição de origem para uma posição de destino
    // Retorna true se o movimento foi válido e executado com sucesso
    // Atualiza o grafo de movimentos após a movimentação
    public boolean moverPeca(String origem, String destino) {
        if (pecas.containsKey(origem) && grafo.bfs(origem).contains(destino)) {
            Peca pecaMovida = pecas.remove(origem);
            if(getPeca(destino)==null && !(pecaMovida instanceof Peao)) {
                ++contadorLances;
            }else {
            	contadorLances=0;
            }
            pecas.put(destino, pecaMovida);
            construirGrafoMovimentos();
            return true;
        }
        return false;
    }

    // Simula um movimento sem alterar permanentemente o estado do tabuleiro
    // Retorna a peça que seria capturada neste movimento (ou null se não houver captura)
    // Útil para verificar consequências de movimentos sem efetivá-los
    public Peca simularMovimento(String origem, String destino) {
        Peca pecaMovida = pecas.remove(origem);
        Peca capturada = pecas.get(destino);
        if (capturada != null) pecas.remove(destino);
        pecas.put(destino, pecaMovida);
        return capturada;
    }

    // Desfaz uma simulação de movimento, restaurando o tabuleiro ao estado anterior
    // Recebe as posições de origem e destino e a peça que foi capturada na simulação
    public void desfazerSimulacao(String origem, String destino, Peca pecaCapturada) {
        Peca pecaMovida = pecas.remove(destino);
        pecas.put(origem, pecaMovida);
        if (pecaCapturada != null) pecas.put(destino, pecaCapturada);
    }

    public void promoverPeao(String posicao, Scanner scanner) {
		if(getPeca(posicao) instanceof Peao) {
			switch(posicao.charAt(1)) {
			case '1':
				getPecasOriginais().put(posicao, new Rainha("preta"));
				break;
			case '8':
				while(true) {
					System.out.println("insira o nome da peca para qual desejas promover o peão");
					String opcao = scanner.nextLine();
					switch(opcao.toLowerCase()){
						case "cavalo":
							getPecasOriginais().put(posicao, new Cavalo("branca"));
							return;
						case "bispo":
							getPecasOriginais().put(posicao, new Bispo("branca"));
							return;
						case "torre":
							getPecasOriginais().put(posicao, new Torre("branca"));
							return;
						case "rainha":
							getPecasOriginais().put(posicao, new Rainha("branca"));
							return;
						default:
							System.out.println("insira um nome valido: \n Rainha \n Cavalo \n Bispo \n Torre" );
					}
				}
			}
		}
		//indica que a peça já se moveu, caso o peão vire uma torre isso será importante para evitar roques inválidos
		getPeca(posicao).mover();	
	}

	public Map<String, Peca> getPecasOriginais() {
		return pecas;
	}
    
    // Retorna a peça na posição especificada
    public Peca getPeca(String posicao) {
        return pecas.get(posicao);
    }
    
    // Retorna uma cópia do mapa de peças
    public Map<String, Peca> getPecas() {
        return new HashMap<>(pecas);
    }


    public void rocar(String destino, String cor) {
    	if(destino.equals("c1") || destino.equals("c8")) {
    		fazerRoqueMaior(cor);
    	}
    	else{
    		fazerRoqueMenor(cor);
    	}
	}

    public void fazerRoqueMaior(String corJogador){
		Peca rei;
		Peca torreEsquerda;
		//remove-se as peças rei e torre da posição atual e realoca-se elas nas casas pós-roque
		if(corJogador.equals("branca")) { 
			pecas.remove("e1");
			pecas.remove("a1");
			pecas.put("c1", new Rei(corJogador));
	        pecas.put("d1", new Torre(corJogador));
	        //atualização do booleano "moveu"
	        rei = getPeca("c1");
	        torreEsquerda = getPeca("d1");
		}
		//a lógica para as peças pretas é análoga
		else{ 
			pecas.put("c8", new Rei(corJogador));
			pecas.remove("e8");
			pecas.remove("a8");
	        pecas.put("d8", new Torre(corJogador));
	        //atualização do booleano "moveu"
	        rei = getPeca("c8");
	        torreEsquerda = getPeca("d8");
		}        
		//indica que as peças se moveram(evitando futuros roques ilegais)
		rei.mover();
        torreEsquerda.mover();
	}

	public void fazerRoqueMenor(String corJogador) {
		Peca rei;
		Peca torreDireita;
		//remove o rei e a torre de suas posições as coloca nas posições pós-roque
		if(corJogador.equals("branca")){
			pecas.remove("e1");
			pecas.remove("h1");
			pecas.put("g1", new Rei(corJogador));
	        pecas.put("f1", new Torre(corJogador));
			rei = getPeca("g1");
			torreDireita = getPeca("f1");
		}
		//lógica análoga para as pretas
		else{
			pecas.remove("e8");
			pecas.remove("h8");
			pecas.put("g8", new Rei(corJogador));
	        pecas.put("f8", new Torre(corJogador));
			rei = getPeca("g8");
			torreDireita = getPeca("f8");
		}
		//indica que a torre e o rei se moveram (evitando futuros roques ilegais) 
        torreDireita.mover();
        rei.mover();
	}

	/**
	 * método que retorna o tanto de lances consecutivos sem captura ou movimentos de peão 
	 * 
	 */
	public int getContadorLances() {
		return contadorLances;
	}

    public Grafo getGrafo() {
        return grafo;
    }
    
}