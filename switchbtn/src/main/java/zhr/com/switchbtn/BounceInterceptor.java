package zhr.com.switchbtn;

import android.view.animation.Interpolator;

public class BounceInterceptor implements Interpolator{

    private float amplitude;

    public BounceInterceptor(float amplitude, float frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    private float frequency;

    @Override
    public float getInterpolation(float v) {
        return (float) (-1 * Math.pow(Math.E, -v / amplitude) * Math.cos(frequency * v) + 1);
    }
}
