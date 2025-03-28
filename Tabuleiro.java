import java.util.*;

public class Tabuleiro {
    private Grafo grafo;
    private Map<String, Peca> pecas;
    
    public Tabuleiro() {
        this.grafo = new Grafo();
        this.pecas = new HashMap<>();
        inicializarTabuleiro();
    }
    
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

    public boolean isCheckMate(String cor) {
        // Cria uma cópia defensiva do mapa de peças
        Map<String, Peca> copiaPecas = new HashMap<>(pecas);
        
        if (!estaEmXeque(cor)) return false;
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                // Cria cópia da lista de movimentos válidos
                List<String> movimentos = new ArrayList<>(
                    entry.getValue().movimentosValidos(entry.getKey(), this)
                );
                if (!movimentos.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean estaEmXeque(String cor) {
        String posicaoRei = encontrarPosicaoRei(cor);
        if (posicaoRei == null) return false;
        
        // Cria cópia defensiva do mapa de peças
        Map<String, Peca> copiaPecas = new HashMap<>(pecas);
        
        for (Map.Entry<String, Peca> entry : copiaPecas.entrySet()) {
            Peca peca = entry.getValue();
            if (!peca.getCor().equals(cor)) {
                // Cria cópia da lista de ameaças
                List<String> ameacas = new ArrayList<>(
                    peca.movimentosBasicos(entry.getKey(), this)
                );
                if (ameacas.contains(posicaoRei)) {
                    return true;
                }
            }
        }
        return false;
    }
    
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
    
    public boolean moverPeca(String origem, String destino) {
        if (pecas.containsKey(origem) && grafo.bfs(origem).contains(destino)) {
            Peca pecaMovida = pecas.remove(origem);
            pecas.put(destino, pecaMovida);
            construirGrafoMovimentos();
            return true;
        }
        return false;
    }
    
    public boolean testCheckMate(String cor, Tabuleiro tabuleiro) {
        // Primeiro verifica se o rei está em xeque
        if (!testCheck(cor)) {
            return false;
        }
        
        // Obtém todas as peças da cor especificada
        Map<String, Peca> pecas = tabuleiro.getPecas();
        List<String> pecasDoJogador = new ArrayList<>();
        
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                pecasDoJogador.add(entry.getKey());
            }
        }
        
        // Para cada peça do jogador
        for (String posicaoOrigem : pecasDoJogador) {
            Peca peca = pecas.get(posicaoOrigem);
            
            // Obtém todos os movimentos válidos da peça
            List<String> movimentos = peca.movimentosValidos(posicaoOrigem, tabuleiro);
            
            // Para cada movimento possível
            for (String posicaoDestino : movimentos) {
                // Simula o movimento
                Peca pecaMovida = pecas.remove(posicaoOrigem);
                Peca pecaCapturada = pecas.get(posicaoDestino);
                if (pecaCapturada != null) {
                    pecas.remove(posicaoDestino);
                }
                pecas.put(posicaoDestino, pecaMovida);
                
                // Verifica se após o movimento ainda está em xeque
                boolean aindaEmXeque = testCheck(cor);
                
                // Desfaz o movimento
                pecas.remove(posicaoDestino);
                pecas.put(posicaoOrigem, pecaMovida);
                if (pecaCapturada != null) {
                    pecas.put(posicaoDestino, pecaCapturada);
                }
                
                // Se encontrou um movimento que tira do xeque, não é checkmate
                if (!aindaEmXeque) {
                    return false;
                }
            }
        }
        
        // Se nenhum movimento tirar do xeque, é checkmate
        return true;
    }

    public boolean testCheck(String cor) {  // Remova o parâmetro Tabuleiro
        // Encontra a posição do rei
        String posicaoRei = null;
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (entry.getValue() instanceof Rei && entry.getValue().getCor().equals(cor)) {
                posicaoRei = entry.getKey();
                break;
            }
        }
        
        if (posicaoRei == null) {
            throw new RuntimeException("Não há rei " + cor + " no tabuleiro!");
        }
        
        // Verifica se alguma peça adversária pode atacar o rei
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (!entry.getValue().getCor().equals(cor)) {
                List<String> movimentos = entry.getValue().movimentosValidos(entry.getKey(), this);
                if (movimentos.contains(posicaoRei)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public Peca simularMovimento(String origem, String destino) {
        Peca pecaMovida = pecas.remove(origem);
        Peca capturada = pecas.get(destino);
        if (capturada != null) pecas.remove(destino);
        pecas.put(destino, pecaMovida);
        return capturada;
    }
    
    public void desfazerSimulacao(String origem, String destino, Peca pecaCapturada) {
        Peca pecaMovida = pecas.remove(destino);
        pecas.put(origem, pecaMovida);
        if (pecaCapturada != null) pecas.put(destino, pecaCapturada);
    }
    
    private String encontrarPosicaoRei(String cor) {
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (entry.getValue() instanceof Rei && entry.getValue().getCor().equals(cor)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Peca makeMove(String origem, String destino) {  // Remove o parâmetro Tabuleiro
        Map<String, Peca> pecas = this.getPecas();        // Usa o this para acessar o próprio tabuleiro
        Peca pecaMovida = pecas.remove(origem);
        Peca pecaCapturada = pecas.get(destino);
        
        if (pecaCapturada != null) {
            pecas.remove(destino);
        }
        
        pecas.put(destino, pecaMovida);
        this.construirGrafoMovimentos();  // Atualiza o grafo do próprio tabuleiro
        
        return pecaCapturada;
    }

    public boolean isStalemate(String cor) {
        if (estaEmXeque(cor)) return false;
        
        for (Map.Entry<String, Peca> entry : pecas.entrySet()) {
            if (entry.getValue().getCor().equals(cor)) {
                if (!entry.getValue().movimentosValidos(entry.getKey(), this).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMaterialInsufficient() {
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

    public void undoMove(String origem, String destino, Peca pecaCapturada) {  // Remove Tabuleiro
        Map<String, Peca> pecas = this.getPecas();
        Peca pecaMovida = pecas.remove(destino);
        
        pecas.put(origem, pecaMovida);
        if (pecaCapturada != null) {
            pecas.put(destino, pecaCapturada);
        }
        
        this.construirGrafoMovimentos();  // Atualiza o grafo do próprio tabuleiro
    }
    
    public Peca getPeca(String posicao) {
        return pecas.get(posicao);
    }
    
    public Map<String, Peca> getPecas() {
        return new HashMap<>(pecas);
    }
}