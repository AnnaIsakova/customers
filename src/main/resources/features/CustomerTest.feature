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


#    =================GET=================
  Scenario: Get all customers no content
    Given the db is empty
    When client request GET /api/customers/
    Then the response code should be 204

#    in Create a valid customer scenario a customer with id=1 was created and was deleted in Given the db is empty
#    that is why ids in the db start from 2
  Scenario: Get all customers
    Given the db is empty
    Given the following customers exist:
      |  name   |  surname   |    phone   |
      | Blackie |  Lawless   | 1111111111 |
      | Rob     |  Halford   | 2222222222 |
      | Yngwie  |  Malmsteen | 3333333333 |

    When client request GET /api/customers/
    Then the response code should be 200
    Then the result json should be:
    """
    [{"id": 2,"name": "Blackie","surname": "Lawless","phone": "1111111111"},
    {"id": 3,"name": "Rob","surname": "Halford","phone": "2222222222"},
    {"id": 4,"name": "Yngwie","surname": "Malmsteen","phone": "3333333333"}]
    """

  Scenario: Get a customer by id success
    When client request GET /api/customers/3
    Then the response code should be 200
    Then the result json should be:
    """
    {"id": 3,"name": "Rob","surname": "Halford","phone": "2222222222"}
    """
  Scenario: Get a customer by id no content
    When client request GET /api/customers/1
    Then the response code should be 404

#    =================UPDATE=================
  Scenario: Update a customer that exists with valid fields
    When client request PUT /api/customers with json data:
    """
    {"id":"4","name":"Lemmy","surname":"Kilmister","phone":"1234567890"}
    """
    Then the response code should be 200

  Scenario: Update a customer that does not exist
    When client request PUT /api/customers with json data:
    """
    {"id":"1","name":"Lemmy","surname":"Kilmister","phone":"1234567890"}
    """
    Then the response code should be 404

  Scenario: Update a customer that exists with invalid phone
    When client request PUT /api/customers with json data:
    """
    {"id":"4","name":"Lemmy","surname":"Kilmister","phone":"333"}
    """
    Then the response code should be 400

  Scenario: Update a customer that exists with invalid name
    When client request PUT /api/customers with json data:
    """
    {"id":"4","name":"","surname":"Kilmister","phone":"1234567890"}
    """
    Then the response code should be 400

  Scenario: Update a customer that exists and wants to take a phone that was already taken
    When client request PUT /api/customers with json data:
    """
    {"id":"4","name":"Lemmy","surname":"Kilmister","phone":"1111111111"}
    """
    Then the response code should be 409

#    =================DELETE=================
  Scenario: Delete customer success
    When client request DELETE /api/customers/4
    Then the response code should be 200

  Scenario: Delete customer not found
    When client request DELETE /api/customers/0
    Then the response code should be 404