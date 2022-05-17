Feature: Teleporting
  Everybody wants to use a pair of teleportgate

  Scenario: When connect two, not neighbour asteroid by player
    Given asteroids:
      | a1 |
      | a2 |
      | a3 |
    Given config Asteroid "a1"
    Given a player on asteroid "a1"
    Given config Asteroid "a2"
    Given config Asteroid "a3"
    Given neighbour between "a1" and "a2"
    Given neighbour between "a2" and "a3"

    Given teleports:
      | t1 |
      | t2 |
    Given pair "t1" and "t2" teleportgates
    Given add teleportgate "t1" to player
    Given add teleportgate "t2" to player
    Then the player should hold 2 teleport gates
    When the player put down a teleportgate
    Then the player should hold 1 teleport gates

    When the player moves to asteroid "a3"
    Then the player "stays on" asteroid "a1"
    When the player moves to asteroid "a2"
    Then the player "moved to" asteroid "a2"
    When the player moves to asteroid "a3"
    Then the player "moved to" asteroid "a3"
    When the player moves to asteroid "a1"
    Then the player "stays on" asteroid "a3"

    When the player put down a teleportgate
    Then the player should hold 0 teleport gates
    When the player moves to teleport "t2"
    Then the player "moved to" asteroid "a1"