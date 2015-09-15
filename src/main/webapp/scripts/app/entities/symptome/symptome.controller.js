'use strict';

angular.module('cmsApp')
    .controller('SymptomeController', function ($scope, Symptome, SymptomeSearch) {
        $scope.symptomes = [];
        $scope.loadAll = function() {
            Symptome.query(function(result) {
               $scope.symptomes = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Symptome.get({id: id}, function(result) {
                $scope.symptome = result;
                $('#deleteSymptomeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Symptome.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSymptomeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SymptomeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.symptomes = result;
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
            $scope.symptome = {libelle: null, description: null, id: null};
        };
    });
