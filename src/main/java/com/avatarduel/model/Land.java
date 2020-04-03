package com.avatarduel.model;

public class Land extends Card {
  public Land() {
    super();
  }

  public Land(int id, String name, Element element, String description, String imagepath) {
    super(id, name, element, description, imagepath);
  }

  @Override
  public CardType getCardType() {
    return CardType.LAND;
  }
}