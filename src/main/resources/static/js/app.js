'use strict';

var app = angular.module('app',['ui.router']);

app.config(function($stateProvider) {

    $stateProvider
        .state('home', {
            url: '',
            views: {
                "customers": {
                    templateUrl: '/views/CustomersList.html',
                    controller: 'CustomersController'
                }
            }
        })
});