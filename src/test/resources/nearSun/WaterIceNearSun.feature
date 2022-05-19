Feature: Water ice sublimation
  Water ice near sun sublime

  Scenario: Water ice not sublimate away from sun
    Given an asteroid with water ice core
    Given a settler
    When settler drill asteroid crust
    Then the asteroid's core should be "water ice"

  Scenario: Water ice sublimate near sun
    Given an asteroid with water ice core
    Given a settler
    When settler drill asteroid crust
    When I set asteroid near Sun
    Then the asteroid's core should be "sublimated"