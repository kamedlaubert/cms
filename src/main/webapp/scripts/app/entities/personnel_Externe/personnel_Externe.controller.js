'use strict';

angular.module('cmsApp')
    .controller('Personnel_ExterneController', function ($scope, Personnel_Externe, Personnel_ExterneSearch) {
        $scope.personnel_Externes = [];
        $scope.loadAll = function() {
            Personnel_Externe.query(function(result) {
               $scope.personnel_Externes = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Personnel_Externe.get({id: id}, function(result) {
                $scope.personnel_Externe = result;
                $('#deletePersonnel_ExterneConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Personnel_Externe.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePersonnel_ExterneConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            Personnel_ExterneSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.personnel_Externes = result;
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
            $scope.personnel_Externe = {id: null};
        };
    });
