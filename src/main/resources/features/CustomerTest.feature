Feature: CRUD on customers

  Scenario: Create a valid customer
    When client request POST /api/customers with json data:
    """
    {"name":"Tarja","surname":"Turunen","phone":"888"}
    """
    Then the response code should be 201

  Scenario: Create an invalid customer
    When client request POST /api/customers with json data:
    """
    {"name":null,"surname":"Turunen","phone":"999"}
    """
    Then the response code should be 400