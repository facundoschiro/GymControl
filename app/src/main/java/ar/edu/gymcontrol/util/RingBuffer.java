package ar.edu.gymcontrol.util;

import java.util.ArrayList;
import java.util.List;

public class RingBuffer<T> {
    private final Object[] data;
    private int size=0, head=0;

    public RingBuffer(int capacity){ data = new Object[capacity]; }
    public void add(T item){
        data[(head+size)%data.length] = item;
        if (size < data.length) size++; else head = (head+1)%data.length;
    }
    @SuppressWarnings("unchecked")
    public List<T> toList(){
        List<T> out = new ArrayList<>(size);              // <- ArrayList complementando al array
        for (int i=0;i<size;i++) out.add((T)data[(head+i)%data.length]);
        return out;
    }
}
