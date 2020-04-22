Feature:A user is able to retrieve a MedicalTest by id
  Scenario:client makes a call to GET /medicaltests/{id} with a good id
    When the client calls /medicaltests/good_id
    Then the client receives status code of 200
    And the client receives a json representation of the medical test

  Scenario:client makes a call to GET /medicaltests/{id} with a bad id
    When the client calls /medicaltests/bad_id
    Then the client receives status code of 404