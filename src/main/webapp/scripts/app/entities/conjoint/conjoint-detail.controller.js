'use strict';

angular.module('cmsApp')
    .controller('ConjointDetailController', function ($scope, $rootScope, $stateParams, entity, Conjoint, Personnel) {
        $scope.conjoint = entity;
        $scope.load = function (id) {
            Conjoint.get({id: id}, function(result) {
                $scope.conjoint = result;
            });
        };
        $rootScope.$on('cmsApp:conjointUpdate', function(event, result) {
            $scope.conjoint = result;
        });
    });
