Feature: Uran if nesesary explode
  Uran near sun explode if it's 3. time contact

  Scenario: Uran not explode away from sun
    Given an asteroid with uran core
    Given a settler
    When I set asteroid with uran near Sun
    Then the asteroid's core should be "uran"

  Scenario: Uran not explode at first contact
    Given an asteroid with uran core
    Given a settler
    When settler drill asteroid crust
    When I set asteroid with uran near Sun
    Then the asteroid's core should be "uran"

  Scenario: Uran not explode at second contact
    Given an asteroid with uran core
    Given a settler
    When settler drill asteroid crust
    When I set asteroid with uran near Sun
    When I set asteroid with uran near Sun
    Then the asteroid's core should be "uran"