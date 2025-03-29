package regras;
import xadrez.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pecas.Bispo;
import pecas.Cavalo;
import pecas.Rei;

public class RegrasXadrez {

    private RegrasXadrez() {}

    // Verifica se o rei da cor especificada está em xeque-mate
    // Retorna true se o rei está em xeque e não há movimentos legais que possam tirá-lo do xeque
    public static boolean estaEmXequeMate(String cor, Tabuleiro tabuleiro) {
        Map<String, Peca> pecas = tabuleiro.getPecas(); 
        // Cria uma cópia defensiva do mapa de peças
        Map<String, Peca> copiaPecas = new HashMap<>(pecas);
        
        if (!estaEmXeque(cor, tabuleiro)) return false;
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                // Cria cópia da lista de movimentos válidos
                List<String> movimentos = new ArrayList<>(
                    entry.getValue().movimentosValidos(entry.getKey(), tabuleiro)
                );
                if (!movimentos.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Verifica se o rei da cor especificada está em xeque
    // Retorna true se alguma peça adversária está atacando a posição do rei
    public static boolean estaEmXeque(String cor, Tabuleiro tabuleiro) {
        Map<String, Peca> pecas = tabuleiro.getPecas(); 
        String posicaoRei = encontrarPosicaoRei(cor, tabuleiro);
        if (posicaoRei == null) return false;
        
        // Cria cópia defensiva do mapa de peças
        
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            Peca peca = entry.getValue();
            if (!peca.getCor().equals(cor)) {
                // Cria cópia da lista de ameaças
                List<String> ameacas = new ArrayList<>(
                    peca.movimentosBasicos(entry.getKey(), tabuleiro)
                );
                if (ameacas.contains(posicaoRei)) {
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
}
