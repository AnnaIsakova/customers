'use strict';

app.factory('CrudService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI = '/api/';

    var factory = {
        fetchAll: fetchAll,
        createObj:createObj,
        deleteObj:deleteObj,
        updateObj:updateObj
    };

    return factory;

    function fetchAll(entity) {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + entity)
            .then(
                function (response) {
                    if (response.status == 200) {
                        deferred.resolve(response.data);
                    }
                },
                function(errResponse){
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function createObj(name, object) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + name, object)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function updateObj(name, object) {
        var deferred = $q.defer();
        var object_json = angular.toJson(object);

        $http.put(REST_SERVICE_URI + name, object_json)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteObj(name, id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI + name + "/" + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);