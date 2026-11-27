package br.edu.urnacidada.app;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

/** Reproduz o som de confirmação sem interromper a interface Swing. */
public final class SomUrna {
    private SomUrna() { }

    public static void tocarConfirmacao() {
        try {
            URL arquivo = SomUrna.class.getResource("/sons/voto-confirmado.wav");
            if (arquivo == null) {
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }
            try (AudioInputStream audio = AudioSystem.getAudioInputStream(arquivo)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }
        } catch (Exception ex) {
            // O voto já foi salvo; o beep é apenas um recurso de acessibilidade.
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
}
