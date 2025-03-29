package pecas;
import java.util.*;

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
        
        // Adicionar roque (opcional - implementação mais avançada)
        // movimentos.addAll(verificarRoque(posicao, tabuleiro));
        
        return movimentos;
    }
}