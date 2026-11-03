package andru.graphics.first;

import java.util.ArrayList;
import java.util.List;

public class Mundo {
    private final int ancho, alto;
    private final List<Objeto> objetos = new ArrayList<>();
    private final double g = 200; //Aceleración (px/s^2) positivo es hacia abajo

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
        for (Objeto o : objetos){
            o.getFisica().actualizar(dt, g);
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

    public int getAncho(){
        return this.ancho;
    }
    public int getAlto(){
        return this.alto;
    }
}
