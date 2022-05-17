Feature: Moving
  Everybody wants to move from an asteroid to another

  Scenario: We can't move if there are no neighbour asteroid
    Given an asteroid
    Given a player on it
    When I ask the player to move into void
    Then the player should be "not moved"

  Scenario: We can't move if the target asteroid is not neighbour
    Given an asteroid
    Given a player on it
    Given an another asteroid
    When I ask the player to move there
    Then the player "stay still"
    Then the player should be2 "oks"

  Scenario: We can move, if the target asteroid is a neighbour
    Given an asteroid
    Given a player on it
    Given a neighbour asteroid
    When I ask the player to move to the neighbour
    Then the player "moved there"