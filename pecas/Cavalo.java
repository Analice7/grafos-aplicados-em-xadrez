package pecas;
import java.util.*;

import xadrez.Peca;
import xadrez.Tabuleiro;

public class Cavalo extends Peca {
    public Cavalo(String cor) {
        super(cor);
        this.valor = 3; 
    }

    @Override
    public List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = new ArrayList<>();
        int[] pos = parsePosicao(posicao);
        int lin = pos[0];
        int col = pos[1];
        
        int[][] offsets = {
            // Movimentos em L
            {2,1}, {2,-1}, {-2,1}, {-2,-1},
            {1,2}, {1,-2}, {-1,2}, {-1,-2}
        };
        
        for (int[] offset : offsets) {
            int novaLin = lin + offset[0];
            int novaCol = col + offset[1];
            
            if (novaLin >= 0 && novaLin < 8 && novaCol >= 0 && novaCol < 8) {
                String novaPos = formatPosicao(novaLin, novaCol);
                Peca peca = tabuleiro.getPeca(novaPos);
                if (peca == null || !peca.getCor().equals(this.cor)) {
                    movimentos.add(novaPos);
                }
            }
        }
        return movimentos;
    }
}