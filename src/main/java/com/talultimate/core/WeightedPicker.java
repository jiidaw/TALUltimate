package com.talultimate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedPicker<T> {
    private static final Random R = new Random();
    private final List<T> items = new ArrayList<>();
    private final List<Integer> weights = new ArrayList<>();
    private int total = 0;

    public void add(T item, int weight){
        if (weight <= 0) return;
        items.add(item);
        weights.add(weight);
        total += weight;
    }

    public T pick(){
        if (items.isEmpty() || total <= 0) return null;
        int r = R.nextInt(total);
        int sum = 0;
        for (int i=0;i<items.size();i++){
            sum += weights.get(i);
            if (r < sum) return items.get(i);
        }
        return items.get(items.size()-1);
    }
}
