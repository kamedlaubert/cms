'use strict';

angular.module('cmsApp')
    .controller('InstitutionSanitaireDetailController', function ($scope, $rootScope, $stateParams, entity, InstitutionSanitaire, Hospitalisation) {
        $scope.institutionSanitaire = entity;
        $scope.load = function (id) {
            InstitutionSanitaire.get({id: id}, function(result) {
                $scope.institutionSanitaire = result;
            });
        };
        $rootScope.$on('cmsApp:institutionSanitaireUpdate', function(event, result) {
            $scope.institutionSanitaire = result;
        });
    });
