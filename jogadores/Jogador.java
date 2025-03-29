package jogadores;
import java.util.Scanner;

import xadrez.Tabuleiro;

public abstract class Jogador{
    private String cor;
    
    public Jogador(String cor) {
        this.cor = cor;
    }

    public String getCor() {
        return cor;
    }

    public abstract String[] obterJogada(Tabuleiro tabuleiro, String cor, Scanner scanner);

}