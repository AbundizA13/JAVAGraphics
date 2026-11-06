package andru.graphics.first;

public class Objeto {
    private final Fisica fisica;
    private int color = 0xFFFFFF; //Blanco por defecto



    public Objeto(int x, int y){
        this.fisica = new Fisica(x, y);
    }
    public Objeto(int x, int y, int size){
        this.fisica = new Fisica(x, y, size);
    }
    public void setVelocidad(int vx, int vy){
        Vec2 velocidad = new Vec2(vx, vy);
        this.fisica.setVelocidad(velocidad);
    }
    public void setColor(int color){
        this.color = color;
    }
    public int getColor(){
        return this.color;
    }
    public void updateColor(){
        double velocidad = Math.abs(fisica.getVelocidad().getY());
        if(velocidad < 1){
            this.color = 0x8BE97C;
        }else if(velocidad < 30){
            this.color = 0xDCFA88;
        }else if(velocidad < 100) {
            this.color = 0xFFCC66;
        }else{
            this.color = 0xFC4747;
        }
    }


    public Fisica getFisica(){
        return this.fisica;
    }
}
