package com.avatarduel.gameutils;

import java.util.HashMap;

import com.avatarduel.model.Element;

public class GamePower {
    /**
     * Power list
     */
    private HashMap<Element, Integer> maxPowerList;
    /**
     * Power list
     */
    private HashMap<Element, Integer> currPowerList;

    /**
     * Constructor
     */
    public GamePower() {
        this.currPowerList = new HashMap<>();
        this.currPowerList.put(Element.AIR, 0);
        this.currPowerList.put(Element.EARTH, 0);
        this.currPowerList.put(Element.ENERGY, 0);
        this.currPowerList.put(Element.FIRE, 0);
        this.currPowerList.put(Element.WATER, 0);

        this.maxPowerList = new HashMap<>();
        this.maxPowerList.put(Element.AIR, 0);
        this.maxPowerList.put(Element.EARTH, 0);
        this.currPowerList.put(Element.ENERGY, 0);
        this.maxPowerList.put(Element.FIRE, 0);
        this.maxPowerList.put(Element.WATER, 0);
    }

    /**
     * Getter for the current powerList
     * @return this.currPowerList
     */
    public HashMap<Element, Integer> getCurrPowerList() {
        return this.currPowerList;
    }

    /**
     * Getter for the maximum powerList
     * @return this.maxPowerList
     */
    public HashMap<Element, Integer> getMaxPowerList() {
        return this.maxPowerList;
    }

    /**
     * Subtract current element power
     * @param element The Element to be set
     * @param value Subtract value
     */
    public void subCurrPower(Element element, int value) {
        assert this.currPowerList.get(element) - value >= 0; // TODO: remove assertion debug
        this.currPowerList.put(element, this.currPowerList.get(element) - value);
    }

    /**
     * Reset current power to max power
     */
    public void resetCurrPower() {
        this.currPowerList.put(Element.AIR, this.maxPowerList.get(Element.AIR));
        this.currPowerList.put(Element.EARTH, this.maxPowerList.get(Element.EARTH));
        this.currPowerList.put(Element.FIRE, this.maxPowerList.get(Element.FIRE));
        this.currPowerList.put(Element.WATER, this.maxPowerList.get(Element.WATER));
    }

    /**
     * Increment max element power
     * @param element The Element to be set
     */
    public void incMaxPower(Element element) {
        this.maxPowerList.put(element, this.maxPowerList.get(element) + 1);
    }
}
