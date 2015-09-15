'use strict';

angular.module('cmsApp')
    .controller('EnfantController', function ($scope, Enfant, EnfantSearch) {
        $scope.enfants = [];
        $scope.loadAll = function() {
            Enfant.query(function(result) {
               $scope.enfants = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Enfant.get({id: id}, function(result) {
                $scope.enfant = result;
                $('#deleteEnfantConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Enfant.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEnfantConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EnfantSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.enfants = result;
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
            $scope.enfant = {id: null};
        };
    });
