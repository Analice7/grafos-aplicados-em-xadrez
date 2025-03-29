package jogadores;
import java.util.Scanner;

import xadrez.Tabuleiro;

public class Humano extends Jogador {

    public Humano(String cor) {
        super(cor);
    }

    @Override
    public String[] obterJogada(Tabuleiro tabuleiro, String cor, Scanner scanner) {
        while (true) {
            try {
                System.out.print("Digite sua jogada (ex: 'a2 a4'): ");
                String input = scanner.nextLine().trim().toLowerCase();
                
                if (input.equals("sair")) {
                    System.out.println("Jogo encerrado.");
                    System.exit(0);
                }
                
                String[] partes = input.split("\\s+");
                if (partes.length == 2 && partes[0].matches("[a-h][1-8]") && partes[1].matches("[a-h][1-8]")) {
                    return partes;
                }
                System.out.println("Formato inválido. Use 'letranúmero letranúmero' (ex: a2 a4)");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
