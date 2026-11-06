package andru.graphics.first;

public class Vec2 {
    private double x = 0;
    private double y = 0;

    public Vec2() {
    }
    public Vec2(double x, double y){
        this.x = x; this.y =  y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void incrementarX(double incremento){
        this.x += incremento;
    }
    public void incrementarY(double incremento){
        this.y += incremento;
    }
    public void decrementarX(double decremento){
        this.x -= decremento;
    }
    public void decrementarY(double decremento){
        this.y -= decremento;
    }
    public void sumarEscalado(Vec2 v, double dt){
        this.x += v.x*dt;
        this.y += v.y*dt;
    }
    public double magnitud(){
        return Math.sqrt(x*x + y*y);
    }
}
