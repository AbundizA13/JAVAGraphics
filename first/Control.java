package andru.graphics.first;

import java.awt.event.KeyEvent;

public class Control {
    private final Eventos eventos;
    private boolean saltoAntes = false;
    private final int saltoImpulso = 40;
    private static final double ACEL = 400, FRENADO = 600, VEL_MAX = 80;

    public Control(Eventos eventos){ this.eventos = eventos; }

    public void aplicar(Fisica f, double dt){
        int dir = 0;
        if (eventos.estaPresionada(KeyEvent.VK_A)) dir -= 1;
        if (eventos.estaPresionada(KeyEvent.VK_D)) dir += 1;   // A y D a la vez se anulan: dir = 0

        Vec2 v = f.getVelocidad();

        double vx = v.getX();
        if (dir != 0) {
            vx += dir * ACEL * dt;                                        // acelera mientras está presionada
        } else {
            vx -= Math.signum(vx) * Math.min(Math.abs(vx), FRENADO * dt); // frena hacia 0 sin pasarse
        }
        vx = Math.max(-VEL_MAX, Math.min(VEL_MAX, vx));                   // el tope que te preocupaba
        v.setX(vx);

        boolean salto = eventos.estaPresionada(KeyEvent.VK_SPACE);
        if(salto && !saltoAntes) f.getVelocidad().incrementarY(-saltoImpulso);
        saltoAntes = salto;
    }

}
