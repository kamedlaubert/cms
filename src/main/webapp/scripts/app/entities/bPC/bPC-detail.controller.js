'use strict';

angular.module('cmsApp')
    .controller('BPCDetailController', function ($scope, $rootScope, $stateParams, entity, BPC, Hospitalisation) {
        $scope.bPC = entity;
        $scope.load = function (id) {
            BPC.get({id: id}, function(result) {
                $scope.bPC = result;
            });
        };
        $rootScope.$on('cmsApp:bPCUpdate', function(event, result) {
            $scope.bPC = result;
        });
    });
