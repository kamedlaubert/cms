'use strict';

angular.module('cmsApp')
    .controller('MedicamentController', function ($scope, Medicament, MedicamentSearch) {
        $scope.medicaments = [];
        $scope.loadAll = function() {
            Medicament.query(function(result) {
               $scope.medicaments = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Medicament.get({id: id}, function(result) {
                $scope.medicament = result;
                $('#deleteMedicamentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Medicament.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMedicamentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MedicamentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.medicaments = result;
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
            $scope.medicament = {nom: null, posologie: null, indParticulier: null, id: null};
        };
    });
