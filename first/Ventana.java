package andru.graphics.first;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

public class Ventana extends Canvas {
    static final int ESCALA = 5;
    static final int tipo = BufferedImage.TYPE_INT_RGB;
    private final Mundo mundo;

    final BufferedImage imagen;
    final int[] pixeles;

    public Ventana(Mundo mundo){
        this.mundo = mundo;
        imagen = new BufferedImage(mundo.getAncho(),mundo.getAlto(),tipo);
        pixeles = ((DataBufferInt) imagen.getRaster().getDataBuffer()).getData();
        setPreferredSize(new Dimension(mundo.getAncho() * ESCALA, mundo.getAlto() * ESCALA));

    }

    public void dibujar(){
        Arrays.fill(pixeles, 0x000000);


        for(Objeto objeto : mundo.getObjetos()){
            int posX = (int) Math.round(objeto.getFisica().getX()), posY = (int) Math.round(objeto.getFisica().getY());
            int size = objeto.getFisica().getSize();

            for(int py = posY; py < posY + size; py++){
                for(int px = posX; px < posX + size; px++){
                    if(estaDentroDeVentana(px, py)){
                        pixeles[py * mundo.getAncho() + px] = 0xFFFFFF;
                    }
                }
            }

            /*if(estaDentroDeVentana(posX, posY)){
                pixeles[posY * mundo.getAncho() + posX] = 0xFFFFFF;
            }*/
        }

        BufferStrategy bs = getBufferStrategy();
        Graphics gr = bs.getDrawGraphics();
        gr.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
        gr.dispose();
        bs.show();
    }
    private boolean estaDentroDeVentana(int px, int py){
        final int izqPared = 0, techo = 0;

        return (px >= izqPared && px < imagen.getWidth() && py >= techo && py < imagen.getHeight());
    }

    public static void main(String[] args){
        Random r = new Random();
        Mundo mundo = new Mundo(160, 120);
        for(int i = 10; i < 150; i+=r.nextInt(15)+1){
            for(int j = 20; j < 100; j+=r.nextInt(20)+1){
                mundo.agregarObjeto(new Objeto(i,j, r.nextInt(10),r.nextInt(3)+1));
            }
        }
        //mundo.agregarObjetos(new Objeto[]{new Objeto(0, 160), new Objeto(60, 160, 0)});
        Ventana canvas = new Ventana(mundo);

        JFrame ventana = new JFrame("Una simulación de pixel mejorada");
        ventana.add(canvas);
        ventana.pack();
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        canvas.createBufferStrategy(2);

        final double dt = 1.0 / 120;
        double acumulado = 0;
        long anterior = System.nanoTime();

        while(true){
            long actual = System.nanoTime();
            acumulado += (actual - anterior) / 1e9;
            anterior = actual;

            while(acumulado >= dt){ //Mientras se acumule al menos un "dt"
                mundo.actualizar(dt);
                acumulado -= dt;
            }
            canvas.dibujar();

            try{Thread.sleep(1);} catch(InterruptedException _){}
        }
    }
}
