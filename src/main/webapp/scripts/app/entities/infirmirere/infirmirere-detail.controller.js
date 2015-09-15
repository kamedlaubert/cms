'use strict';

angular.module('cmsApp')
    .controller('InfirmirereDetailController', function ($scope, $rootScope, $stateParams, entity, Infirmirere) {
        $scope.infirmirere = entity;
        $scope.load = function (id) {
            Infirmirere.get({id: id}, function(result) {
                $scope.infirmirere = result;
            });
        };
        $rootScope.$on('cmsApp:infirmirereUpdate', function(event, result) {
            $scope.infirmirere = result;
        });
    });
