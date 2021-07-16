package io.github.mghhrn.tin;

import android.animation.ValueAnimator;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import io.github.mghhrn.tin.view.Display;
import io.github.mghhrn.tin.view.Knob;


public class FrequencySelectionActivity extends AppCompatActivity implements Knob.OnKnobChangeListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, ValueAnimator.AnimatorUpdateListener {


    private Audio audio;
    private Knob knob;
    private Display display;
    private SeekBar fine;
    private PowerManager.WakeLock wakeLock;

    private static final String LOCK = "Tin:lock";
    private static final int MAX_FINE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency_selection);

        knob = findViewById(R.id.knob);
        display = findViewById(R.id.display);
        fine = findViewById(R.id.fine);

        // Get wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK);

        audio = new Audio();
        audio.start();

        setupWidgets();
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        // Get value
        float value = (Float) animation.getAnimatedValue();

        // Set knob value
        if (knob != null)
            knob.setValue(value);
    }


    // On click
    @Override
    public void onClick(View v)
    {
        // Check id
        int id = v.getId();
        switch (id)
        {
            // Lower
            case R.id.lower:
                if (fine != null)
                {
                    int progress = fine.getProgress();
                    fine.setProgress(--progress);
                }
                break;

            // Higher
            case R.id.higher:
                {
                    int progress = fine.getProgress();
                    fine.setProgress(++progress);
                }
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser)
    {
        if (audio == null)
            return;

        double frequency = Math.pow(10.0, knob.getValue() /
                200.0) * 10.0;
        double adjust = ((progress - MAX_FINE / 2) /
                (double) MAX_FINE) / 50.0;

        frequency += frequency * adjust;

        if (display != null)
            display.setFrequency(frequency);

        if (audio != null)
            audio.frequency = frequency;
    }



    @Override
    public void onKnobChange(Knob knob, float value) {
        // Frequency
        double frequency = Math.pow(10.0, value / 200.0) * 10.0;
        double adjust = ((fine.getProgress() - MAX_FINE / 2) /
                (double) MAX_FINE) / 100.0;

        frequency += frequency * adjust;

        // Display
        if (display != null)
            display.setFrequency(frequency);

        if (audio != null)
            audio.frequency = frequency;
    }


    // Set up widgets
    private void setupWidgets()
    {
        View v;

        if (knob != null) {
            knob.setOnKnobChangeListener(this);
            knob.setValue(400);
        }

        v = findViewById(R.id.lower);
        if (v != null) {
            v.setOnClickListener(this);
        }

        v = findViewById(R.id.higher);
        if (v != null) {
            v.setOnClickListener(this);
        }

        if (fine != null) {
            fine.setOnSeekBarChangeListener(this);
            fine.setMax(MAX_FINE);
            fine.setProgress(MAX_FINE / 2);
        }
    }


    // Not Used
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    // Not Used
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    // Audio
    protected class Audio implements Runnable
    {
        protected static final int SINE = 0;

//        protected int waveform;
        protected boolean mute;

        protected double frequency;
        protected double level;

        protected Thread thread;

        private AudioTrack audioTrack;

        protected Audio()
        {
            frequency = 440.0;
            level = 16384;
        }

        // Start
        protected void start()
        {
            thread = new Thread(this, "Audio");
            thread.start();
        }

        // Stop
        protected void stop()
        {
            Thread t = thread;
            thread = null;

            // Wait for the thread to exit
            while (t != null && t.isAlive())
                Thread.yield();
        }

        public void run()
        {
            processAudio();
        }

        // Process audio
        @SuppressWarnings("deprecation")
        protected void processAudio()
        {
            short buffer[];

            int rate =
                    AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
            int minSize =
                    AudioTrack.getMinBufferSize(rate, AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);

            // Find a suitable buffer size
            int sizes[] = {1024, 2048, 4096, 8192, 16384, 32768};
            int size = 0;

            for (int s : sizes)
            {
                if (s > minSize)
                {
                    size = s;
                    break;
                }
            }

            final double K = 2.0 * Math.PI / rate;

            // Create the audio track
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, rate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    size, AudioTrack.MODE_STREAM);
            // Check audioTrack

            // Check state
            int state = audioTrack.getState();

            if (state != AudioTrack.STATE_INITIALIZED)
            {
                audioTrack.release();
                return;
            }

            audioTrack.play();

            // Create the buffer
            buffer = new short[size];

            // Initialise the generator variables
            double f = frequency;
            double l = 0.0;
            double q = 0.0;

            while (thread != null)
            {
                // Fill the current buffer
                for (int i = 0; i < buffer.length; i++)
                {
                    f += (frequency - f) / 4096.0;
                    l += ((false ? 0.0 : level) * 16384.0 - l) / 4096.0;
                    q += (q < Math.PI) ? f * K : (f * K) - (2.0 * Math.PI);

                    buffer[i] = (short) Math.round(Math.sin(q) * l);
                }

                audioTrack.write(buffer, 0, buffer.length);
            }

            audioTrack.stop();
            audioTrack.release();
        }
    }
}