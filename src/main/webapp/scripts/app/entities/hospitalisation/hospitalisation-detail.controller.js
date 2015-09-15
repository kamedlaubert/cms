'use strict';

angular.module('cmsApp')
    .controller('HospitalisationDetailController', function ($scope, $rootScope, $stateParams, entity, Hospitalisation, Consultation, BPC, InstitutionSanitaire) {
        $scope.hospitalisation = entity;
        $scope.load = function (id) {
            Hospitalisation.get({id: id}, function(result) {
                $scope.hospitalisation = result;
            });
        };
        $rootScope.$on('cmsApp:hospitalisationUpdate', function(event, result) {
            $scope.hospitalisation = result;
        });
    });
