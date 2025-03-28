import java.util.*;

class Peao extends Peca {
    public Peao(String cor) {
        super(cor);
        this.valor = 1;
    }
    
    @Override
    public List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = new ArrayList<>();
        int[] pos = parsePosicao(posicao);
        int lin = pos[0];
        int col = pos[1];
        
        int direcao = cor.equals("branca") ? 1 : -1;
        
        // Movimento para frente
        String frente = formatPosicao(lin + direcao, col);
        if (tabuleiro.getPeca(frente) == null) {
            movimentos.add(frente);
            
            // Primeiro movimento (avanço duplo)
            if ((cor.equals("branca") && lin == 1) || (cor.equals("preta") && lin == 6)) {
                String frente2 = formatPosicao(lin + 2*direcao, col);
                if (tabuleiro.getPeca(frente2) == null) {
                    movimentos.add(frente2);
                }
            }
        }
        
        // Capturas diagonais
        for (int dc : new int[]{-1, 1}) {
            if (col + dc >= 0 && col + dc < 8) {
                String diagonal = formatPosicao(lin + direcao, col + dc);
                Peca peca = tabuleiro.getPeca(diagonal);
                if (peca != null && !peca.getCor().equals(cor)) {
                    movimentos.add(diagonal);
                }
            }
        }
        
        return movimentos;
    }
}