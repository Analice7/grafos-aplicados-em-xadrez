package regras;
import xadrez.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pecas.Bispo;
import pecas.Cavalo;
import pecas.Rei;

public class RegrasXadrez {

    private RegrasXadrez() {}

    // Verifica se o rei da cor especificada está em xeque-mate
    // Retorna true se o rei está em xeque e não há movimentos legais que possam tirá-lo do xeque
    public static boolean estaEmXequeMate(String cor, Tabuleiro tabuleiro) {
            if (!estaEmXeque(cor, tabuleiro)) return false;
            
            Map<String, Peca> copiaPecas = new HashMap<>(tabuleiro.getPecas());
            Grafo grafo = tabuleiro.getGrafo();
            
            for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
                if (entry.getValue().getCor().equals(cor)) {
                    String origem = entry.getKey();
                    
                    // Usa BFS para obter todos os movimentos válidos a partir da origem
                    Set<String> destinosPossiveis = grafo.bfs(origem);
                   
                    destinosPossiveis.remove(origem);
                    
                    // Se há ao menos um movimento possível para alguma peça, não é xeque-mate
                    if (!destinosPossiveis.isEmpty()) {
                        // Verificar se algum dos movimentos tira do xeque
                        for (String destino : destinosPossiveis) {
                            
                            Peca capturada = tabuleiro.simularMovimento(origem, destino);
                            
                            boolean aindaEmXeque = estaEmXeque(cor, tabuleiro);
                            
                            tabuleiro.desfazerSimulacao(origem, destino, capturada);
                            
                            if (!aindaEmXeque) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    
    // Verifica se o rei da cor especificada está em xeque
    // Retorna true se alguma peça adversária está atacando a posição do rei
    public static boolean estaEmXeque(String cor, Tabuleiro tabuleiro) {
        String posicaoRei = encontrarPosicaoRei(cor, tabuleiro);
        if (posicaoRei == null) return false;
        
        Map<String, Peca> copiaPecas = new HashMap<>(tabuleiro.getPecas());
        Grafo grafo = tabuleiro.getGrafo();
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            Peca peca = entry.getValue();
            if (!peca.getCor().equals(cor)) {
                String origem = entry.getKey();
                
                // Usa BFS para obter todos os movimentos válidos desta peça adversária
                Set<String> destinosPossiveis = grafo.bfs(origem);
                
                // Se o rei estiver entre os destinos possíveis, está em xeque
                if (destinosPossiveis.contains(posicaoRei)) {
                    return true;
                }
            }
        }
        return false;
    }
	
    /**
     * Verifica condição de afogamento para a cor especificada.
     * @param tabuleiro estado atual do tabuleiro
     * @param cor cor do jogador sendo verificado
     * @return true se a condição de afogamento foi atingida
     */
    public static boolean afogamento(String cor, Tabuleiro tabuleiro) {
        // Afogamento ocorre quando o jogador não está em xeque mas não tem movimentos válidos
        if (!estaEmXeque(cor, tabuleiro)) {
            for (Map.Entry<String, Peca> entry : tabuleiro.getPecas().entrySet()) {
                if (entry.getValue().getCor().equals(cor)) {
                    if (!entry.getValue().movimentosValidos(entry.getKey(), tabuleiro).isEmpty()) {
                        return false; 
                    }
                }
            }
            return true;
        }
        return false;
    }

    // Encontra a posição no tabuleiro do rei da cor especificada
    // Retorna a coordenada ou null se o rei não for encontrado
    private static String encontrarPosicaoRei(String cor, Tabuleiro tabuleiro) {
        Map<String, Peca> pecas = tabuleiro.getPecas(); 
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (entry.getValue() instanceof Rei && entry.getValue().getCor().equals(cor)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Verifica se há material insuficiente para dar xeque-mate no tabuleiro.
     * @param tabuleiro estado atual do tabuleiro
     * @return true se não há peças suficientes para dar xeque-mate
     */
    public static boolean materialInsuficiente(Tabuleiro tabuleiro) {
        Map<String, Peca> pecas = tabuleiro.getPecas(); 
        int totalPecas = pecas.size();
        if (totalPecas == 2) return true; // Apenas reis
        
        if (totalPecas == 3) {
            // Rei + bispo ou cavalo vs Rei
            for (Peca peca : pecas.values()) {
                if (!(peca instanceof Rei) && !(peca instanceof Bispo) && !(peca instanceof Cavalo)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean empateDos50Lances(Tabuleiro tabuleiro){
    	if(tabuleiro.getContadorLances()==50) {
    		return true;
    	}
    	return false;
    }
	public static boolean empatePorRepeticao(Tabuleiro tabuleiro) {
		for(NoPosicao no: tabuleiro.getLista().getPosicoes()) {
			if (no.getIncidencia()==3) {
				return true;
			}
		}
		return false;
	}
}
