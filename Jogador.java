import java.util.Scanner;

public class Jogador{
    private String cor;
    private boolean ehIA;
    private IA ia;
    
    public Jogador(String cor, boolean ehIA) {
        this.cor = cor;
        this.ehIA = ehIA;
        if (ehIA) {
            this.ia = new IA();
        } else {
            this.ia = null;
        }
    }
    
    public String[] fazerJogada(Tabuleiro tabuleiro) {
        if (ehIA) {
            return ia.melhorJogada(tabuleiro, cor);
        } else {
            // Lógica para jogador humano
            System.out.println("Digite a posição de origem (ex: a2):");
            String origem = System.console().readLine();
            System.out.println("Digite a posição de destino (ex: a4):");
            String destino = System.console().readLine();
            return new String[]{origem, destino};
        }
    }

    public boolean ehIA() {
        return ehIA;
    }

    public String getCor() {
        return cor;
    }

    public String[] obterJogadaHumana(Scanner scanner) {
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

    public IA getIA() {
        return ia;
    }

}