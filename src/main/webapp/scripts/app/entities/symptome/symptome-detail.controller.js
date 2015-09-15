'use strict';

angular.module('cmsApp')
    .controller('SymptomeDetailController', function ($scope, $rootScope, $stateParams, entity, Symptome, Consultation) {
        $scope.symptome = entity;
        $scope.load = function (id) {
            Symptome.get({id: id}, function(result) {
                $scope.symptome = result;
            });
        };
        $rootScope.$on('cmsApp:symptomeUpdate', function(event, result) {
            $scope.symptome = result;
        });
    });
