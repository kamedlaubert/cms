'use strict';

angular.module('cmsApp')
    .controller('Personnel_ExterneDetailController', function ($scope, $rootScope, $stateParams, entity, Personnel_Externe) {
        $scope.personnel_Externe = entity;
        $scope.load = function (id) {
            Personnel_Externe.get({id: id}, function(result) {
                $scope.personnel_Externe = result;
            });
        };
        $rootScope.$on('cmsApp:personnel_ExterneUpdate', function(event, result) {
            $scope.personnel_Externe = result;
        });
    });
