package andru.graphics.first;

import javax.swing.*;
import java.util.Random;

public class Motor {
    private static final int[] dimensiones_mundo = {160, 120};
    private static final Mundo mundo = new Mundo(dimensiones_mundo[0], dimensiones_mundo[1]);
    private static final Ventana canvas = new Ventana(mundo);

    public static void main(String[] args){
        crearObjetos();

        JFrame ventana = new JFrame("Ahora con clase Motor.");
        ventana.add(canvas);
        ventana.pack();
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
        canvas.createBufferStrategy(2);

        cicloFisica();
    }

    private static void crearObjetos() {
        Random r = new Random();
        for (int i = 10; i < 150; i += r.nextInt(15) + 1) {
            for (int j = 20; j < 100; j += r.nextInt(20) + 1) {
                Objeto nuevo = new Objeto(i, j, r.nextInt(3) + 1);
                nuevo.setVelocidad(r.nextInt(10), r.nextInt(30));
                mundo.agregarObjeto(nuevo);

            }
        }
        Objeto color = new Objeto(0, 0, 10);
        color.setVelocidad(0, 120); color.setColor(0x99FFFF);
        mundo.agregarObjeto(color);
    }
    private static void cicloFisica(){
        final double dt = 1.0/120;
        double acumulado = 0;
        long anterior = System.nanoTime();

        while(true){
            long actual = System.nanoTime();
            acumulado += (actual-anterior)/1e9;
            acumulado = Math.min(acumulado, 0.25); //Evitar saltos largos
            anterior = actual;

            while(acumulado >= dt){ //Mientras se tenga acumulado al menos un "dt"
                mundo.actualizar(dt);
                acumulado -= dt;
            }
            canvas.dibujar();

            try{Thread.sleep(1);} catch(InterruptedException _){}
        }
    }
}
