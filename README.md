# Grafos Aplicados em Xadrez

## ♟️ Visão Geral do Projeto

Este projeto implementa um jogo de xadrez completo utilizando **teoria dos grafos** como estrutura fundamental para representar e analisar o tabuleiro e os movimentos das peças. O sistema modela:

- O tabuleiro como um **grafo direcionado**
- Movimentos possíveis como **arestas** entre posições
- Buscas em grafos para **validação de movimentos** e **IA**

## 🌐 Representação em Grafos

### Estrutura do Grafo

```java
public class Grafo {
    private Map<String, List<Aresta>> adjacencias = new HashMap<>();
    
    public void adicionarAresta(String origem, String destino, int peso) {
        // Implementação da conexão entre vértices
    }
    
    public List<String> bfs(String origem) {
        // Busca em largura para encontrar movimentos alcançáveis
    }
}
```

**Vértices**: Posições do tabuleiro (ex: "e4", "a1")  
**Arestas**: Movimentos válidos para cada peça  
**Pesos**: Custo do movimento (normalmente 1)

### Por que Usar Grafos?

1. **Eficiência**:
   - Busca rápida de movimentos válidos (O(1) para movimentos diretos)
   - Algoritmos otimizados para análise do tabuleiro

2. **Flexibilidade**:
   - Modela naturalmente a conectividade entre posições
   - Fácil adaptação para diferentes regras ou peças

3. **Análise Completa**:
   - Permite verificar alcançabilidade entre quaisquer posições
   - Suporta algoritmos complexos como Minimax para IA

## 🔍 Algoritmos Implementados

### 1. Busca em Largura (BFS)

```java
public List<String> bfs(String origem) {
    List<String> alcancaveis = new ArrayList<>();
    Queue<String> fila = new LinkedList<>();
    Set<String> visitados = new HashSet<>();
    
    fila.add(origem);
    visitados.add(origem);
    
    while (!fila.isEmpty()) {
        String atual = fila.poll();
        alcancaveis.add(atual);
        
        for (Aresta aresta : adjacencias.get(atual)) {
            if (!visitados.contains(aresta.destino)) {
                visitados.add(aresta.destino);
                fila.add(aresta.destino);
            }
        }
    }
    return alcancaveis;
}
```

**Uso no Xadrez**:
- Valida se um destino é alcançável a partir de uma origem
- Encontra todos os movimentos possíveis para uma peça
- Base para verificação de xeque e xeque-mate

### 2. Integração com as Regras

```java
public boolean moverPeca(String origem, String destino) {
    if (pecas.containsKey(origem) && grafo.bfs(origem).contains(destino)) {
        // Movimento válido segundo o grafo
        Peca pecaMovida = pecas.remove(origem);
        pecas.put(destino, pecaMovida);
        construirGrafoMovimentos(); // Reconstroi o grafo
        return true;
    }
    return false;
}
```

## 📊 Vantagens da Abordagem com Grafos

| Vantagem | Aplicação no Xadrez |
|----------|---------------------|
| **Modelagem Natural** | Cada casa do tabuleiro é um vértice, cada movimento uma aresta |
| **Busca Eficiente** | Encontra caminhos mais curtos para peças como rainha e bispo |
| **Análise de Conectividade** | Verifica se o rei pode escapar de xeque |
| **Extensibilidade** | Fácil adição de novas regras ou peças especiais |

## 🧠 IA Baseada em Grafos

O robô utiliza o grafo para:

1. **Gerar Movimentos Válidos**:  
   ```peca.movimentosValidos()``` retorna vértices adjacentes

2. **Avaliar Posições**:  
   Analisa a estrutura do grafo para calcular vantagens posicionais

3. **Tomada de Decisão**:  
   Usa o grafo como base para o algoritmo Minimax com poda Alpha-Beta

## ⚙️ Funcionamento do Sistema

1. **Inicialização**:
   - Cria vértices para todas as posições do tabuleiro
   - Conecta arestas baseadas nos movimentos de cada peça

2. **Durante o Jogo**:
   - Atualiza o grafo após cada movimento
   - Verifica condições de vitória/derrota usando travessias no grafo

3. **Para IA**:
   - Simula movimentos criando subgrafos temporários
   - Avalia milhares de posições rapidamente usando busca em grafos

## 📈 Exemplo Prático: Movimento do Cavalo

```java
// No grafo, o cavalo em "b1" teria arestas para:
["a3", "c3", "d2"]
```
**Representação Visual**:
```
b1 ── a3
 │
 └── c3
 │
 └── d2
```

## 🚀 Como Executar

1. Compile o projeto:
```bash
javac -d . xadrez/*.java pecas/*.java regras/*.java jogadores/*.java Main.java
```

2. Execute:
```bash
java Main
```

Esta abordagem com grafos transforma o xadrez em um playground para algoritmos clássicos de teoria dos grafos, oferecendo tanto eficiência quanto clareza conceitual.