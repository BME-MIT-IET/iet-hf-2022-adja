Feature: Drilling
  A settler wants to drill an asteroid layer

  Scenario: We can drill if the layer is bigger than 0
    Given an asteroid
    Given a player on it
    When I ask the player to drill
    Then the player should be "drilling"

  Scenario: We can't drill if the layer is 0
    Given an asteroid with zero thickness
    Given a player on it
    When I ask the player to drill
    Then the player should be "not drilling"