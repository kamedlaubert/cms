'use strict';

angular.module('cmsApp')
    .controller('MedecinDetailController', function ($scope, $rootScope, $stateParams, entity, Medecin) {
        $scope.medecin = entity;
        $scope.load = function (id) {
            Medecin.get({id: id}, function(result) {
                $scope.medecin = result;
            });
        };
        $rootScope.$on('cmsApp:medecinUpdate', function(event, result) {
            $scope.medecin = result;
        });
    });
