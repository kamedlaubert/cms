'use strict';

angular.module('cmsApp')
    .controller('MedecinController', function ($scope, Medecin, MedecinSearch) {
        $scope.medecins = [];
        $scope.loadAll = function() {
            Medecin.query(function(result) {
               $scope.medecins = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Medecin.get({id: id}, function(result) {
                $scope.medecin = result;
                $('#deleteMedecinConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Medecin.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMedecinConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MedecinSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.medecins = result;
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
            $scope.medecin = {id: null};
        };
    });
