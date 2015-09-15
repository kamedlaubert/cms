'use strict';

angular.module('cmsApp')
    .controller('OrdonnanceController', function ($scope, Ordonnance, OrdonnanceSearch) {
        $scope.ordonnances = [];
        $scope.loadAll = function() {
            Ordonnance.query(function(result) {
               $scope.ordonnances = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ordonnance.get({id: id}, function(result) {
                $scope.ordonnance = result;
                $('#deleteOrdonnanceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ordonnance.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOrdonnanceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            OrdonnanceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.ordonnances = result;
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
            $scope.ordonnance = {date: null, ligne: null, id: null};
        };
    });
