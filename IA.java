import java.util.*;

public class IA {
    private static final int PROFUNDIDADE_MAXIMA = 3;
    
    public String[] melhorJogada(Tabuleiro tabuleiro, String cor) {
        int melhorValor = Integer.MIN_VALUE;
        String[] melhorJogada = null;
        
        // 1. Crie uma cópia defensiva das peças
        Map<String, Peca> pecasCopia = new HashMap<>(tabuleiro.getPecas());
        
        for (Map.Entry<String, Peca> entry : pecasCopia.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                String origem = entry.getKey();
                Peca peca = entry.getValue();
                
                // 2. Crie uma cópia dos movimentos válidos
                List<String> movimentos = new ArrayList<>(peca.movimentosValidos(origem, tabuleiro));
                
                for (String destino : movimentos) {
                    // 3. Use os métodos de simulação do Tabuleiro
                    Peca capturada = tabuleiro.simularMovimento(origem, destino);
                    int valor = minimax(tabuleiro, PROFUNDIDADE_MAXIMA - 1, 
                                      Integer.MIN_VALUE, Integer.MAX_VALUE, false, cor);
                    tabuleiro.desfazerSimulacao(origem, destino, capturada);
                    
                    if (valor > melhorValor) {
                        melhorValor = valor;
                        melhorJogada = new String[]{origem, destino};
                    }
                }
            }
        }
        return melhorJogada;
    }
    
    private int minimax(Tabuleiro tabuleiro, int profundidade, int alpha, int beta, 
                       boolean maximizando, String cor) {
        // 4. Crie uma cópia defensiva para o minimax
        Map<String, Peca> pecasCopia = new HashMap<>(tabuleiro.getPecas());
        
        if (profundidade == 0 || jogoAcabou(tabuleiro, cor)) {
            return avaliarTabuleiro(tabuleiro, cor);
        }
        
        int valor = maximizando ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        for (Map.Entry<String, Peca> entry : pecasCopia.entrySet()) {
            if (entry.getValue().getCor().equals(maximizando ? cor : outraCor(cor))) {
                String origem = entry.getKey();
                Peca peca = entry.getValue();
                
                List<String> movimentos = new ArrayList<>(peca.movimentosValidos(origem, tabuleiro));
                
                for (String destino : movimentos) {
                    Peca capturada = tabuleiro.simularMovimento(origem, destino);
                    int avaliacao = minimax(tabuleiro, profundidade-1, alpha, beta, !maximizando, cor);
                    tabuleiro.desfazerSimulacao(origem, destino, capturada);
                    
                    if (maximizando) {
                        valor = Math.max(valor, avaliacao);
                        alpha = Math.max(alpha, valor);
                    } else {
                        valor = Math.min(valor, avaliacao);
                        beta = Math.min(beta, valor);
                    }
                    
                    if (alpha >= beta) {
                        return valor; // Poda alpha-beta
                    }
                }
            }
        }
        return valor;
    }
    
    private String outraCor(String cor) {
        return cor.equals("branca") ? "preta" : "branca";
    }
    
    private int avaliarTabuleiro(Tabuleiro tabuleiro, String cor) {
        int valorTotal = 0;
        Map<String, Peca> pecas = tabuleiro.getPecas();
        
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            String posicao = entry.getKey();
            Peca peca = entry.getValue();
            
            // Valor base da peça
            int valorPeca = peca.getValor();
            
            // Bônus por posição no tabuleiro
            int[] pos = Peca.parsePosicao(posicao);
            double centro = (3.5 - Math.abs(3.5 - pos[0])) + (3.5 - Math.abs(3.5 - pos[1]));
            int valorPosicao = (int)(centro * 0.1);  // Peças no centro valem mais
            
            // Se for peça adversária, subtrai o valor
            if (!peca.getCor().equals(cor)) {
                valorTotal -= (valorPeca + valorPosicao);
            } else {
                valorTotal += (valorPeca + valorPosicao);
            }
        }
        
        return valorTotal;
    }
    
    private boolean jogoAcabou(Tabuleiro tabuleiro, String cor) {
        // 1. Verifica xeque-mate
        if (tabuleiro.isCheckMate(cor)) {
            return true;
        }
        
        // 2. Verifica afogamento (stalemate)
        if (isStalemate(tabuleiro, cor)) {
            return true;
        }
        
        // 3. Verifica material insuficiente
        if (isMaterialInsufficient(tabuleiro)) {
            return true;
        }
        
        // 4. Verifica repetição de posições (opcional)
        // if (isRepeticaoPosicoes()) {...}
        
        return false;
    }
    
    private boolean isStalemate(Tabuleiro tabuleiro, String cor) {
        // Afogamento ocorre quando o jogador não está em xeque mas não tem movimentos válidos
        if (!tabuleiro.estaEmXeque(cor)) {
            for (Map.Entry<String, Peca> entry : tabuleiro.getPecas().entrySet()) {
                if (entry.getValue().getCor().equals(cor)) {
                    if (!entry.getValue().movimentosValidos(entry.getKey(), tabuleiro).isEmpty()) {
                        return false; // Ainda tem movimentos válidos
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean isMaterialInsufficient(Tabuleiro tabuleiro) {
        Map<String, Peca> pecas = tabuleiro.getPecas();
        int contagemPecas = pecas.size();
        
        // Casos de material insuficiente:
        // 1. Apenas os dois reis
        if (contagemPecas == 2) return true;
        
        // 2. Rei + bispo vs Rei
        // 3. Rei + cavalo vs Rei
        if (contagemPecas == 3) {
            boolean apenasBispoOuCavalo = true;
            for (Peca peca : pecas.values()) {
                if (!(peca instanceof Rei) && !(peca instanceof Bispo) && !(peca instanceof Cavalo)) {
                    apenasBispoOuCavalo = false;
                    break;
                }
            }
            return apenasBispoOuCavalo;
        }
        
        // 4. Rei + bispo vs Rei + bispo (mesma cor do quadrado)
        if (contagemPecas == 4) {
            // Implementação mais complexa para verificar bispos em quadrados da mesma cor
            // Pode ser implementada se necessário
        }
        
        return false;
    }
}