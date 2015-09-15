'use strict';

angular.module('cmsApp')
    .controller('PersonnelDetailController', function ($scope, $rootScope, $stateParams, entity, Personnel, Consultation, Conjoint, Enfant) {
        $scope.personnel = entity;
        $scope.load = function (id) {
            Personnel.get({id: id}, function(result) {
                $scope.personnel = result;
            });
        };
        $rootScope.$on('cmsApp:personnelUpdate', function(event, result) {
            $scope.personnel = result;
        });
    });
