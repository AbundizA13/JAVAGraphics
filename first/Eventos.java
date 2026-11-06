package andru.graphics.first;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Eventos {
    enum Estado{PRESIONADA, SUELTA}
    record EventoTecla(int tecla, Estado estado, long momento){}
    private final Set<Integer> teclasPresionadas = ConcurrentHashMap.newKeySet();

    public boolean estaPresionada(int tecla){ return teclasPresionadas.contains(tecla);}

    public void instalarInput(Component componente){
        componente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                teclasPresionadas.add(e.getKeyCode());
            }
            @Override
            public void keyReleased(KeyEvent e){
                teclasPresionadas.remove(e.getKeyCode());
            }
        });
        componente.setFocusable(true);
    }

    /*public void procesarEventosJugador(Objeto jugador){
        EventoTecla ev;
        while((ev = eventos.poll()) != null){ //Saca directamente un evento y lo comprueba
            if(ev.tecla() != KeyEvent.VK_SPACE) continue;


            if(ev.estado() == Estado.PRESIONADA){
                if(estaPresionada(ev.tecla)) continue; //Ya estaba presionada
                teclasPresionadas.add(KeyEvent.VK_SPACE);
                jugador.getFisica().getVelocidad().decrementarY(50); //Salto
                //System.out.printf("\nPresionando espacio en el momento #%d",ev.momento());
            }else{
                teclasPresionadas.remove(KeyEvent.VK_SPACE);
                //System.out.printf("\nSoltando espacio en el momento #%d",ev.momento);
            }
        }
    }*/
}
