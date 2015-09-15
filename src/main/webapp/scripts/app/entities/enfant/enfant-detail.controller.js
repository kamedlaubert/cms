'use strict';

angular.module('cmsApp')
    .controller('EnfantDetailController', function ($scope, $rootScope, $stateParams, entity, Enfant, Personnel) {
        $scope.enfant = entity;
        $scope.load = function (id) {
            Enfant.get({id: id}, function(result) {
                $scope.enfant = result;
            });
        };
        $rootScope.$on('cmsApp:enfantUpdate', function(event, result) {
            $scope.enfant = result;
        });
    });
