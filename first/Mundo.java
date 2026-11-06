package andru.graphics.first;

import java.util.ArrayList;
import java.util.List;

public class Mundo {
    private final int ancho, alto;
    private final List<Objeto> objetos = new ArrayList<>();
    private final Vec2 g = new Vec2(0, 200); //Aceleración (px/s^2) positivo es hacia abajo
    private Jugador jugador = null;

    public void agregarObjeto(Objeto o) {objetos.add(o);}
    public void agregarObjetos(Objeto[] objetos){
        for(Objeto o : objetos){
            this.objetos.add(o);
        }
    }
    public List<Objeto> getObjetos(){ return this.objetos;}
    public int getObjetosSize(){ return this.objetos.size();}

    public Mundo(int ancho, int alto){
        this.ancho = ancho;
        this.alto = alto;
    }

    public void actualizar(double dt){
        //Eventos.procesarEventosJugador(this.jugador);

        for (Objeto o : objetos){
            if(o.getControl() != null){
                o.getControl().aplicar(o.getFisica(), dt);
            }
            o.getFisica().actualizar(dt, g);
            o.updateColor();
        }
        detectarColisiones();
    }

    private void detectarColisiones(){
        for(int i = 0; i < objetos.size(); i++){
            detectarColisionesEntreObjetos(objetos.get(i), i);
        }
        for(Objeto o : objetos){ //Las paredes al final, porque separar puede sacar un pixel del mundo
            Fisica f = o.getFisica();
            int size = f.getSize();
            if(f.getY() < 0 || f.getY() > alto-size){
                f.setY(Math.max(0, Math.min(alto-size, f.getY())));
                f.reboteVertical();
            }
            if(f.getX() < 0 || f.getX() > ancho-size){
                f.setX(Math.max(0, Math.min(ancho-size, f.getX())));
                f.reboteHorizontal();
            }
        }
    }
    private void detectarColisionesEntreObjetos(Objeto origen, int indice){
        for(int j = indice+1; j < getObjetosSize(); j++){
            Fisica a = origen.getFisica(), b = objetos.get(j).getFisica();
            if(a.tocaA(b)){
                a.colisionarConObjeto(b);
            }
        }
    }
    public void vincularJugador(Jugador jugador){
        this.jugador = jugador;//new Jugador(0,0, 10);
        objetos.add(jugador);
    }

    public int getAncho(){
        return this.ancho;
    }
    public int getAlto(){
        return this.alto;
    }
}
