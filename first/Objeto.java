package andru.graphics.first;

public class Objeto {
    private final Fisica fisica;



    public Objeto(int x, int y){
        this.fisica = new Fisica(x, y);
    }
    public Objeto(int x, int y, double vx){
        this.fisica = new Fisica(x, y, vx);
    }
    public Objeto(int x, int y, double vx, int size){
        this.fisica = new Fisica(x, y ,vx, size);
    }

    public Fisica getFisica(){
        return this.fisica;
    }
}
