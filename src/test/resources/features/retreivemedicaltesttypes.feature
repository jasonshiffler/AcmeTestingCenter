Feature:A user is able to retrieve the types of MedicalTests available from the testing center
  Scenario:client makes a call to GET /medicaltests
    When the client calls /medicaltests
    Then the client receives status code of 200
    And the client receives a list of the available medicaltests