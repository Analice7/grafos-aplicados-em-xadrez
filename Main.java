import java.util.*;

public class Main {
    private final Tabuleiro tabuleiro;
    private final Jogador jogadorBranco;
    private final Jogador jogadorPreto;
    private Jogador jogadorAtual;
    
    public Main() {
        this.tabuleiro = new Tabuleiro();
        this.jogadorBranco = new Jogador("branca", false);
        this.jogadorPreto = new Jogador("preta", true);
        this.jogadorAtual = jogadorBranco;
    }
    
    public void iniciarJogo() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                mostrarTabuleiro();
                
                String resultado = verificarFimDeJogo();
                if (resultado != null) {
                    System.out.println(resultado);
                    break;
                }
                
                System.out.println("Vez do jogador " + jogadorAtual.getCor());
                String[] jogada = obterJogadaValida(scanner);
                
                if (tabuleiro.moverPeca(jogada[0], jogada[1])) {
                    jogadorAtual = (jogadorAtual == jogadorBranco) ? jogadorPreto : jogadorBranco;
                }
            }
            mostrarTabuleiro();
        }
    }

    private String verificarFimDeJogo() {
        // Verifica para ambos os jogadores
        if (tabuleiro.isCheckMate("branca")) {
            return "Xeque-mate! As pretas venceram!";
        }
        if (tabuleiro.isCheckMate("preta")) {
            return "Xeque-mate! As brancas venceram!";
        }
        if (tabuleiro.isStalemate(jogadorAtual.getCor())) {
            return "Afogamento! O jogo terminou em empate.";
        }
        if (tabuleiro.isMaterialInsufficient()) {
            return "Material insuficiente para dar mate. Empate!";
        }
        return null;
    }

    private String[] obterJogadaValida(Scanner scanner) {
        if (jogadorAtual.ehIA()) {
            System.out.println("IA está pensando...");
            try {
                Thread.sleep(1000); // Pausa dramática
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            String[] jogadaIA = jogadorAtual.getIA().melhorJogada(tabuleiro, jogadorAtual.getCor());
            
            if (jogadaIA != null) {
                System.out.println("IA jogou: " + jogadaIA[0] + " " + jogadaIA[1]);
                return jogadaIA;
            }
            
            System.out.println("IA não encontrou movimentos válidos!");
            return new String[]{"a1", "a1"}; // Movimento de fallback
        }
        
        // Lógica do jogador humano (permanece idêntica)
        return jogadorAtual.obterJogadaHumana(scanner);
    }
    
    private void mostrarTabuleiro() {
        System.out.println("\n   a b c d e f g h");
        for (int lin = 7; lin >= 0; lin--) {
            System.out.print((lin + 1) + " ");
            for (int col = 0; col < 8; col++) {
                String posicao = String.format("%c%d", 'a' + col, lin + 1);
                Peca peca = tabuleiro.getPeca(posicao);
                System.out.print("|" + (peca != null ? obterSimbolo(peca) : " "));
            }
            System.out.println("| " + (lin + 1));
        }
        System.out.println("   a b c d e f g h");
    }
    
    private String obterSimbolo(Peca peca) {
        Map<String, Map<String, String>> simbolos = Map.of(
            "Peao", Map.of("branca", "♙", "preta", "♟"),
            "Torre", Map.of("branca", "♖", "preta", "♜"),
            "Cavalo", Map.of("branca", "♘", "preta", "♞"),
            "Bispo", Map.of("branca", "♗", "preta", "♝"),
            "Rainha", Map.of("branca", "♕", "preta", "♛"),
            "Rei", Map.of("branca", "♔", "preta", "♚")
        );
        return simbolos.getOrDefault(peca.getClass().getSimpleName(), Map.of())
                     .getOrDefault(peca.getCor(), "?");
    }
    
    public static void main(String[] args) {
        new Main().iniciarJogo();
    }
}