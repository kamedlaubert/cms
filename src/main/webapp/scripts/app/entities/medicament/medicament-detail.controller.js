'use strict';

angular.module('cmsApp')
    .controller('MedicamentDetailController', function ($scope, $rootScope, $stateParams, entity, Medicament, Ordonnance) {
        $scope.medicament = entity;
        $scope.load = function (id) {
            Medicament.get({id: id}, function(result) {
                $scope.medicament = result;
            });
        };
        $rootScope.$on('cmsApp:medicamentUpdate', function(event, result) {
            $scope.medicament = result;
        });
    });
