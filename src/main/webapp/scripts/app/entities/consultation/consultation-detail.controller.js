'use strict';

angular.module('cmsApp')
    .controller('ConsultationDetailController', function ($scope, $rootScope, $stateParams, entity, Consultation, Practiciens, Ordonnance, Symptome, Hospitalisation, Examens, Personnel) {
        $scope.consultation = entity;
        $scope.load = function (id) {
            Consultation.get({id: id}, function(result) {
                $scope.consultation = result;
            });
        };
        $rootScope.$on('cmsApp:consultationUpdate', function(event, result) {
            $scope.consultation = result;
        });
    });
