'use strict';

app.controller('CustomersController', function($scope, $filter, $http, CrudService) {
    $scope.customers = [];

    var addingCustomer = false;
    fetchAll();

    function fetchAll(){
        CrudService.fetchAll('customers')
            .then(
                function(d) {
                    $scope.customers = d;
                },
                function(errResponse){
                    $scope.errorMessage = "Oops, error while fetching customers occurred :(\nPlease, try again later!";
                }
            );
    }

    function createOne(data){
        CrudService.createObj('customers', data)
            .then(
                function(d) {
                    $scope.errorMessage = null;
                    fetchAll();
                },
                function(errResponse){
                    if (errResponse.status == 400){
                        $scope.errorMessage = "Wrong data";
                    } else if (errResponse.status == 409){
                        $scope.errorMessage = "Customer with such phone already exists";
                    } else {
                        $scope.errorMessage = "Something went wrong";
                    }
                    $scope.customers.pop();
                }
            );
    }

    function editOne(data){
        CrudService.updateObj('customers', data)
            .then(
                function(d) {
                    $scope.errorMessage = null;
                    fetchAll();
                },
                function(errResponse){
                    if (errResponse.status == 400){
                        $scope.errorMessage = "Wrong data";
                    } else if (errResponse.status == 409){
                        $scope.errorMessage = "Customer with such phone already exists";
                    } else if (errResponse.status == 404){
                        $scope.errorMessage = "Customer was not found";
                    } else {
                        $scope.errorMessage = "Something went wrong";
                    }
                    fetchAll();
                }
            );
    }

    function deleteOne(id){
        CrudService.deleteObj('customers', id)
            .then(
                function(d) {
                    $scope.errorMessage = null;
                    fetchAll();
                },
                function(errResponse){
                    if (errResponse.status == 404){
                        $scope.errorMessage = "Customer was not found";
                    }
                }
            );
    }

    $scope.saveCustomer = function(data, id) {

        if (!validateCustomer(data)){
            $scope.customers.pop();
            return;
        }

        if (addingCustomer){
            createOne(data);
            addingCustomer = false;
        } else {
            angular.extend(data, {id: id});
            editOne(data);
        }

    };

    // remove user
    $scope.removeCustomer = function(index) {
        $scope.customers.splice(index, 1);
        deleteOne(index);
    };

    // add user
    $scope.addCustomer = function() {
        $scope.inserted = {
            name: '',
            surname: '',
            phone: ''
        };
        $scope.customers.push($scope.inserted);
        addingCustomer = true;
    };

    $scope.checkName = function(data) {
        console.log("DATA: " + data)
        if (data == '' || data == undefined) {
            return "<small style='color: darkred'>Required field</small>";
        }
    };

    function validateCustomer(data) {
        var validName = data.name != undefined && data.name != null && data.name != '';
        var validPhone = data.phone != undefined && /^([0-9]{10})$/.test(data.phone);;
        return (validName && validPhone);
    }
});