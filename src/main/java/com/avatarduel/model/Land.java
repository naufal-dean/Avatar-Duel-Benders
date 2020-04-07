package com.avatarduel.model;

public class Land extends Card {
  public Land() {
    super();
  }

  public Land(int id, String name, Element element, String description, String imagePath) {
    super(id, name, element, description, imagePath);
  }

  @Override
  public String getImagePath() {
      return "com/avatarduel/card/image/land/" + super.getImagePath();
  }

  @Override
  public CardType getCardType() {
    return CardType.LAND;
  }
}