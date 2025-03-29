package pecas;
import java.util.*;

import xadrez.Peca;
import xadrez.Tabuleiro;

public class Torre extends Peca {
    public Torre(String cor) {
        super(cor);
        this.valor = 5;
    }
    
    @Override
    public List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = new ArrayList<>();
        int[] pos = parsePosicao(posicao);
        int lin = pos[0];
        int col = pos[1];
        
        // Movimentos nas 4 direções
        int[][] direcoes = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        for (int[] dir : direcoes) {
            for (int i = 1; i < 8; i++) {
                int novaLin = lin + i * dir[0];
                int novaCol = col + i * dir[1];
                
                if (novaLin >= 0 && novaLin < 8 && novaCol >= 0 && novaCol < 8) {
                    String novaPos = formatPosicao(novaLin, novaCol);
                    Peca peca = tabuleiro.getPeca(novaPos);
                    
                    if (peca == null) {
                        movimentos.add(novaPos);
                    } else {
                        if (!peca.getCor().equals(cor)) {
                            movimentos.add(novaPos);
                        }
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        
        return movimentos;
    }
}