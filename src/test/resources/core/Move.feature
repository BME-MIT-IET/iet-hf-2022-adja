Feature: Is it moving?
  Everybody wants to move from an asteroid to another

  Given these asteroids:
    | start |
    | not neighbour |
    | neighbour |

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