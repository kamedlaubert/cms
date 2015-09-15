'use strict';

angular.module('cmsApp')
    .controller('OrdonnanceDetailController', function ($scope, $rootScope, $stateParams, entity, Ordonnance, Consultation, Medicament) {
        $scope.ordonnance = entity;
        $scope.load = function (id) {
            Ordonnance.get({id: id}, function(result) {
                $scope.ordonnance = result;
            });
        };
        $rootScope.$on('cmsApp:ordonnanceUpdate', function(event, result) {
            $scope.ordonnance = result;
        });
    });
