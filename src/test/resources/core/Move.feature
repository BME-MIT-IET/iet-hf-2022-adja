Feature: Is it moving?
  Everybody wants to move from an asteroid to another

  Given these asteroids:
    | start |
    | not neighbour |
    | neighbour |

  Scenario: We can't move if there are no neighbour asteroid
    Given an asteroid
    Given a player
    When I ask the player to move
    Then I should be told "Not Moved"