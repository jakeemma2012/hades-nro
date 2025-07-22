/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.lib;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @param <E>
 *
 */
public class RandomCollection<E> {

    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public RandomCollection<E> add(double weight, E result, String function) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    // Method to remove a value
    public boolean remove(E result) {
        for (Double key : map.keySet()) {
            if (map.get(key).equals(result)) {
                total -= key;
                map.remove(key);
                return true;
            }
        }
        return false;
    }

    public void print() {
        for(Double key : map.keySet()){
            System.err.println("WEIGHT : " + key + " ,value : "+ map.get(key));
        }
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
