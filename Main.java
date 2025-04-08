import java.util.*;

import jogadores.Humano;
import jogadores.Jogador;
import jogadores.Robo;
import pecas.Peao;
import pecas.Rei;
import xadrez.NoPosicao;
import xadrez.Peca;
import xadrez.Tabuleiro;

public class Main {
    private final Tabuleiro tabuleiro;
    private final Jogador jogadorBranco;
    private final Jogador jogadorPreto;
    private Jogador jogadorAtual;

    public Main() {
        this.tabuleiro = new Tabuleiro();
        this.jogadorBranco = new Humano("branca");
        this.jogadorPreto = new Robo("preta");
        this.jogadorAtual = jogadorBranco;
    }
    
    public void iniciarJogo() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                mostrarTabuleiro();
                
                String resultado = tabuleiro.verificarFimDeJogo(jogadorAtual.getCor());
                if (resultado != null) {
                    System.out.println(resultado);
                    break;
                }
                
                System.out.println("Vez do jogador " + jogadorAtual.getCor());
                String[] jogada = obterJogadaValida(scanner);
                
                if (tabuleiro.moverPeca(jogada[0], jogada[1])) {
                	if(tabuleiro.getPeca(jogada[1]) instanceof Peao) {
                		if(jogada[1].charAt(1)=='1' || jogada[1].charAt(1)=='8') {
                			tabuleiro.promoverPeao(jogada[1], scanner);
                		}
                	}
                	if(tabuleiro.getPeca(jogada[1]) instanceof Rei && (jogada[0].charAt(0)==jogada[1].charAt(0)+2 || 
                			jogada[0].charAt(0)==jogada[1].charAt(0)-2 )){
                		tabuleiro.rocar(jogada[1], jogadorAtual.getCor());
                	}	
                	tabuleiro.getLista().inserir(new NoPosicao(tabuleiro.getPecas()));
                    jogadorAtual = (jogadorAtual == jogadorBranco) ? jogadorPreto : jogadorBranco;
                }
            }
            mostrarTabuleiro();
        }
    }

    private String[] obterJogadaValida(Scanner scanner) {
        if (jogadorAtual instanceof Robo) {
            System.out.println("Robo esta pensando...");
            try {
                Thread.sleep(1000); // Pausa dramática
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            String[] jogadaRobo = jogadorAtual.obterJogada(tabuleiro, jogadorAtual.getCor(), scanner);
            
            if (jogadaRobo != null) {
                System.out.println("Robo jogou: " + jogadaRobo[0] + " " + jogadaRobo[1]);
                return jogadaRobo;
            }
            
            System.out.println("Robo não encontrou movimentos válidos!");
            return new String[]{"a1", "a1"}; // Movimento de fallback
        }
        
        // Lógica do jogador humano (permanece idêntica)
        return jogadorAtual.obterJogada(tabuleiro, jogadorAtual.getCor(), scanner);
    }

    /**
     * Verifica se o jogo terminou em alguma condição de fim de jogo.
     * @param tabuleiro estado atual do tabuleiro
     * @param cor cor do jogador atual
     * @return true se o jogo terminou, false caso contrário
     */

    
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