import java.util.Random;

public class Dado {
    private int caras;
    private int valor;
    private Random rnd;

    public Dado(){
        caras = 6;
        rnd = new Random(caras);
        valor = valor;
    }

    public int tirarDado(){
        valor = rnd.nextInt(caras) + 1;
        return valor;
    }

    public int getValor(){
        return valor;
    }

    public void setValor(int valor){
        this.valor = valor;
    }

    @Override
    public String toString(){
        return "Valor dado" + valor;
    }
}
