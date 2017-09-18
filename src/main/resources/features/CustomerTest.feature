Feature: CRUD on customers

#  =================CREATE=================
  Scenario: Create a valid customer
    When client request POST /api/customers with json data:
    """
    {"name":"Tarja","surname":"Turunen","phone":"1234567890"}
    """
    Then the response code should be 201

  Scenario: Create an invalid customer with wrong phone number
    When client request POST /api/customers with json data:
    """
    {"name":"Philip","surname":"Anselmo","phone":"999"}
    """
    Then the response code should be 400

  Scenario: Create an invalid customer with null name
    When client request POST /api/customers with json data:
    """
    {"name":null,"surname":"Turunen","phone":"1111122222"}
    """
    Then the response code should be 400

  Scenario: Create a customer that already exists checked by phone
    When client request POST /api/customers with json data:
    """
    {"name":"Lemmy","surname":"Kilmister","phone":"1234567890"}
    """
    Then the response code should be 409

