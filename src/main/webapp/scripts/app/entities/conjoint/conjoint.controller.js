'use strict';

angular.module('cmsApp')
    .controller('ConjointController', function ($scope, Conjoint, ConjointSearch) {
        $scope.conjoints = [];
        $scope.loadAll = function() {
            Conjoint.query(function(result) {
               $scope.conjoints = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Conjoint.get({id: id}, function(result) {
                $scope.conjoint = result;
                $('#deleteConjointConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Conjoint.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteConjointConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ConjointSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.conjoints = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.conjoint = {id: null};
        };
    });
