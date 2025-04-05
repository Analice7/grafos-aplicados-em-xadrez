package jogadores;
import java.util.*;

import xadrez.Peca;
import xadrez.Tabuleiro;
import xadrez.Grafo;
import regras.RegrasXadrez;

public class Robo extends Jogador{

    public Robo(String cor) {
        super(cor);
    }

    // Constante que define a profundidade máxima de busca no algoritmo Minimax
    private static final int PROFUNDIDADE_MAXIMA = 3;
    
    /**
     * Método principal que decide a melhor jogada para o robô.
     * Implementa a lógica de decisão usando o algoritmo Minimax e utiliza o grafo de movimentos.
     * @param tabuleiro o estado atual do tabuleiro
     * @param cor a cor das peças do robô
     * @param scanner scanner para entrada (não utilizado pelo robô)
     * @return array com as posições de origem e destino da melhor jogada encontrada
     */
    @Override
    public String[] obterJogada(Tabuleiro tabuleiro, String cor, Scanner scanner) {
        int melhorValor = Integer.MIN_VALUE;
        String[] melhorJogada = null;
        
        // Obter o grafo de movimentos do tabuleiro
        Grafo grafo = tabuleiro.getGrafo();
        
        Map<String, Peca> pecasCopia = new HashMap<>(tabuleiro.getPecas());
        
        for (Map.Entry<String, Peca> entry : pecasCopia.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                String origem = entry.getKey();
                
                // Usar o BFS do grafo para obter os movimentos possíveis a partir da origem
                Set<String> destinosPossiveis = grafo.bfs(origem);
                
                destinosPossiveis.remove(origem);
                
                for (String destino : destinosPossiveis) {
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
    
    /**
     * Implementação do algoritmo Minimax com poda Alpha-Beta.
     * Avalia recursivamente todas as possíveis jogadas até a profundidade máxima.
     * Utiliza o grafo de movimentos do tabuleiro.
     * @param tabuleiro estado atual do tabuleiro
     * @param profundidade profundidade atual da busca
     * @param alpha valor alpha para poda (limite inferior)
     * @param beta valor beta para poda (limite superior)
     * @param maximizando indica se está maximizando (true) ou minimizando (false) o valor
     * @param cor cor das peças do robô
     * @return valor de avaliação da posição atual
     */
    private int minimax(Tabuleiro tabuleiro, int profundidade, int alpha, int beta, boolean maximizando, String cor) {
   
        Map<String, Peca> pecasCopia = new HashMap<>(tabuleiro.getPecas());
        
        if (profundidade == 0 || jogoAcabou(tabuleiro, cor)) {
            return avaliarTabuleiro(tabuleiro, cor);
        }
        
        int valor = maximizando ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        
        // Obter o grafo de movimentos do tabuleiro
        Grafo grafo = tabuleiro.getGrafo();
        
        for (Map.Entry<String, Peca> entry : pecasCopia.entrySet()) {
            if (entry.getValue().getCor().equals(maximizando ? cor : corOposta(cor))) {
                String origem = entry.getKey();
                
                // Usar o BFS do grafo para obter os movimentos possíveis a partir da origem
                Set<String> destinosPossiveis = grafo.bfs(origem);
                // Remove a própria posição de origem dos destinos possíveis
                destinosPossiveis.remove(origem);
                
                for (String destino : destinosPossiveis) {
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
    
    private String corOposta(String cor) {
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
        if (RegrasXadrez.estaEmXeque(cor, tabuleiro)) {
            return true;
        }
        
        // 2. Verifica afogamento (stalemate)
        if (RegrasXadrez.afogamento(cor, tabuleiro)) {
            return true;
        }
        
        // 3. Verifica material insuficiente
        if (RegrasXadrez.materialInsuficiente(tabuleiro)) {
            return true;
        }
        
        return false;
    }
}