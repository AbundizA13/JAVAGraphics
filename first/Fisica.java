package andru.graphics.first;

public class Fisica {
    private double x, y; //Posición
    private double vx = 40, vy = 0;
    final double rebote = 0.8;
    private final int size;


    public Fisica(double x, double y){
        this.x = x;
        this.y = y;
        this.size = 1;
    }
    public Fisica(double x, double y, double vx){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.size = 1;
    }
    public Fisica(double x, double y, double vx, int size){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.size = size;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y){
        this.y = y;
    }
    public void actualizar(double dt, double g){
        vy += g*dt;
        x += vx * dt;
        y += vy * dt;
    }

    public void colisionarConObjeto(Fisica otra){
        double dx = otra.getCentroX() - getCentroX(), dy = otra.getCentroY() - getCentroY(); //Distancia relativa desde 'self' hacia 'otra'
        double minimo = (size + otra.size) / 2.0;

        //Deja los pixeles justo en contacto, recorre justo la cantidad penetrada
        if(Math.abs(dx) >= Math.abs(dy)){ //% de penet. mayor en ejeY
            //El signo solo indica hacia donde se debe mover cada obj.
            double signo = dx >= 0 ? 1 : -1;
            double penetracion = (minimo - Math.abs(dx)) / 2;
            x -= signo * penetracion;
            otra.x += signo * penetracion;
            if ((otra.vx - vx) * signo < 0) { double t = vx; vx = otra.vx; otra.vx = t; }
        }else{ //% de penet. mayor en eje X
            double signo = dy >= 0 ? 1 : -1;
            double penetracion = (minimo - Math.abs(dy)) / 2;
            y -= signo * penetracion;
            otra.y += signo * penetracion;
            if ((otra.vy - vy) * signo < 0) { double t = vy; vy = otra.vy; otra.vy = t; }
        }

        //Producto punto para saber si están viajando en direcciones contrarias
        if((otra.vx - vx) * dx + (otra.vy - vy) * dy < 0){
            double tmpVx = vx, tmpVy = vy;
            //Intercambian velocidades
            vx = otra.vx; vy = otra.vy;
            otra.vx = tmpVx; otra.vy = tmpVy;
        }
    }
    public boolean tocaA(Fisica otra){
        double minimo = (size + otra.size) / 2.0;
        return Math.abs(getCentroX() - otra.getCentroX()) < minimo
                && Math.abs(getCentroY() - otra.getCentroY()) < minimo;
    }
    //Obtienen el centro respecto a las coordenadas reales.
    private double getCentroX(){
        return x + size / 2.0;
    }
    private double getCentroY(){
        return y + size / 2.0;
    }

    public void reboteHorizontal(){ vx = -vx;}
    public void reboteVertical(){ vy = -vy * rebote; }
    public int getSize(){ return this.size;}
}
