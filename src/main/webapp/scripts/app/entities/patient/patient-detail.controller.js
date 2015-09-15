'use strict';

angular.module('cmsApp')
    .controller('PatientDetailController', function ($scope, $rootScope, $stateParams, entity, Patient) {
        $scope.patient = entity;
        $scope.load = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
            });
        };
        $rootScope.$on('cmsApp:patientUpdate', function(event, result) {
            $scope.patient = result;
        });
    });
