Feature: Check the time in words
  Some explanatory blurb goes here...

  Scenario: Time on the hour in words
    Given the time "09:00"
    When translated
    Then it maps to time "naw o'r gloch"

  Scenario: Half past in words
    Given the time "09:30"
    When translated
    Then it maps to time "hanner awr wedi naw o'r gloch"

  Scenario: Five past the hour
    Given the time "09:05"
    When translated
    Then it maps to time "pum muned wedi naw o'r gloch"

  Scenario: Five to the hour
    Given the time "08:55"
    When translated
    Then it maps to time "pum muned i naw o'r gloch"