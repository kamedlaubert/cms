'use strict';

angular.module('cmsApp')
    .controller('InfirmirereController', function ($scope, Infirmirere, InfirmirereSearch) {
        $scope.infirmireres = [];
        $scope.loadAll = function() {
            Infirmirere.query(function(result) {
               $scope.infirmireres = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Infirmirere.get({id: id}, function(result) {
                $scope.infirmirere = result;
                $('#deleteInfirmirereConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Infirmirere.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInfirmirereConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            InfirmirereSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.infirmireres = result;
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
            $scope.infirmirere = {id: null};
        };
    });
