package andru.graphics.first;

public class Fisica {
    private Vec2 posicion = new Vec2(); //Posición
    private Vec2 velocidad = new Vec2();
    final double rebote = 0.8;
    private final int size;


    public Fisica(double x, double y){
        this.posicion.setX(x);
        this.posicion.setY(y);
        //this.velocidad.setX(vx);
        this.size = 1;
    }
    public Fisica(double x, double y, int size){
        this.posicion.setX(x);
        this.posicion.setY(y);
        //this.velocidad.setX(vx);
        this.size = size;
    }

    public double getX() {
        return posicion.getX();
    }
    public double getY() {
        return posicion.getY();
    }
    public void setX(double x){
        this.posicion.setX(x);
    }
    public void setY(double y){
        this.posicion.setY(y);
    }
    public void actualizar(double dt, Vec2 g){
        this.velocidad.sumarEscalado(g, dt);
        this.posicion.sumarEscalado(velocidad, dt);

    }

    public void colisionarConObjeto(Fisica otra){
        double dx = otra.getCentroX() - getCentroX(), dy = otra.getCentroY() - getCentroY(); //Distancia relativa desde 'self' hacia 'otra'
        double minimo = (size + otra.size) / 2.0;

        //Deja los pixeles justo en contacto, recorre justo la cantidad penetrada
        if(Math.abs(dx) >= Math.abs(dy)){ //% de penet. mayor en ejeY
            //El signo solo indica hacia donde se debe mover cada obj.
            double signo = dx >= 0 ? 1 : -1;
            double penetracion = (minimo - Math.abs(dx)) / 2;
            posicion.decrementarX(signo * penetracion);
            otra.posicion.incrementarX(signo * penetracion);
            if ((otra.velocidad.getX() - velocidad.getX()) * signo < 0) { double t = velocidad.getX(); velocidad.setX(otra.velocidad.getX()); otra.velocidad.setX(t); }
        }else{ //% de penet. mayor en eje X
            double signo = dy >= 0 ? 1 : -1;
            double penetracion = (minimo - Math.abs(dy)) / 2;
            posicion.decrementarY(signo * penetracion);
            otra.posicion.incrementarY(signo * penetracion);
            if ((otra.velocidad.getY() - velocidad.getY()) * signo < 0) { double t = velocidad.getY(); velocidad.setY(otra.velocidad.getY()); otra.velocidad.setY(t); }
        }

        //Producto punto para saber si están viajando en direcciones contrarias
        if((otra.velocidad.getX() - velocidad.getX()) * dx + (otra.velocidad.getY() - velocidad.getY()) * dy < 0){
            double tmpVx = velocidad.getX(), tmpVy = velocidad.getY();
            //Intercambian velocidades
            velocidad.setX(otra.velocidad.getX()); velocidad.setY(otra.velocidad.getY());
            otra.velocidad.setX(tmpVx); otra.velocidad.setY(tmpVy);
        }
    }
    public boolean tocaA(Fisica otra){
        double minimo = (size + otra.size) / 2.0;
        return Math.abs(getCentroX() - otra.getCentroX()) < minimo
                && Math.abs(getCentroY() - otra.getCentroY()) < minimo;
    }
    //Obtienen el centro respecto a las coordenadas reales.
    private double getCentroX(){
        return posicion.getX() + size / 2.0;
    }
    private double getCentroY(){
        return posicion.getY() + size / 2.0;
    }

    public void reboteHorizontal(){ velocidad.setX(-velocidad.getX()*rebote);}
    public void reboteVertical(){ velocidad.setY(-velocidad.getY()*rebote);}
    public int getSize(){ return this.size;}

    public Vec2 getPosicion(){
        return this.posicion;
    }
    public void setPosicion(Vec2 posicion){
        this.posicion = posicion;
    }

    public Vec2 getVelocidad(){
        return this.velocidad;
    }
    public void setVelocidad(Vec2 velocidad){
        this.velocidad = velocidad;
    }
}
