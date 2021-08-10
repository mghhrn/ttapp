package io.github.mghhrn.tin;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.mghhrn.tin.wave.WavFile;
import io.github.mghhrn.tin.wave.WavFileException;

public class TherapySessionActivity extends AppCompatActivity {

    private TextView minuteTextView;
    private TextView secondTextView;

    private long therapySessionId;
    private double selectedFrequency;
    private int duration;
    private int filteredRange;
    private String selectedBalance;
    private int volume;
    private Context context;
    private AudioTrack audioTrack;
    private short[] finalAudioBuffer;
    private File resultFile;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_session);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        therapySessionId = getIntent().getExtras().getLong("therapySessionId");
        selectedFrequency = getIntent().getExtras().getDouble("selectedFrequency");
        duration = getIntent().getExtras().getInt("duration");
        filteredRange = getIntent().getExtras().getInt("filteredRange");
        selectedBalance = getIntent().getExtras().getString("selectedBalance");

        minuteTextView = findViewById(R.id.minute);
        secondTextView = findViewById(R.id.second);
        context = this;

        Double frequencyMin = selectedFrequency - (filteredRange / 2);
        Double frequencyMax = selectedFrequency + (filteredRange / 2);
        try (InputStream wafFileInputStream = getResources().openRawResource(getResources().getIdentifier("pink_noise", "raw", getPackageName()));) {
            File file = new File(getCacheDir(), "cached.wav");
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = wafFileInputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            WavFile wavFile = WavFile.openWavFile(file);
            int numberOfFrames = (int)wavFile.getNumFrames();
            double[] buffer = new double[numberOfFrames];
            wavFile.readFrames(buffer, numberOfFrames);
            long FFT_SIZE = wavFile.getNumFrames() / 2;
            DoubleFFT_1D fft = new DoubleFFT_1D(FFT_SIZE);
            fft.realForward(buffer);

            for(int fftBin = 0; fftBin < FFT_SIZE; fftBin++){
                double frequency = (double)fftBin * 88200F / (double)FFT_SIZE;
                if( frequencyMin < frequency && frequency < frequencyMax){
                    int real = 2 * fftBin;
                    int imaginary = 2 * fftBin + 1;
                    buffer[real] = 0;
                    buffer[imaginary] = 0;
                }
            }
            fft.realInverse(buffer, true);
            File outputDir = context.getCacheDir();
            resultFile = File.createTempFile("cached_filtered", ".wav", outputDir);
            WavFile outFile = WavFile.newWavFile(resultFile, 1, wavFile.getNumFrames()/2, 16, wavFile.getSampleRate());
            outFile.writeFrames(buffer, numberOfFrames/2);
            outFile.close();

            outFile = WavFile.openWavFile(resultFile);
            int[] intBuffer = new int[numberOfFrames/2];
            outFile.readFrames(intBuffer, numberOfFrames/2);
            finalAudioBuffer = new short[intBuffer.length];
            for (int i = 0 ; i < intBuffer.length ; i++) {
                finalAudioBuffer[i] = (short) intBuffer[i];
            }
            outFile.close();
            wavFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error I/O!!!", Toast.LENGTH_SHORT);
        } catch (WavFileException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error wav file!!!", Toast.LENGTH_SHORT);
        }

        Runnable audioPlayer = new Runnable() {
            private boolean continuePlaying = true;
            @Override
            public void run() {
                int rate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, rate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        finalAudioBuffer.length,
                        AudioTrack.MODE_STREAM);
                audioTrack.play();
                switch (selectedBalance) {
                    case "LEFT":
                        audioTrack.setStereoVolume(1, 0);
                        break;
                    case "RIGHT":
                        audioTrack.setStereoVolume(0, 1);
                        break;
                    case "BOTH":
                        audioTrack.setStereoVolume(1, 1);
                        break;
                    default:
                        audioTrack.setStereoVolume(1, 1);
                        Toast.makeText(context, "unknown selected balance!", Toast.LENGTH_SHORT);
                }
                while (continuePlaying) {
                    audioTrack.write(finalAudioBuffer, 0, finalAudioBuffer.length);
                }
            }
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(audioPlayer);

        new CountDownTimer(duration * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long totalRemainingSeconds = millisUntilFinished / 1000;
                Long remainingMinutes = totalRemainingSeconds / 60;
                Long remainingSeconds = totalRemainingSeconds % 60;
                minuteTextView.setText(remainingMinutes.toString());
                secondTextView.setText(remainingSeconds.toString());
                volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }

            @Override
            public void onFinish() {
                executorService.shutdownNow();
                if (audioTrack != null) {
                    audioTrack.stop();
                    audioTrack.release();
                }
                resultFile.delete();
                Intent intent = new Intent(context, AssesmentActivity.class);
                intent.putExtra("therapySessionId", therapySessionId);
                intent.putExtra("volume", volume);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}