'use strict';

angular.module('cmsApp')
    .controller('InstitutionSanitaireController', function ($scope, InstitutionSanitaire, InstitutionSanitaireSearch) {
        $scope.institutionSanitaires = [];
        $scope.loadAll = function() {
            InstitutionSanitaire.query(function(result) {
               $scope.institutionSanitaires = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            InstitutionSanitaire.get({id: id}, function(result) {
                $scope.institutionSanitaire = result;
                $('#deleteInstitutionSanitaireConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            InstitutionSanitaire.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInstitutionSanitaireConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            InstitutionSanitaireSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.institutionSanitaires = result;
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
            $scope.institutionSanitaire = {raisonSocial: null, nom: null, type: null, id: null};
        };
    });
