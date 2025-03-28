import java.util.*;

public class Grafo {
    private Map<String, List<String>> adjacencia;
    private Map<String, Integer> pesos;

    public Grafo() {
        this.adjacencia = new HashMap<>();
        this.pesos = new HashMap<>();
    }

    public void adicionarAresta(String u, String v, int peso) {
        adjacencia.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        pesos.put(u + "-" + v, peso);
    }

    public Set<String> bfs(String inicio) {
        Set<String> visitados = new LinkedHashSet<>();
        Queue<String> fila = new LinkedList<>();
        fila.add(inicio);
        visitados.add(inicio);

        while (!fila.isEmpty()) {
            String vertice = fila.poll();
            for (String vizinho : adjacencia.getOrDefault(vertice, new ArrayList<>())) {
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);
                }
            }
        }
        return visitados;
    }

    public List<String> aEstrela(String inicio, String objetivo, Heuristica heuristica) {
        PriorityQueue<Node> filaPrioridade = new PriorityQueue<>();
        Map<String, Integer> custoAtual = new HashMap<>();
        Map<String, String> veioDe = new HashMap<>();
        Set<String> explorados = new HashSet<>();

        filaPrioridade.add(new Node(inicio, 0));
        custoAtual.put(inicio, 0);
        veioDe.put(inicio, null);

        while (!filaPrioridade.isEmpty()) {
            Node atual = filaPrioridade.poll();
            
            if (atual.vertice.equals(objetivo)) {
                break;
            }
            
            explorados.add(atual.vertice);
            
            for (String vizinho : adjacencia.getOrDefault(atual.vertice, new ArrayList<>())) {
                int novoCusto = custoAtual.get(atual.vertice) + pesos.getOrDefault(atual.vertice + "-" + vizinho, 1);
                
                if (!explorados.contains(vizinho) && (!custoAtual.containsKey(vizinho) || novoCusto < custoAtual.get(vizinho))) {
                    custoAtual.put(vizinho, novoCusto);
                    int prioridade = novoCusto + heuristica.calcular(vizinho, objetivo);
                    filaPrioridade.add(new Node(vizinho, prioridade));
                    veioDe.put(vizinho, atual.vertice);
                }
            }
        }

        // Reconstruir caminho
        List<String> caminho = new ArrayList<>();
        String atual = objetivo;
        while (atual != null) {
            caminho.add(atual);
            atual = veioDe.get(atual);
        }
        Collections.reverse(caminho);
        return caminho;
    }

    private class Node implements Comparable<Node> {
        String vertice;
        int prioridade;

        public Node(String vertice, int prioridade) {
            this.vertice = vertice;
            this.prioridade = prioridade;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.prioridade, other.prioridade);
        }
    }
}

interface Heuristica {
    int calcular(String atual, String objetivo);
}