'use strict';

angular.module('cmsApp')
    .controller('ExamensDetailController', function ($scope, $rootScope, $stateParams, entity, Examens, Consultation) {
        $scope.examens = entity;
        $scope.load = function (id) {
            Examens.get({id: id}, function(result) {
                $scope.examens = result;
            });
        };
        $rootScope.$on('cmsApp:examensUpdate', function(event, result) {
            $scope.examens = result;
        });
    });
