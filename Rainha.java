import java.util.*;

public class Rainha extends Peca {
    public Rainha(String cor) {
        super(cor);
        this.valor = 9; // Valor da rainha no xadrez
    }

    @Override
    public List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = new ArrayList<>();
        int[] pos = parsePosicao(posicao);
        int lin = pos[0];
        int col = pos[1];
        
        // Combina movimentos da torre e do bispo
        // Movimentos na horizontal/vertical (como a torre)
        int[][] direcoesTorre = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        // Movimentos na diagonal (como o bispo)
        int[][] direcoesBispo = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        
        // Junta todas as direções
        int[][] todasDirecoes = new int[8][];
        System.arraycopy(direcoesTorre, 0, todasDirecoes, 0, 4);
        System.arraycopy(direcoesBispo, 0, todasDirecoes, 4, 4);
        
        for (int[] dir : todasDirecoes) {
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