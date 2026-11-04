package andru.graphics.first;

public class Vec2 {
    private double x;
    private double y;

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
}
