package andru.graphics.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Pixel extends Canvas {
    static final int ANCHO = 160, ALTO=120, ESCALA=5;
    static final int tipo = BufferedImage.TYPE_INT_RGB;

    final BufferedImage imagen = new BufferedImage(ANCHO,ALTO,tipo);
    final int[] pixeles = ((DataBufferInt) imagen.getRaster().getDataBuffer()).getData();

    enum Tipo{ PRESIONADA, SUELTA}
    record EventoTecla(int tecla, Tipo tipo, long cuando){}

    final Queue<EventoTecla> eventos = new ConcurrentLinkedQueue<>();

    // El listener NO decide nada: solo traduce el evento de AWT a tu evento y lo encola.
    private void instalarInput() {
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                eventos.offer(new EventoTecla(e.getKeyCode(), Tipo.PRESIONADA, e.getWhen()));
            }
            @Override public void keyReleased(KeyEvent e) {
                eventos.offer(new EventoTecla(e.getKeyCode(), Tipo.SUELTA, e.getWhen()));
            }
        });
        setFocusable(true);
    }

    /*Estado físico (pixeles y segundos)*/
    double x = 20, y = 10; //Posición
    double vx = 40, vy = 0; //Velocidad
    final double g = 200; //Aceleración (px/s^2) positivo es hacia abajo
    final double rebote = 0.8; //Coef. de restitución

    boolean espacioAbajo = false;
    long paso = 0;

    public void dibujar(){
        Arrays.fill(pixeles, 0x000000);
        int posX = (int) x, posY = (int) y;

        if(estaDentroDeVentana(posX, posY)){
            pixeles[posY * ANCHO + posX] = 0xFFFFFF;
        }

        BufferStrategy bs = getBufferStrategy();
        Graphics gr = bs.getDrawGraphics();
        gr.drawImage(imagen, 0, 0, ANCHO*ESCALA,ALTO*ESCALA, null);
        gr.dispose();
        bs.show();

    }
    private boolean estaDentroDeVentana(int px, int py){
        final int izqPared = 0, techo = 0;

        return (px >= izqPared && px < ANCHO && py >= techo && py < ALTO);
    }

    public void actualizar(double dt){
        procesarEventos();

        vy += g*dt;
        x += vx * dt;
        y += vy * dt;

        detectarColisiones();

        paso++;
    }
    public void detectarColisiones(){
        if(y < 0 || y >= ALTO-1){ //Si el pixel pasa del piso
            y = Math.max(0, Math.min(ALTO-1, y)); //Ajusta posición para no salir de la ventana
            reboteVertical(); //Altera sus físicas para un rebote.
        }
        if(x < 0 || x >= ANCHO){
            x = Math.max(0, Math.min(ANCHO-1, x)); //Ajusta posición para no salir de la ventana
            reboteHorizontal();
        }
    }

    private void reboteHorizontal(){ vx = -vx;}
    private void reboteVertical(){ vy = -vy * rebote; }

    private void procesarEventos(){
        EventoTecla ev;
        while( (ev = eventos.poll()) != null){
            if (ev.tecla() !=  KeyEvent.VK_SPACE) continue; //Si no es espacio, no importa

            if (ev.tipo() == Tipo.PRESIONADA){
                if (espacioAbajo) continue; //Ya estaba presionado
                espacioAbajo = true;
                alPresionarEspacio();
            }else{
                espacioAbajo = false;
                alSoltarEspacio();
            }
        }
    }
    private void alPresionarEspacio(){
        vy -= 50;
        System.out.printf("\nPresionando espacio en paso #%d",paso);
    }
    private void alSoltarEspacio(){
        System.out.printf("\nSoltando espacio en paso #%d",paso);
    }

    public static void main(String[] args){
        Pixel lienzo = new Pixel();
        lienzo.setPreferredSize(new Dimension(ANCHO*ESCALA,ALTO*ESCALA));
        lienzo.instalarInput();

        JFrame ventana = new JFrame("A little pixel story");
        ventana.add(lienzo);
        ventana.pack();
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        lienzo.createBufferStrategy(2);

        /*while (true) {
            lienzo.dibujar();
            try { Thread.sleep(16); } catch (InterruptedException _) { }  // ~60 FPS
        }*/
        final double dt = 1.0 / 120; //Tasa de actualización fija.
        double acumulado = 0;
        long anterior = System.nanoTime();
        while(true){
            /*Guarda el tiempo exacto que pasó respecto al último frame*/
            long actual = System.nanoTime();
            acumulado += (actual - anterior) / 1e9;
            anterior = actual;

            //Si pasó al menos un 'dt' (la tasa fija)...
            while(acumulado >= dt){
                /*Se actualiza la física en una unidad de la tasa fija*/
                lienzo.actualizar(dt);
                acumulado -= dt;
                //Retira el deltatime que ya se calculó y reflejó en la física.
            }//Si han pasado más de un dt, este ciclo puede repetirse.
            lienzo.dibujar(); //Dibuja en la ventana una vez actualizadas sus posiciones-

            try{ Thread.sleep(1);} catch(InterruptedException _){}
        }
    }
}
