package com.caletateam.caletapp.app.md.models;

import androidx.annotation.NonNull;

import com.caletateam.caletapp.app.utils.Functions;

import java.util.List;

public class ValueModel {
    private long timestamp;
    private String type;
    private Float values[];
    boolean anomaly;
    public long getTimestamp() {
        return timestamp;
    }

    public ValueModel(String type) {

        this.type = type;
        anomaly = false;
        if(type.equals(Functions.TYPE_ACTIVITY))
            values = new Float[3];
        else values = new Float[1];
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Float[] getValues(){
        /*if(type.equals(Functions.TYPE_ACTIVITY))
            return getProcessedValueActivity();
        return getValue();*/
        return values;
    }
    private Float[] getValuesF() {
            return values;

    }

    public float getProcessedValueActivity(){
        return (float) ((values[0]+values[1]+values[2])/3.0);
    }
    public boolean isAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }

    public Float getValue(){
        return values[0];
    }

    public void setValue(Object value){
        if(type.equals(Functions.TYPE_ACTIVITY))
            values = (Float[]) value;
        else values[0] = (Float) value;
    }
    public void setValues(Float[] values) {
        this.values = values;
    }

    @NonNull
    @Override
    public String toString() {
        return "Type:"+type+"   --- "+"Value:"+values[0] +"  Anomnaly: "+anomaly+"   - Timestamp:"+timestamp;
    }
}
