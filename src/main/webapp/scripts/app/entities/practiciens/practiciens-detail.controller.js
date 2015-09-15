'use strict';

angular.module('cmsApp')
    .controller('PracticiensDetailController', function ($scope, $rootScope, $stateParams, entity, Practiciens, Consultation) {
        $scope.practiciens = entity;
        $scope.load = function (id) {
            Practiciens.get({id: id}, function(result) {
                $scope.practiciens = result;
            });
        };
        $rootScope.$on('cmsApp:practiciensUpdate', function(event, result) {
            $scope.practiciens = result;
        });
    });
