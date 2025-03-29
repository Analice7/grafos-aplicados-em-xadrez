package xadrez;
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

}