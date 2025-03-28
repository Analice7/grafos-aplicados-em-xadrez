import java.util.*;

public abstract class Peca {
    protected String cor;
    protected int valor;
    
    public Peca(String cor) {
        this.cor = cor;
        this.valor = 0;
    }
    
    public abstract List<String> movimentosBasicos(String posicao, Tabuleiro tabuleiro);
    
    public List<String> movimentosValidos(String posicao, Tabuleiro tabuleiro) {
        List<String> movimentos = movimentosBasicos(posicao, tabuleiro);
        List<String> validos = new ArrayList<>();
        
        for (String destino : movimentos) {
            Peca capturada = tabuleiro.simularMovimento(posicao, destino);
            boolean emXeque = tabuleiro.estaEmXeque(this.getCor());
            tabuleiro.desfazerSimulacao(posicao, destino, capturada);
            
            if (!emXeque) {
                validos.add(destino);
            }
        }
        return validos;
    }
    
    public String getCor() {
        return cor;
    }
    
    public int getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + cor + ")";
    }
    
    protected static int[] parsePosicao(String posicao) {
        int col = posicao.charAt(0) - 'a';
        int lin = Character.getNumericValue(posicao.charAt(1)) - 1;
        return new int[]{lin, col};
    }
    
    protected static String formatPosicao(int lin, int col) {
        return String.format("%c%d", 'a' + col, lin + 1);
    }

    protected List<String> filtrarMovimentosValidos(String posicaoOrigem, 
            List<String> movimentos, Tabuleiro tabuleiro) {
        List<String> validos = new ArrayList<>();
        String cor = tabuleiro.getPeca(posicaoOrigem).getCor();
        
        for (String destino : movimentos) {
            Peca capturada = tabuleiro.simularMovimento(posicaoOrigem, destino);
            boolean emXeque = tabuleiro.estaEmXeque(cor);
            tabuleiro.desfazerSimulacao(posicaoOrigem, destino, capturada);
            
            if (!emXeque) {
                validos.add(destino);
            }
        }
        return validos;
    }
}