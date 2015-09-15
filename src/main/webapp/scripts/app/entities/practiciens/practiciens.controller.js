'use strict';

angular.module('cmsApp')
    .controller('PracticiensController', function ($scope, Practiciens, PracticiensSearch) {
        $scope.practicienss = [];
        $scope.loadAll = function() {
            Practiciens.query(function(result) {
               $scope.practicienss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Practiciens.get({id: id}, function(result) {
                $scope.practiciens = result;
                $('#deletePracticiensConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Practiciens.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePracticiensConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PracticiensSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.practicienss = result;
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
            $scope.practiciens = {matricule: null, tel: null, email: null, type: null, id: null};
        };
    });
