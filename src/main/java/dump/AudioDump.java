package dump;

import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioDump {
      public void run() {
            try {
                  consumeAudio();
            } catch (LineUnavailableException e) {
                  throw new RuntimeException("error", e);
            }
      }

      private void consumeAudio() throws LineUnavailableException {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                 44100.0F,
                                                 16,
                                                 1,
                                                 2,
                                                 44100.0F,
                                                 false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                                                   format);
            if (!AudioSystem.isLineSupported(info)) {
                  System.out.println("NOT SUPPORTED");
                  System.exit(1);
            }
            // Obtain and open the line.
            MFCC mfcc = new MFCC(4, 44100, 7);
            try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
                  line.open(format);
                  line.start();

                  while (true) {
                        byte[] bytes = new byte[1024*16];
                        ByteBuffer buf = ByteBuffer.wrap(bytes);
                        int pos = 0;
                        float[] floats = new float[bytes.length / 2];
                        while (pos < bytes.length) {
                              int read = line.read(bytes,
                                                   pos,
                                                   bytes.length - pos);
                              if (read <= 0) {
                                    line.stop();
                                    System.out.println("Q");
                                    return;
                              }
                              pos += read;
                        }

                        buf.rewind();

                        for (int i = 0; i < bytes.length / 2; i++) {
                              floats[i] = buf.getShort() / 32767.0f;
                        }
                        double[] doubles = mfcc.doMFCC(floats);
                        System.out.printf("\r%7.2f %7.2f %7.2f %7.2f %7.2f %7.2f %7.2f ",
                                          doubles[0], doubles[1], doubles[2],
                                          doubles[3], doubles[4], doubles[5], doubles[6]);

                  }
            }
      }

      public static void main(String[] args) {
            new AudioDump().run();
      }
}
