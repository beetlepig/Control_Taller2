package serializable;

import java.io.Serializable;

public class InstruccionAndroid implements Serializable{


    private static final long serialVersionUID = 1L;
    public float x;
    public float y;
    public float angulo;
    public float fuerza;
    public float releaseAngle;
    public float[] posRelease;
    public boolean isReleased;
    public String instrucccionExtra;


    public InstruccionAndroid(float x, float y, float angulo, float fuerza, float releaseAngle, float[]posRelease, boolean isReleased, String instruccion){
        this.x=x;
        this.y=y;
        this.angulo=angulo;
        this.fuerza=fuerza;
        this.releaseAngle=releaseAngle;
        this.posRelease=posRelease;
        this.isReleased=isReleased;
        this.instrucccionExtra=instruccion;
    }

}
