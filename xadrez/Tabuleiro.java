package xadrez;
import java.util.*;

import pecas.Bispo;
import pecas.Cavalo;
import pecas.Peao;
import pecas.Rainha;
import pecas.Rei;
import pecas.Torre;

public class Tabuleiro {
    private Grafo grafo;
    private Map<String, Peca> pecas;
    
    public Tabuleiro() {
        this.grafo = new Grafo();
        this.pecas = new HashMap<>();
        inicializarTabuleiro();
    }
    

    /// Inicializa o tabuleiro com as peças em suas posições iniciais
    // e constrói o grafo de movimentos possíveis
    private void inicializarTabuleiro() {
        // Posições iniciais das peças
        String[] colunas = {"a", "b", "c", "d", "e", "f", "g", "h"};
        
        // Peças brancas
        pecas.put("a1", new Torre("branca"));
        pecas.put("b1", new Cavalo("branca"));
        pecas.put("c1", new Bispo("branca"));
        pecas.put("d1", new Rainha("branca"));
        pecas.put("e1", new Rei("branca"));
        pecas.put("f1", new Bispo("branca"));
        pecas.put("g1", new Cavalo("branca"));
        pecas.put("h1", new Torre("branca"));
        for (String col : colunas) {
            pecas.put(col + "2", new Peao("branca"));
        }
        
        // Peças pretas
        pecas.put("a8", new Torre("preta"));
        pecas.put("b8", new Cavalo("preta"));
        pecas.put("c8", new Bispo("preta"));
        pecas.put("d8", new Rainha("preta"));
        pecas.put("e8", new Rei("preta"));
        pecas.put("f8", new Bispo("preta"));
        pecas.put("g8", new Cavalo("preta"));
        pecas.put("h8", new Torre("preta"));
        for (String col : colunas) {
            pecas.put(col + "7", new Peao("preta"));
        }
        
        construirGrafoMovimentos();
    }
    
    // Constrói o grafo de movimentos possíveis para todas as peças no tabuleiro
    // Cada aresta representa um movimento válido que uma peça pode fazer
    public void construirGrafoMovimentos() {
        grafo = new Grafo();
        Map<String, Peca> copiaPecas = new HashMap<>(pecas);
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            
            String posicao = entry.getKey();
            Peca peca = entry.getValue();
            List<String> movimentos = peca.movimentosValidos(posicao, this);
            
            if (movimentos != null) {
                for (String movimento : movimentos) {
                    if (movimento != null) {
                        grafo.adicionarAresta(posicao, movimento, 1);
                    }
                }
            }
        }
    }
    
    // Move uma peça de uma posição de origem para uma posição de destino
    // Retorna true se o movimento foi válido e executado com sucesso
    // Atualiza o grafo de movimentos após a movimentação
    public boolean moverPeca(String origem, String destino) {
        if (pecas.containsKey(origem) && grafo.bfs(origem).contains(destino)) {
            Peca pecaMovida = pecas.remove(origem);
            pecas.put(destino, pecaMovida);
            construirGrafoMovimentos();
            return true;
        }
        return false;
    }

    // Simula um movimento sem alterar permanentemente o estado do tabuleiro
    // Retorna a peça que seria capturada neste movimento (ou null se não houver captura)
    // Útil para verificar consequências de movimentos sem efetivá-los
    public Peca simularMovimento(String origem, String destino) {
        Peca pecaMovida = pecas.remove(origem);
        Peca capturada = pecas.get(destino);
        if (capturada != null) pecas.remove(destino);
        pecas.put(destino, pecaMovida);
        return capturada;
    }
    
    // Desfaz uma simulação de movimento, restaurando o tabuleiro ao estado anterior
    // Recebe as posições de origem e destino e a peça que foi capturada na simulação
    public void desfazerSimulacao(String origem, String destino, Peca pecaCapturada) {
        Peca pecaMovida = pecas.remove(destino);
        pecas.put(origem, pecaMovida);
        if (pecaCapturada != null) pecas.put(destino, pecaCapturada);
    }
    

    
    // Retorna a peça na posição especificada
    public Peca getPeca(String posicao) {
        return pecas.get(posicao);
    }
    
    // Retorna uma cópia do mapa de peças
    public Map<String, Peca> getPecas() {
        return new HashMap<>(pecas);
    }
}