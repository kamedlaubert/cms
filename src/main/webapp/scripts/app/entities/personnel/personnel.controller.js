'use strict';

angular.module('cmsApp')
    .controller('PersonnelController', function ($scope, Personnel, PersonnelSearch) {
        $scope.personnels = [];
        $scope.loadAll = function() {
            Personnel.query(function(result) {
               $scope.personnels = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Personnel.get({id: id}, function(result) {
                $scope.personnel = result;
                $('#deletePersonnelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Personnel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePersonnelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PersonnelSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.personnels = result;
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
            $scope.personnel = {matricule: null, nom: null, prenom: null, direction: null, age: null, sexe: null, type: null, famille: null, groupeSanguin: null, infosCompl: null, allergie: null, statut: null, id: null};
        };
    });
